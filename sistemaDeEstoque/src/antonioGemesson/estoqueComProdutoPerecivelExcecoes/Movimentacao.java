package antonioGemesson.estoqueComProdutoPerecivelExcecoes;

import java.util.Date;

public class Movimentacao {
    private Date data;
    private String tipo;
    private int quantidade;
    private double valor;
    private int estoqueFinal;

    public Movimentacao(Date data, String tipo, int quantidade, double valor, int estoqueFinal) {
        this.data = data;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.valor = valor;
        this.estoqueFinal = estoqueFinal;
    }
    @Override
    public String toString() {
        return String.format("[%s] %s - Quant: %d - Valor: %.2f - Estoque final: %d",
                data.toString(), tipo, quantidade, valor, estoqueFinal);
    }
    public Date getData() {
        return data;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValor() {
        return valor;
    }

    public int getEstoqueFinal() {
        return estoqueFinal;
    }
}
