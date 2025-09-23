package antonioGemesson.estoqueComProdutoPerecivel;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import antonioGemesson.estoque.Produto;
import org.junit.Test;

public class TestesEstoqueResumido {
	
	// ----------------------------Pesquisar -> Produto e Produto Perecivel----------------------------------------
	@Test
	public void pesquisarProdutosJaIncluidos() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, 2);
		Produto prod2 = new Produto(13, "Aparelho de Barbear", 5, 1);
		Produto prod3 = new ProdutoPerecivel(14, "Sorvete", 5, 2);
		Produto prod4 = new ProdutoPerecivel(15, "Cerveja", 5, 1);

		estoque.incluir(prod1);
		estoque.incluir(prod2);
		estoque.incluir(prod3);
		estoque.incluir(prod4);
		
		Produto outro1 = estoque.pesquisar(12);
		assertEquals("Shampoo", outro1.getDescricao());
		
		Produto outro2 = estoque.pesquisar(13);
		assertEquals("Aparelho de Barbear", outro2.getDescricao());
		
		Produto outro3 = estoque.pesquisar(14);
		assertEquals("Sorvete", outro3.getDescricao());
		
		Produto outro4 = estoque.pesquisar(15);
		assertEquals("Cerveja", outro4.getDescricao());
		
		assertNull(estoque.pesquisar(16));
	}
	
	@Test
	public void incluirProdutoComDadosInvalidos() {
		Estoque estoque = new Estoque();
		
		Produto prod1 = new Produto(-12, "Shampoo", 5, 2);
		assertFalse(estoque.incluir(prod1));
		prod1 = new Produto(12, "", 5, 2);
		assertFalse(estoque.incluir(prod1));
		prod1 = new Produto(12, "Shampoo", -5, 2);
		assertFalse(estoque.incluir(prod1));
		prod1 = new Produto(12, "Shampoo", 5, -2);
		assertFalse(estoque.incluir(prod1));
		prod1 = new Produto(12, "Shampoo", 5, 2);
		assertTrue(estoque.incluir(prod1));
		assertFalse(estoque.incluir(prod1));

	}
	
	@Test
	public void incluirProdutoNulo() {
		Estoque estoque = new Estoque();
		assertFalse(estoque.incluir(null));
	}

	
	@Test
	public void incluirProdutoComCodigoNegativo() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(-12, "Shampoo", 5, 2);
		Produto prod2 = new ProdutoPerecivel(-14, "Sorvete", 5, 2);

		assertFalse(estoque.incluir(prod1));
		assertFalse(estoque.incluir(prod2));
		assertNull(estoque.pesquisar(-12));
		assertNull(estoque.pesquisar(-14));
	}
	
	@Test
	public void incluirProdutoComDescricaoVazia() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "", 5, 2);
		Produto prod2 = new ProdutoPerecivel(14, "", 5, 2);

		assertFalse(estoque.incluir(prod1));
		assertFalse(estoque.incluir(prod2));
		assertNull(estoque.pesquisar(12));
		assertNull(estoque.pesquisar(14));
	}
	

	@Test
	public void incluirProdutoComEstoqueMinimoZero() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 0, 2);
		Produto prod2 = new ProdutoPerecivel(14, "Sorvete", 0, 2);

		assertFalse(estoque.incluir(prod1));
		assertFalse(estoque.incluir(prod2));
		assertNull(estoque.pesquisar(12));
		assertNull(estoque.pesquisar(14));
	}
	
	@Test
	public void incluirProdutoComLucroNegativo() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, -2);
		Produto prod2 = new ProdutoPerecivel(14, "Sorvete", 5, -2);

		assertFalse(estoque.incluir(prod1));
		assertFalse(estoque.incluir(prod2));
		assertNull(estoque.pesquisar(12));
		assertNull(estoque.pesquisar(14));
	}

	
	// ------------------------------Comprar----------------------------------------
	@Test
	public void compraProdutoNaoExistente() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, 2);
		Produto prod2 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Date data = new Date();
		
		assertFalse(estoque.comprar(prod1.getCodigo(), 10, 5, null));
		assertFalse(estoque.comprar(prod2.getCodigo(), 5, 2, data));
	}
	
	@Test
	public void compraQuantidadeZeroDeProdutos() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, 2);
		Produto prod2 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Date data = new Date();

		estoque.incluir(prod1);
		estoque.incluir(prod2);
		assertFalse(estoque.comprar(prod1.getCodigo(), 0, 5, null));
		assertFalse(estoque.comprar(prod2.getCodigo(), 0, 2, data));
	}
	
	// ------------------------------Comprar -> Produto Nao Perecivel----------------------------------------
	@Test
	public void compraProdutoNaoPerecivel() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, 2);
		Produto prod2 = new Produto(13, "Aparelho de Barbear", 5, 1);
		
		estoque.incluir(prod1);
		estoque.incluir(prod2);
		assertTrue(estoque.comprar(12, 32, 1, null));
		assertEquals(32, estoque.quantidade(12));
		assertTrue(estoque.comprar(12, 10, 5, null));
		assertEquals(42, estoque.quantidade(12));
		assertTrue(estoque.comprar(13, 50, 20, null));
		assertEquals(50, estoque.quantidade(13));
		assertTrue(estoque.comprar(13, 5, 2.5, null));
		assertEquals(55, estoque.quantidade(13));
	}
	
	@Test
	public void compraProdutoNaoPerecivelComData() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, 2);
		Produto prod2 = new Produto(13, "Aparelho de Barbear", 5, 1);
		Date data = new Date();
		data.setTime(data.getTime() + 120000);
		
		estoque.incluir(prod1);
		estoque.incluir(prod2);
		assertFalse(estoque.comprar(12, 10, 5, data));
		assertEquals(0, estoque.quantidade(12));
		assertFalse(estoque.comprar(13, 5, 2.5, data));
		assertEquals(0, estoque.quantidade(13));
	}

	// ------------------------------Comprar -> Produto Perecivel----------------------------------------
	@Test
	public void compraProdutoPerecivel() {
		Estoque estoque = new Estoque();
		Produto prod1 = new ProdutoPerecivel(14, "Sorvete", 5, 2);
		Produto prod2 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Date data = new Date();
		data.setTime(data.getTime() + 120000);

		estoque.incluir(prod1);
		estoque.incluir(prod2);
		assertTrue(estoque.comprar(14, 24, 8, data));
		assertTrue(estoque.comprar(14, 10, 5, data));
		assertEquals(34, estoque.quantidade(14));
		assertTrue(estoque.comprar(15, 11, 4.23, data));
		assertTrue(estoque.comprar(15, 5, 2.5, data));
		assertEquals(16, estoque.quantidade(15));

	}
	
	@Test
	public void compraProdutoPerecivelComValidadeMenorQueDataAtual() {
		Estoque estoque = new Estoque();
		Produto prod1 = new ProdutoPerecivel(14, "Sorvete", 5, 2);
		Produto prod2 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Date data = new Date();
		data.setTime(data.getTime() - 120000);

		estoque.incluir(prod1);
		estoque.incluir(prod2);
		assertFalse(estoque.comprar(14, 24, 8, data));
		assertEquals(0, estoque.quantidade(14));
		assertFalse(estoque.comprar(15, 11, 4.23, data));
		assertEquals(0, estoque.quantidade(15));
	}

	@Test
	public void compraProdutoPerecivelComDataNull() {
		Estoque estoque = new Estoque();
		Produto prod1 = new ProdutoPerecivel(14, "Sorvete", 5, 2);
		Produto prod2 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Date data = new Date();

		estoque.incluir(prod1);
		estoque.incluir(prod2);
		assertFalse(estoque.comprar(14, 24, 8, null));
		assertEquals(0, estoque.quantidade(14));
		assertFalse(estoque.comprar(15, 11, 4.23, null));
		assertEquals(0, estoque.quantidade(15));
	}
	
	// ------------------------------Vender----------------------------------------
	@Test
	public void vendaProduto() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, 2);
		Produto prod2 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Date data = new Date();
		data.setTime(data.getTime() + 120000);
		
		estoque.incluir(prod1);
		estoque.incluir(prod2);
		estoque.comprar(12, 10, 5, null);
		estoque.comprar(12, 10, 2.5, null);
		estoque.comprar(12, 10, 7.5, null);
		estoque.comprar(15, 10, 2.5, data);
		estoque.comprar(15, 10, 5, data);
		estoque.comprar(15, 10, 7.5, data);
		assertEquals(3*15, estoque.vender(12, 3), 0);
		assertEquals(3*10, estoque.vender(15, 3), 0);
	}
	
	@Test
	public void vendaQuantidadeMaiorQueEstoque() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, 2);
		Produto prod2 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Date data = new Date();
		data.setTime(data.getTime() + 120000);
		
		estoque.incluir(prod1);
		estoque.incluir(prod2);
		estoque.comprar(12, 10, 5, null);
		estoque.comprar(15, 5, 2.5, data);
		estoque.vender(12, 5);
		assertEquals(5, estoque.quantidade(12));
		estoque.vender(15, 2);
		assertEquals(3, estoque.quantidade(15));
		assertEquals(-1, estoque.vender(12, 12), 0.001);
		assertEquals(-1, estoque.vender(15, 6), 0.001);
		assertEquals(5, estoque.quantidade(12));
		assertEquals(3, estoque.quantidade(15));
	}
	
	// ------------------------------Vender -> Produto----------------------------------------
	@Test
	public void vendaProdutoNaoPerecivel() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, 2);
		Produto prod2 = new Produto(13, "Aparelho de Barbear", 5, 1);
		
		estoque.incluir(prod1);
		estoque.incluir(prod2);
		estoque.comprar(12, 10, 5, null);
		estoque.comprar(12, 10, 5, null);
		estoque.comprar(13, 5, 2.5, null);
		estoque.comprar(13, 5, 2.5, null);
		assertEquals(3*15, estoque.vender(12, 3), 0.001);
		assertEquals(3*5, estoque.vender(13, 3), 0.001);
	}
	
	// ------------------------------Vender -> Produto Perecivel----------------------------------------
	@Test
	public void vendaProdutoPerecivel() {
		Estoque estoque = new Estoque();
		Produto prod1 = new ProdutoPerecivel(14, "Sorvete", 5, 2);
		Produto prod2 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Date data1 = new Date();
		Date data2 = new Date();
		Date data3 = new Date();
		data2.setTime(data1.getTime() + 120000);
		data3.setTime(data1.getTime() + 240000);
		
		estoque.incluir(prod1);
		estoque.incluir(prod2);
		estoque.comprar(prod1.getCodigo(), 10, 5, data1);
		estoque.comprar(prod1.getCodigo(), 10, 5, data2);
		estoque.comprar(prod1.getCodigo(), 10, 5, data3);
		estoque.comprar(prod2.getCodigo(), 5, 2.5, data1);
		estoque.comprar(prod2.getCodigo(), 5, 2.5, data2);
		estoque.comprar(prod2.getCodigo(), 10, 2.5, data3);
		assertEquals(3*15, estoque.vender(14, 3), 0.001);
		assertEquals(3*5, estoque.vender(15, 3), 0.001);
	}
	
	@Test
	public void vendaProdutoPerecivelVencido() {
		Estoque estoque = new Estoque();
		Produto prod1 = new ProdutoPerecivel(14, "Sorvete", 5, 2);
		Produto prod2 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Date data = new Date();
		data.setTime(data.getTime() + 120000);
		
		estoque.incluir(prod1);
		estoque.incluir(prod2);
		estoque.comprar(prod1.getCodigo(), 10, 5, data);
		estoque.comprar(prod2.getCodigo(), 5, 2.5, data);
		data.setTime(data.getTime() - 120000);
		assertEquals(-1, estoque.vender(14, 3), 0);
		assertEquals(-1, estoque.vender(15, 3), 0);
	}
	
	// ------------------------------Estoque abaixo do minimo----------------------------------------
	@Test
	public void produtosAbaixoDoEstoqueMinimo() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, 2);
		Produto prod2 = new Produto(13, "Aparelho de Barbear", 5, 1);
		Produto prod3 = new ProdutoPerecivel(14, "Sorvete", 5, 2);
		Produto prod4 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Date data = new Date();
		data.setTime(data.getTime() + 120000);
		
		estoque.incluir(prod1);
		estoque.incluir(prod2);
		estoque.incluir(prod3);
		estoque.incluir(prod4);
		estoque.comprar(prod1.getCodigo(), 10, 5, null);
		estoque.comprar(prod2.getCodigo(), 10, 2.5, null);
		estoque.comprar(prod3.getCodigo(), 10, 5, data);
		estoque.comprar(prod4.getCodigo(), 20, 2.5, data);
		estoque.vender(12, 9);
		estoque.vender(13, 1);
		estoque.vender(14, 2);
		estoque.vender(15, 18);
		ArrayList<Produto> abaixoMinActual = estoque.estoqueAbaixoDoMinimo();
		assertEquals(2, abaixoMinActual.size());
		for (Produto produto : abaixoMinActual) {
			assertTrue(produto == prod1 || produto == prod4);
		}
	}
	// ------------------------------Estoque Vencido----------------------------------------
	@Test
	public void produtosComLotesVencidos() {
		Estoque estoque = new Estoque();
		Produto prod1 = new Produto(12, "Shampoo", 5, 2);
		Produto prod2 = new Produto(13, "Aparelho de Barbear", 5, 1);
		Produto prod3 = new ProdutoPerecivel(14, "Sorvete", 5, 2);
		Produto prod4 = new ProdutoPerecivel(15, "Cerveja", 5, 1);
		Produto prod5 = new ProdutoPerecivel(16, "Cerveja Pilsen", 5, 1);
		Date data1 = new Date();
		data1.setTime(data1.getTime() + 120000);
		Date data2 = new Date();
		data2.setTime(data1.getTime());
		
		estoque.incluir(prod1);
		estoque.incluir(prod2);
		estoque.incluir(prod3);
		estoque.incluir(prod4);
		estoque.incluir(prod5);
		estoque.comprar(prod1.getCodigo(), 10, 5, null);
		estoque.comprar(prod2.getCodigo(), 30, 2.5, null);
		estoque.comprar(prod3.getCodigo(), 10, 5, data1);
		estoque.comprar(prod3.getCodigo(), 20, 5, data2);
		estoque.comprar(prod4.getCodigo(), 5, 2.5, data2);
		estoque.comprar(prod4.getCodigo(), 5, 2.5, data2);
		estoque.comprar(prod5.getCodigo(), 5, 2.5, data1);
		estoque.comprar(prod5.getCodigo(), 5, 2.5, data1);
		data1.setTime(data1.getTime() - 120000);
		ArrayList<Produto> produtosVencidos = estoque.estoqueVencido();
		assertEquals(2, produtosVencidos.size());
		for (Produto produto : produtosVencidos) {
			assertTrue(produto == prod3 || produto == prod5);
		}
	}
	
}
