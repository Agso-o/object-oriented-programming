package antonioGemesson.estoque;

import java.util.ArrayList;

public class Produto {

    private int quantidadeTotal;
    private ArrayList<Fornecedor> fornecedores = new ArrayList<>();
    private double precoVenda;
    private double precoCompra;
    private ArrayList<Movimentacao> movimentacoes = new ArrayList<>();

    private int codigo;
    private String descricao;
    private int minimoEstoque;
    private double lucro;

    public Produto(int cod, String desc, int min, double lucro){
        this.codigo = cod;
        this.descricao = desc;
        this.minimoEstoque = min;
        this.lucro = lucro;

    }
    public boolean addFornecedor(Fornecedor f){
        for(Fornecedor f1 : fornecedores){
            if(f1.getCnpj() == f.getCnpj()){
                return false;
            }
        }
        fornecedores.add(f);
        return true;
    }

    public ArrayList<Movimentacao> getMovimentacoes() {
        return movimentacoes;
    }

    public void addMovimentacao(Movimentacao m){
        movimentacoes.add(m);
    }
    public void compra(int quant, double val){
        this.precoCompra = (quantidadeTotal * precoCompra + quant * val)/(quantidadeTotal + quant);
        this.quantidadeTotal += quant;

        precoVenda = precoCompra * (1 + lucro);

    }
    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(int quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public ArrayList<Fornecedor> getFornecedores() {
        return fornecedores;
    }

    public void setFornecedores(ArrayList<Fornecedor> fornecedores) {
        this.fornecedores = fornecedores;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(double precoCompra) {
        this.precoCompra = precoCompra;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getMinimoEstoque() {
        return minimoEstoque;
    }

    public void setMinimoEstoque(int minimoEstoque) {
        this.minimoEstoque = minimoEstoque;
    }

    public double getLucro() {
        return lucro;
    }

    public void setLucro(double lucro) {
        this.lucro = lucro;
    }
}
