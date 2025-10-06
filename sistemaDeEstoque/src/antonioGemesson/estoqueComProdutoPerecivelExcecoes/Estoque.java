package antonioGemesson.estoqueComProdutoPerecivelExcecoes;

import antonioGemesson.estoqueComProdutoPerecivelExcecoes.Excecoes.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Estoque implements InterfaceEstoqueComExcecoes {

    private List<Produto> produtos = new ArrayList<>();

    // Método auxiliar para verificar se produto existe sem lançar exceção
    private Produto buscarSemExcecao(int cod) {
        for (Produto p : produtos) {
            if (p.getCodigo() == cod) return p;
        }
        return null;
    }

    // Método auxiliar para registrar movimentação
    private void registrarMovimentacao(Produto p, String tipo, int quant, double valor) {
        Movimentacao m = new Movimentacao(new Date(), tipo, quant, valor, p.getQuantidadeTotal());
        p.addMovimentacao(m);
    }

    @Override
    public void incluir(Produto p) throws DadosInvalidos, ProdutoJaCadastrado {
        if (p == null) throw new DadosInvalidos("Produto nulo");
        if (p.getCodigo() < 0 || p.getLucro() < 0 || p.getMinimoEstoque() <= 0 || p.getDescricao().isEmpty()) {
            throw new DadosInvalidos("Dados inválidos");
        }
        if (buscarSemExcecao(p.getCodigo()) != null) {
            throw new ProdutoJaCadastrado("Produto já existe no estoque!");
        }
        produtos.add(p);
    }

    @Override
    public void comprar(int cod, int quant, double preco, Date val) throws ProdutoInexistente, DadosInvalidos, ProdutoNaoPerecivel {
        Produto p = pesquisar(cod);

        if (quant <= 0 || preco <= 0) {
            throw new DadosInvalidos("Quantidade ou preço inválidos");
        }

        if (val == null) {
            if (p instanceof ProdutoPerecivel) {
                throw new DadosInvalidos("Produto perecível sem data de validade");
            }
            p.compra(quant, preco);
            registrarMovimentacao(p, "COMPRA", quant, quant * preco);
        } else {
            if (!(p instanceof ProdutoPerecivel)) {
                throw new ProdutoNaoPerecivel("Produto não perecível com data de validade");
            }
            ProdutoPerecivel pp = (ProdutoPerecivel) p;
            if (!val.after(new Date())) {
                throw new DadosInvalidos("Produto vencido");
            }
            pp.compra(quant, preco, val);
            registrarMovimentacao(p, "COMPRA", quant, quant * preco);
        }
    }

    @Override
    public double vender(int cod, int quant) throws ProdutoInexistente, ProdutoVencido, DadosInvalidos {
        Produto p = pesquisar(cod);

        if (quant <= 0) {
            throw new DadosInvalidos("Quantidade inválida");
        }

        if (p instanceof ProdutoPerecivel) {
            ProdutoPerecivel pp = (ProdutoPerecivel) p;
            double valor = pp.vender(quant); // pp.vender já lança DadosInvalidos ou ProdutoVencido
            registrarMovimentacao(p, "VENDA", quant, valor);
            return valor;
        } else {
            if (quant > p.getQuantidadeTotal()) {
                throw new DadosInvalidos("Quantidade maior que o estoque disponível");
            }
            p.setQuantidadeTotal(p.getQuantidadeTotal() - quant);
            double valor = quant * p.getPrecoVenda();
            registrarMovimentacao(p, "VENDA", quant, valor);
            return valor;
        }
    }


    @Override
    public Produto pesquisar(int cod) throws ProdutoInexistente {
        for (Produto p : produtos) {
            if (p.getCodigo() == cod) return p;
        }
        throw new ProdutoInexistente("Produto não encontrado");
    }

    @Override
    public ArrayList<Produto> estoqueAbaixoDoMinimo() {
        ArrayList<Produto> lista = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.getQuantidadeTotal() < p.getMinimoEstoque()) {
                lista.add(p);
            }
        }
        return lista;
    }

    @Override
    public ArrayList<Produto> estoqueVencido() {
        ArrayList<Produto> lista = new ArrayList<>();
        for (Produto p : produtos) {
            if (p instanceof ProdutoPerecivel) {
                ProdutoPerecivel pp = (ProdutoPerecivel) p;
                if (pp.vencido()) {
                    lista.add(pp);
                }
            }
        }
        return lista;
    }

    @Override
    public int quantidadeVencidos(int cod) throws ProdutoInexistente {
        Produto p = pesquisar(cod);
        if (!(p instanceof ProdutoPerecivel)) return 0;
        ProdutoPerecivel pp = (ProdutoPerecivel) p;
        return pp.quantidadeVencida(); // Assumindo que ProdutoPerecivel implementa
    }

    @Override
    public int quantidade(int cod) throws ProdutoInexistente {
        Produto p = pesquisar(cod);
        return p.getQuantidadeTotal();
    }

    // Métodos adicionais (mantidos da implementação original)
    public void adicionarFornecedor(int cod, Fornecedor f) throws ProdutoInexistente {
        Produto p = pesquisar(cod);
        p.addFornecedor(f);
    }

    public double precoDeVenda(int cod) throws ProdutoInexistente {
        Produto p = pesquisar(cod);
        return p.getPrecoVenda();
    }

    public double precoDeCompra(int cod) throws ProdutoInexistente {
        Produto p = pesquisar(cod);
        return p.getPrecoCompra();
    }

    public String movimentacao(int cod, Date inicio, Date fim) throws ProdutoInexistente {
        Produto p = pesquisar(cod);
        StringBuilder sb = new StringBuilder();
        for (Movimentacao m : p.getMovimentacoes()) {
            if (!m.getData().before(inicio) && !m.getData().after(fim)) {
                sb.append(m.toString()).append("\n");
            }
        }
        return sb.toString();
    }

    public ArrayList<Fornecedor> fornecedores(int cod) throws ProdutoInexistente {
        Produto p = pesquisar(cod);
        return p.getFornecedores();
    }

    public List<Produto> getProdutos() {
        return produtos;
    }
}
