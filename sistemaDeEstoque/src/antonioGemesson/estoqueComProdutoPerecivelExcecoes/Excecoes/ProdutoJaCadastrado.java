package antonioGemesson.estoqueComProdutoPerecivelExcecoes.Excecoes;

public class ProdutoJaCadastrado extends RuntimeException {
    public ProdutoJaCadastrado(String message) {
        super(message);
    }
}
