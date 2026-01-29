package br.com.alura.screenmatch.exerciciosjpa.repository;

import br.com.alura.screenmatch.exerciciosjpa.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositório JPA para a entidade Produto
 * Extende JpaRepository para operações CRUD automáticas
 * 
 * EXERCÍCIOS JPQL - AULA 03
 * Consultas personalizadas usando @Query com JPQL e SQL Nativo
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // ========================================
    // EXERCÍCIOS JPQL - AULA 03
    // ========================================
    
    /**
     * EXERCÍCIO 1: Produtos com preço maior que um valor
     * JPQL: SELECT p FROM Produto p WHERE p.preco > :preco
     */
    @Query("SELECT p FROM Produto p WHERE p.preco > :preco")
    List<Produto> findByPrecoMaiorQue(@Param("preco") Double preco);
    
    /**
     * EXERCÍCIO 2: Produtos ordenados por preço crescente
     * JPQL: SELECT p FROM Produto p ORDER BY p.preco ASC
     */
    @Query("SELECT p FROM Produto p ORDER BY p.preco ASC")
    List<Produto> findAllOrderByPrecoAsc();
    
    /**
     * EXERCÍCIO 3: Produtos ordenados por preço decrescente
     * JPQL: SELECT p FROM Produto p ORDER BY p.preco DESC
     */
    @Query("SELECT p FROM Produto p ORDER BY p.preco DESC")
    List<Produto> findAllOrderByPrecoDesc();
    
    /**
     * EXERCÍCIO 4: Produtos que começam com uma letra específica
     * JPQL: SELECT p FROM Produto p WHERE p.nome LIKE :letra%
     */
    @Query("SELECT p FROM Produto p WHERE p.nome LIKE :letra%")
    List<Produto> findByNomeStartingWith(@Param("letra") String letra);
    
    /**
     * EXERCÍCIO 6: Média de preços dos produtos
     * JPQL: SELECT AVG(p.preco) FROM Produto p
     * Função agregada AVG() retorna Double
     */
    @Query("SELECT AVG(p.preco) FROM Produto p")
    Double calcularMediaPrecos();
    
    /**
     * EXERCÍCIO 7: Preço máximo de um produto em uma categoria
     * JPQL: SELECT MAX(p.preco) FROM Produto p WHERE p.categoria.nome = :categoria
     * Função agregada MAX() retorna Double
     */
    @Query("SELECT MAX(p.preco) FROM Produto p WHERE p.categoria.nome = :categoria")
    Double findPrecoMaximoByCategoria(@Param("categoria") String categoria);
    
    /**
     * EXERCÍCIO 10: Produtos filtrados por nome OU categoria
     * JPQL: SELECT p FROM Produto p WHERE p.nome LIKE %:termo% OR p.categoria.nome = :categoria
     * Operador OR combina duas condições
     */
    @Query("SELECT p FROM Produto p WHERE p.nome LIKE %:termo% OR p.categoria.nome = :categoria")
    List<Produto> findByNomeOrCategoria(@Param("termo") String termo, @Param("categoria") String categoria);
    
    /**
     * EXERCÍCIO 11: Query nativa para buscar os 5 produtos mais caros
     * SQL NATIVO: SELECT * FROM produtos ORDER BY preco DESC LIMIT 5
     * nativeQuery = true: Usa SQL nativo em vez de JPQL
     */
    @Query(value = "SELECT * FROM produtos ORDER BY preco DESC LIMIT 5", nativeQuery = true)
    List<Produto> findTop5MaisCarosNativo();
    
    // ========================================
    // DERIVED QUERY METHODS (mantidos)
    // ========================================
    
    List<Produto> findByNome(String nome);
    List<Produto> findByCategoriaNome(String categoriaNome);
    List<Produto> findByPrecoGreaterThan(Double preco);
    List<Produto> findByPrecoLessThan(Double preco);
    List<Produto> findByNomeContaining(String termo);
    List<Produto> findByCategoriaNomeOrderByPrecoAsc(String categoriaNome);
    List<Produto> findByCategoriaNomeOrderByPrecoDesc(String categoriaNome);
    long countByCategoriaNome(String categoriaNome);
    long countByPrecoGreaterThan(Double preco);
    List<Produto> findByPrecoLessThanOrNomeContaining(Double preco, String termo);
    List<Produto> findTop3ByOrderByPrecoDesc();
    List<Produto> findTop5ByCategoriaNomeOrderByPrecoAsc(String categoriaNome);
}
