package antonioGemesson.estoqueComProdutoPerecivel;

import antonioGemesson.estoque.Produto;

import java.util.ArrayList;
import java.util.Date;

public class ProdutoPerecivel extends Produto {
    private ArrayList<Lote> lotes = new ArrayList<>();

    public ProdutoPerecivel(int cod, String desc, int min, double lucro) {
        super(cod, desc, min, lucro);
    }

    public void compra(int quant, double preco, Date validade) {
        super.compra(quant, preco);
        lotes.add(new Lote(quant, validade));

    }
    public int quantidadeNaoVencida() {
        int total = 0;
        for (Lote lote : lotes) {
            if (!lote.estaVencido()) {
                total += lote.getQuantidade();
            }
        }
        return total;
    }

    public ArrayList<Lote> getLotes() {
        return lotes;
    }
    public boolean vencido(){
        for(Lote t: lotes){
            if(t.estaVencido()){
                return true;
            }
        }
        return false;
    }

    public double vender(int quant){
        int disponivel = 0;
        for (Lote lote : lotes) {
            if (!lote.estaVencido()) {
                disponivel += lote.getQuantidade();
            }
        }

        if (disponivel < quant) {
            return -1;
        }

        int restante = quant;
        double total = 0;

        lotes.sort((l1, l2) -> {
            if (l1.getValidade() == null) return 1;
            if (l2.getValidade() == null) return -1;
            return l1.getValidade().compareTo(l2.getValidade());
        });

        for (Lote lote : lotes) {
            if (lote.estaVencido()) continue;

            if (lote.getQuantidade() >= restante) {
                lote.setQuant(lote.getQuantidade() - restante);
                total += restante * getPrecoVenda();
                restante = 0;
                break;
            } else {
                total += lote.getQuantidade() * getPrecoVenda();
                restante -= lote.getQuantidade();
                lote.setQuant(0);
            }
        }

        super.setQuantidadeTotal(super.getQuantidadeTotal() - quant);

        return total;
    }

}

