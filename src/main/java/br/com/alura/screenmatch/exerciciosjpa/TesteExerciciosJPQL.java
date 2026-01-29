package br.com.alura.screenmatch.exerciciosjpa;

import br.com.alura.screenmatch.exerciciosjpa.model.Categoria;
import br.com.alura.screenmatch.exerciciosjpa.model.Pedido;
import br.com.alura.screenmatch.exerciciosjpa.model.Produto;
import br.com.alura.screenmatch.exerciciosjpa.repository.CategoriaRepository;
import br.com.alura.screenmatch.exerciciosjpa.repository.PedidoRepository;
import br.com.alura.screenmatch.exerciciosjpa.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe para testar os 11 exercícios JPQL avançados
 * Exercícios da Aula 03 - Consultas JPQL Personalizadas
 */
@Component
public class TesteExerciciosJPQL {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public void executarTodos() {
        System.out.println("\n========================================");
        System.out.println("EXERCÍCIOS JPQL - AULA 03");
        System.out.println("========================================\n");

        exercicio1();
        exercicio2();
        exercicio3();
        exercicio4();
        exercicio5();
        exercicio6();
        exercicio7();
        exercicio8();
        exercicio9();
        exercicio10();
        exercicio11();
    }

    /**
     * EXERCÍCIO 1: Produtos com preço maior que um valor
     * JPQL: SELECT p FROM Produto p WHERE p.preco > :preco
     */
    private void exercicio1() {
        System.out.println("1️⃣  EXERCÍCIO 1: Produtos com preço > 1000");
        List<Produto> produtos = produtoRepository.findByPrecoMaiorQue(1000.0);
        produtos.forEach(p -> System.out.println("   - " + p.getNome() + ": R$ " + p.getPreco()));
        System.out.println();
    }

    /**
     * EXERCÍCIO 2: Produtos ordenados por preço crescente
     * JPQL: SELECT p FROM Produto p ORDER BY p.preco ASC
     */
    private void exercicio2() {
        System.out.println("2️⃣  EXERCÍCIO 2: Produtos ordenados por preço crescente");
        List<Produto> produtos = produtoRepository.findAllOrderByPrecoAsc();
        produtos.forEach(p -> System.out.println("   - " + p.getNome() + ": R$ " + p.getPreco()));
        System.out.println();
    }

    /**
     * EXERCÍCIO 3: Produtos ordenados por preço decrescente
     * JPQL: SELECT p FROM Produto p ORDER BY p.preco DESC
     */
    private void exercicio3() {
        System.out.println("3️⃣  EXERCÍCIO 3: Produtos ordenados por preço decrescente");
        List<Produto> produtos = produtoRepository.findAllOrderByPrecoDesc();
        produtos.forEach(p -> System.out.println("   - " + p.getNome() + ": R$ " + p.getPreco()));
        System.out.println();
    }

    /**
     * EXERCÍCIO 4: Produtos que começam com uma letra específica
     * JPQL: SELECT p FROM Produto p WHERE p.nome LIKE :letra%
     */
    private void exercicio4() {
        System.out.println("4️⃣  EXERCÍCIO 4: Produtos que começam com 'N'");
        List<Produto> produtos = produtoRepository.findByNomeStartingWith("N");
        produtos.forEach(p -> System.out.println("   - " + p.getNome()));
        System.out.println();
    }

    /**
     * EXERCÍCIO 5: Pedidos feitos entre duas datas
     * JPQL: SELECT p FROM Pedido p WHERE p.data BETWEEN :dataInicio AND :dataFim
     */
    private void exercicio5() {
        System.out.println("5️⃣  EXERCÍCIO 5: Pedidos entre 2024-01-01 e 2024-12-31");
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);
        LocalDate dataFim = LocalDate.of(2024, 12, 31);
        List<Pedido> pedidos = pedidoRepository.findByDataBetween(dataInicio, dataFim);
        pedidos.forEach(p -> System.out.println("   - Pedido #" + p.getId() + " em " + p.getData()));
        System.out.println();
    }

    /**
     * EXERCÍCIO 6: Média de preços dos produtos
     * JPQL: SELECT AVG(p.preco) FROM Produto p
     */
    private void exercicio6() {
        System.out.println("6️⃣  EXERCÍCIO 6: Média de preços dos produtos");
        Double media = produtoRepository.calcularMediaPrecos();
        System.out.println("   Média: R$ " + String.format("%.2f", media));
        System.out.println();
    }

    /**
     * EXERCÍCIO 7: Preço máximo de um produto em uma categoria
     * JPQL: SELECT MAX(p.preco) FROM Produto p WHERE p.categoria.nome = :categoria
     */
    private void exercicio7() {
        System.out.println("7️⃣  EXERCÍCIO 7: Preço máximo na categoria 'Eletrônicos'");
        Double precoMax = produtoRepository.findPrecoMaximoByCategoria("Eletrônicos");
        System.out.println("   Preço máximo: R$ " + precoMax);
        System.out.println();
    }

    /**
     * EXERCÍCIO 8: Contar número de produtos por categoria
     * JPQL: SELECT c.nome, COUNT(p) FROM Categoria c JOIN c.produtos p GROUP BY c.nome
     */
    private void exercicio8() {
        System.out.println("8️⃣  EXERCÍCIO 8: Número de produtos por categoria");
        List<Object[]> resultado = categoriaRepository.contarProdutosPorCategoria();
        resultado.forEach(r -> System.out.println("   - " + r[0] + ": " + r[1] + " produtos"));
        System.out.println();
    }

    /**
     * EXERCÍCIO 9: Filtrar categorias com mais de 10 produtos
     * JPQL: SELECT c FROM Categoria c WHERE SIZE(c.produtos) > 10
     */
    private void exercicio9() {
        System.out.println("9️⃣  EXERCÍCIO 9: Categorias com mais de 10 produtos");
        List<Categoria> categorias = categoriaRepository.findCategoriasComMaisDe10Produtos();
        categorias.forEach(c -> System.out.println("   - " + c.getNome() + ": " + c.getProdutos().size() + " produtos"));
        System.out.println();
    }

    /**
     * EXERCÍCIO 10: Produtos filtrados por nome OU categoria
     * JPQL: SELECT p FROM Produto p WHERE p.nome LIKE %:termo% OR p.categoria.nome = :categoria
     */
    private void exercicio10() {
        System.out.println("🔟 EXERCÍCIO 10: Produtos com 'Note' no nome OU categoria 'Eletrônicos'");
        List<Produto> produtos = produtoRepository.findByNomeOrCategoria("Note", "Eletrônicos");
        produtos.forEach(p -> System.out.println("   - " + p.getNome() + " (" + p.getCategoria().getNome() + ")"));
        System.out.println();
    }

    /**
     * EXERCÍCIO 11: Query nativa para buscar os 5 produtos mais caros
     * SQL NATIVO: SELECT * FROM produtos ORDER BY preco DESC LIMIT 5
     */
    private void exercicio11() {
        System.out.println("1️⃣1️⃣  EXERCÍCIO 11: Top 5 produtos mais caros (Query Nativa)");
        List<Produto> produtos = produtoRepository.findTop5MaisCarosNativo();
        produtos.forEach(p -> System.out.println("   - " + p.getNome() + ": R$ " + p.getPreco()));
        System.out.println();
    }
}
