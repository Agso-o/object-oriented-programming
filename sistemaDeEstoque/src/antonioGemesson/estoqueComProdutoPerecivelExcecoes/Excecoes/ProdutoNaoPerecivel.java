package antonioGemesson.estoqueComProdutoPerecivelExcecoes.Excecoes;

public class ProdutoNaoPerecivel extends RuntimeException {
    public ProdutoNaoPerecivel(String message) {
        super(message);
    }
}
