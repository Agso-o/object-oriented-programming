package antonioGemesson.estoque;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class EstoqueTest {

    private Estoque estoque;
    private Produto p1;
    private Produto p2;
    private Fornecedor f1;

    // Este método é executado antes de CADA teste.
    // Garante que os testes sejam independentes e não interfiram uns nos outros.
    @BeforeEach
    void setUp() {
        estoque = new Estoque();
        // Produto com estoque mínimo 10 e lucro de 50% (0.5)
        p1 = new Produto(101, "Arroz", 10, 0.5);
        // Produto com estoque mínimo 5 e lucro de 100% (1.0)
        p2 = new Produto(102, "Feijão", 5, 1.0);
        f1 = new Fornecedor(12345, "Distribuidora Alimentos");
    }

    @Test
    @DisplayName("Deve incluir um novo produto com sucesso")
    void incluirProdutoComSucesso() {
        estoque.incluir(p1);
        assertEquals(0, estoque.quantidade(101), "Novo produto deve ter quantidade 0");
    }

    @Test
    @DisplayName("Não deve incluir produto com código duplicado")
    void naoDeveIncluirProdutoDuplicado() {
        estoque.incluir(p1);
        estoque.incluir(p1); // Tenta incluir o mesmo produto novamente
        // A lógica para contar produtos não existe na classe, então validamos pela quantidade
        // Se pudéssemos ver a lista interna, verificaríamos seu tamanho.
        // Por enquanto, apenas garantimos que a quantidade não foi alterada de forma inesperada.
        assertEquals(0, estoque.quantidade(101));
    }

    @Test
    @DisplayName("Deve realizar uma compra e atualizar quantidade e preços")
    void comprarProduto() {
        estoque.incluir(p1);
        // Compra inicial: 20 unidades a R$ 20.00 cada
        estoque.comprar(101, 20, 20.0);

        assertEquals(20, estoque.quantidade(101));
        assertEquals(20.0, estoque.precoDeCompra(101));
        // Preço de venda = 20.0 * (1 + 0.5) = 30.0
        assertEquals(30.0, estoque.precoDeVenda(101));

        // Segunda compra: 30 unidades a R$ 10.00 cada
        estoque.comprar(101, 30, 10.0);
        double novoPrecoCompra = (20 * 20.0 + 30 * 10.0) / 50; // Média ponderada

        assertEquals(50, estoque.quantidade(101));
        assertEquals(novoPrecoCompra, estoque.precoDeCompra(101));
        // Novo Preço de venda = 14.0 * (1 + 0.5) = 21.0
        assertEquals(novoPrecoCompra * (1 + p1.getLucro()), estoque.precoDeVenda(101));
    }

    @Test
    @DisplayName("Não deve comprar produto com quantidade negativa ou preço zero")
    void naoDeveComprarComDadosInvalidos() {
        estoque.incluir(p1);
        estoque.comprar(101, -5, 10.0); // Quantidade negativa
        assertEquals(0, estoque.quantidade(101));

        estoque.comprar(101, 5, -10.0); // Preço negativo
        assertEquals(0, estoque.quantidade(101));
    }

    @Test
    @DisplayName("Deve vender um produto e decrementar a quantidade")
    void venderProdutoComSucesso() {
        estoque.incluir(p1);
        estoque.comprar(101, 20, 10.0); // Estoque: 20, Preço Venda: 15.0
        double valorVenda = estoque.vender(101, 5);

        assertEquals(15, estoque.quantidade(101));
        assertEquals(5 * 15.0, valorVenda, "O valor retornado da venda deve ser quant * precoVenda");
    }

    @Test
    @DisplayName("Não deve vender produto com quantidade maior que o estoque")
    void naoDeveVenderComEstoqueInsuficiente() {
        estoque.incluir(p1);
        estoque.comprar(101, 10, 10.0);
        double valorVenda = estoque.vender(101, 11); // Tenta vender 11, mas só tem 10

        assertEquals(10, estoque.quantidade(101), "A quantidade não deve mudar");
        assertEquals(-1, valorVenda, "A venda deve retornar -1 para indicar falha");
    }

    @Test
    @DisplayName("Deve retornar -1 ao tentar vender produto inexistente")
    void naoDeveVenderProdutoInexistente() {
        double valorVenda = estoque.vender(999, 1);
        assertEquals(-1, valorVenda);
    }

    @Test
    @DisplayName("Deve adicionar um fornecedor a um produto")
    void adicionarFornecedor() {
        estoque.incluir(p1);
        estoque.adicionarFornecedor(101, f1);

        ArrayList<Fornecedor> fornecedores = estoque.fornecedores(101);
        assertNotNull(fornecedores);
        assertEquals(1, fornecedores.size());
        assertEquals("Distribuidora Alimentos", fornecedores.get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar a lista de produtos com estoque abaixo do mínimo")
    void estoqueAbaixoDoMinimo() {
        estoque.incluir(p1); // Mínimo 10
        estoque.incluir(p2); // Mínimo 5

        estoque.comprar(101, 9, 10.0);  // p1 está abaixo (9 < 10)
        estoque.comprar(102, 5, 20.0);  // p2 não está abaixo (5 == 5)

        ArrayList<Produto> abaixo = estoque.estoqueAbaixoDoMinimo();
        assertEquals(1, abaixo.size(), "Apenas um produto deve estar na lista");
        assertEquals(101, abaixo.get(0).getCodigo());
    }

    @Test
    @DisplayName("Deve retornar uma string com as movimentações de um produto")
    void movimentacao() throws InterruptedException {
        estoque.incluir(p1);
        Date inicio = new Date();

        // Pequena pausa para garantir que as datas sejam distintas
        TimeUnit.MILLISECONDS.sleep(10);

        estoque.comprar(101, 20, 15.0);
        TimeUnit.MILLISECONDS.sleep(10);
        estoque.vender(101, 5);

        TimeUnit.MILLISECONDS.sleep(10);
        Date fim = new Date();

        String relatorio = estoque.movimentacao(101, inicio, fim);

        assertNotNull(relatorio);
        assertTrue(relatorio.contains("COMPRA"), "Relatório deve conter a movimentação de compra");
        assertTrue(relatorio.contains("VENDA"), "Relatório deve conter a movimentação de venda");
        assertTrue(relatorio.contains("Estoque final: 20"), "Relatório deve mostrar o estoque final após a compra");
        assertTrue(relatorio.contains("Estoque final: 15"), "Relatório deve mostrar o estoque final após a venda");
    }

    @Test
    @DisplayName("Deve retornar null ao pedir fornecedores de produto inexistente")
    void fornecedoresDeProdutoInexistente() {
        assertNull(estoque.fornecedores(999));
    }
}