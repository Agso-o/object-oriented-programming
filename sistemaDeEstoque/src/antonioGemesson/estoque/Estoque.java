package antonioGemesson.estoque;

import java.util.ArrayList;
import java.util.Date;

public class Estoque {

    private ArrayList<Produto> produtos = new ArrayList<>();

    public Estoque(){}

    private Produto procuraProduto(int cod){
        for (Produto p : produtos){
            if(p.getCodigo() == cod){
                return p;
            }
        }
        return null;
    }

    public void incluir(Produto p){
        if(procuraProduto(p.getCodigo()) == null){
            produtos.add(p);
        }else{
            System.out.println("Produto já cadastrado!");
        }
    }
    public void comprar(int cod, int quant, double preco){
        Produto p = procuraProduto(cod);
        if(p != null && quant > 0 && preco > 0){
            p.compra(quant, preco);
            double valor = quant * preco;

            Movimentacao m = new Movimentacao(new Date(), "COMPRA", quant, valor, p.getQuantidadeTotal());
            p.addMovimentacao(m);
        }else {
            System.out.println("Produto nao encontrado ou dados de compra invalidos");
        }
    }
    public double vender(int cod, int quant){
        Produto p = procuraProduto(cod);
        if(p != null && quant > 0 && quant <= p.getQuantidadeTotal()){
            p.setQuantidadeTotal(p.getQuantidadeTotal() - quant);
            double valor = quant * p.getPrecoVenda();

            Movimentacao m = new Movimentacao(new Date(), "VENDA", quant, valor, p.getQuantidadeTotal());
            p.addMovimentacao(m);
            return valor;
        }else {
            System.out.println("Produto nao encontrado ou dados de venda invalidos");
        }
        return -1;
    }
    public int quantidade(int cod){
        Produto p = procuraProduto(cod);
        if(p!= null){
            return p.getQuantidadeTotal();
        }
        return -1;
    }


    public String movimentacao(int cod, Date inicio, Date fim){
        Produto p = procuraProduto(cod);
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
        Produto p = procuraProduto(cod);
        if(p != null){
            return p.getFornecedores();
        }
        return null;
    }
    public ArrayList<Produto> estoqueAbaixoDoMinimo(){
        ArrayList<Produto> lista = new ArrayList<>();
        for(Produto p : produtos){
            if(p.getQuantidadeTotal() < p.getMinimoEstoque()){
                lista.add(p);
            }
        }
        return lista;
    }
    public void adicionarFornecedor(int cod, Fornecedor f){
        Produto p = procuraProduto(cod);
        if(p !=  null){
            if(!p.addFornecedor(f)){
                System.out.println("Fornecedor já cadastrado no sistema!");
            }else{
                System.out.println("Novo fornecedor cadastrado!");
            }
        }
    }
    public double precoDeVenda(int cod){
        Produto p = procuraProduto(cod);
        if(p != null){
            return p.getPrecoVenda();
        }
        return -1;
    }
    public double precoDeCompra(int cod){
        Produto p = procuraProduto(cod);
        if(p != null){
            return p.getPrecoCompra();
        }
        return -1;
    }
}
