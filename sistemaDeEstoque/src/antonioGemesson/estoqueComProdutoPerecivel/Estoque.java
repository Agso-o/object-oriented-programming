package antonioGemesson.estoqueComProdutoPerecivel;

import antonioGemesson.estoque.Fornecedor;
import antonioGemesson.estoque.Movimentacao;
import antonioGemesson.estoque.Produto;

import java.util.ArrayList;
import java.util.Date;

public class Estoque implements InterfaceEstoque {
    private ArrayList<Produto> produtos = new ArrayList<>();

    @Override
    public boolean incluir(Produto p) {
        if(p == null) return false;
        if(pesquisar(p.getCodigo()) == null) {
            if(p.getCodigo() < 0 || p.getLucro() < 0 || p.getMinimoEstoque() <= 0 || p.getDescricao() == ""){
                return false;
            }
            produtos.add(p);
            return true;
        }
        return false;
    }

    @Override
    public boolean comprar(int cod, int quant, double preco, Date val) {
        Produto p = pesquisar(cod);

        if (p == null || quant <= 0 || preco <= 0) {
            return false;
        }

        if (val == null) {
            if (p instanceof ProdutoPerecivel) {
                return false;
            }
            p.compra(quant, preco);

            double valor = quant * preco;
            Movimentacao m = new Movimentacao(new Date(), "COMPRA", quant, valor, p.getQuantidadeTotal());
            p.addMovimentacao(m);

            return true;
        } else {
            if (!(p instanceof ProdutoPerecivel)) {
                return false;
            }

            ProdutoPerecivel p1 = (ProdutoPerecivel) p;
            Date hoje = new Date();
            if(!val.after(hoje)){
                return false;
            }
            p1.compra(quant, preco, val);

            double valor = quant * preco;
            Movimentacao m = new Movimentacao(new Date(), "COMPRA", quant, valor, p.getQuantidadeTotal());
            p.addMovimentacao(m);

            return true;
        }
    }


    @Override
    public double vender(int cod, int quant) {
        Produto p = pesquisar(cod);

        if(p == null || quant <= 0 || quant > p.getQuantidadeTotal()){
            return -1;
        }
        if(p instanceof ProdutoPerecivel){
            ProdutoPerecivel p1 = (ProdutoPerecivel) p;
            double valor = p1.vender(quant);
            Movimentacao m = new Movimentacao(new Date(), "VENDA", quant, valor, p.getQuantidadeTotal());
            p.addMovimentacao(m);
            return valor;

        } else{

            p.setQuantidadeTotal(p.getQuantidadeTotal() - quant);
            double valor = quant * p.getPrecoVenda();

            Movimentacao m = new Movimentacao(new Date(), "VENDA", quant, valor, p.getQuantidadeTotal());
            p.addMovimentacao(m);
            return valor;
        }
    }

    @Override
    public Produto pesquisar(int cod) {
        for (Produto p : produtos) {
            if (p.getCodigo() == cod) {
                return p;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Produto> estoqueAbaixoDoMinimo() {
        ArrayList<Produto> lista = new ArrayList<>();
        for(Produto p : produtos){
            if(p.getQuantidadeTotal() < p.getMinimoEstoque()){
                lista.add(p);
            }
        }
        return lista;
    }

    @Override
    public ArrayList<Produto> estoqueVencido() {
        ArrayList<Produto> lista = new ArrayList<>();
        for (Produto p: produtos){
            if(p instanceof ProdutoPerecivel){
                ProdutoPerecivel p1 = (ProdutoPerecivel) p;
                if(p1.vencido()){
                    lista.add(p1);
                }
            }
        }
        return lista;
    }

    @Override
    public int quantidadeVencidos(int cod) {
        int totalVencidos = 0;
        for(Produto p : produtos){
            if(p instanceof ProdutoPerecivel){
                ProdutoPerecivel p1 = (ProdutoPerecivel) p;
                totalVencidos += p.getQuantidadeTotal() - p1.getQuantidadeTotal();
            }
        }
        return totalVencidos;
    }

    @Override
    public void adicionarFornecedor(int cod, Fornecedor f) {
        Produto p = pesquisar(cod);
        if(p !=  null){
            if(!p.addFornecedor(f)){
                System.out.println("Fornecedor já cadastrado no sistema!");
            }else{
                System.out.println("Novo fornecedor cadastrado!");
            }
        }

    }

    @Override
    public double precoDeVenda(int cod) {
        Produto p = pesquisar(cod);
        if(p != null){
            return p.getPrecoVenda();
        }
        return -1;
    }

    @Override
    public double precoDeCompra(int cod) {
        Produto p = pesquisar(cod);
        if(p != null){
            return p.getPrecoCompra();
        }
        return -1;
    }

    public int quantidade(int cod) {
        Produto p = pesquisar(cod);
        if(p!= null){
            return p.getQuantidadeTotal();
        }
        return -1;
    }
    public String movimentacao(int cod, Date inicio, Date fim){
        Produto p = pesquisar(cod);
        if(p != null){
            StringBuilder movimentacoes = new StringBuilder();
            for(Movimentacao m : p.getMovimentacoes()){
                if(!m.getData().before(inicio) && !m.getData().after(fim)){
                    movimentacoes.append(m.toString()).append("\n");
                }
            }
            return movimentacoes.toString();
        }else{
            System.out.println("Produto nao encontrado");
        }
        return "";
    }

    public ArrayList<Fornecedor> fornecedores(int cod){
        Produto p = pesquisar(cod);
        if(p != null){
            return p.getFornecedores();
        }
        return null;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }


}
