package antonioGemesson.estoqueComProdutoPerecivelExcecoes.Excecoes;

public class ProdutoInexistente extends RuntimeException {
    public ProdutoInexistente(String message) {
        super(message);
    }
}
