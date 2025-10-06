package antonioGemesson.estoqueComProdutoPerecivelExcecoes.Excecoes;

public class ProdutoVencido extends RuntimeException {
    public ProdutoVencido(String message) {
        super(message);
    }
}
