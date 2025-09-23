package antonioGemesson.estoqueComProdutoPerecivel;

import antonioGemesson.estoque.Fornecedor;
import antonioGemesson.estoque.Produto;

import java.util.ArrayList;
import java.util.Date;

public interface InterfaceEstoque {

    // Retorna false se ouver algum campo numerico invalido ou texto vazio ou null ou produto ja cadastrado.
    public boolean incluir(Produto p);

    // Retorna false se o produto nao existir, se houver valores numericos invalidos ou se enviar data e o produto comprado nao for perecivel
    public boolean comprar(int cod, int quant, double preco, Date val);

    // Retorna -1 se o produto nao existir, se for vencido ou se houver um dado invalido em numeros.
    public double vender(int cod, int quant);

    // Retorna null se o produto nao existir
    public Produto pesquisar (int cod);

    // Retorna lista de produtos abaixo do minimo. Lista vazia se nao tiver nenhum.
    public ArrayList<Produto> estoqueAbaixoDoMinimo();

    // Retorna lista de produtos vencidos. Lista vazia se nao tiver nenhum.
    public ArrayList<Produto> estoqueVencido();

    // Retorna 0 se nao houver nenhum produto vencido ou a quantidade de produtos vencidos de um determinado codigo.
    public int quantidadeVencidos(int cod);

    // Adiciona um fornecedor à lista de fornecedores do produto
    public void adicionarFornecedor(int cod, Fornecedor f);

    // Retorna o preço de venda de um produto
    public double precoDeVenda(int cod);

    // Retorna o preço de compra de um produto
    public double precoDeCompra(int cod);

}
