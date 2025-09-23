package antonioGemesson.estoqueComProdutoPerecivel;

import java.util.Date;

public class Lote {
    private int quant;
    private Date validade;

    public Lote(int quant, Date validade){
        this.quant = quant;
        this.validade = validade;

    }

    public int getQuantidade() {
        return this.quant;
    }

    public Date getValidade() {
        return validade;
    }

    public boolean estaVencido() {
        if (validade == null) return false;
        Date hoje = new Date();
        return !validade.after(hoje);
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }
}
