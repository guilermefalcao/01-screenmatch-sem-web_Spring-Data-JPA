package br.com.alura.screenmatch.exerciciosjpa.repository;

import br.com.alura.screenmatch.exerciciosjpa.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório JPA para a entidade Produto
 * Extende JpaRepository para operações CRUD automáticas
 * 
 * DERIVED QUERY METHODS - EXERCÍCIOS AVANÇADOS
 * O Spring Data JPA cria automaticamente a implementação baseada no nome do método
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // ========================================
    // CONSULTAS BÁSICAS
    // ========================================
    
    /**
     * 1. Busca produtos pelo nome exato
     * SQL: SELECT * FROM produtos WHERE nome = ?
     */
    List<Produto> findByNome(String nome);
    
    /**
     * 2. Busca produtos por categoria (relacionamento)
     * SQL: SELECT p.* FROM produtos p JOIN categorias c ON p.categoria_id = c.id WHERE c.nome = ?
     */
    List<Produto> findByCategoriaNome(String categoriaNome);
    
    /**
     * 3. Busca produtos com preço maior que o valor
     * SQL: SELECT * FROM produtos WHERE preco > ?
     */
    List<Produto> findByPrecoGreaterThan(Double preco);
    
    /**
     * 4. Busca produtos com preço menor que o valor
     * SQL: SELECT * FROM produtos WHERE preco < ?
     */
    List<Produto> findByPrecoLessThan(Double preco);
    
    /**
     * 5. Busca produtos cujo nome contém o termo
     * SQL: SELECT * FROM produtos WHERE nome LIKE %termo%
     */
    List<Produto> findByNomeContaining(String termo);
    
    // ========================================
    // CONSULTAS COM ORDENAÇÃO
    // ========================================
    
    /**
     * 8. Busca produtos de uma categoria ordenados por preço crescente
     * SQL: SELECT p.* FROM produtos p JOIN categorias c ON p.categoria_id = c.id 
     *      WHERE c.nome = ? ORDER BY p.preco ASC
     */
    List<Produto> findByCategoriaNomeOrderByPrecoAsc(String categoriaNome);
    
    /**
     * 9. Busca produtos de uma categoria ordenados por preço decrescente
     * SQL: SELECT p.* FROM produtos p JOIN categorias c ON p.categoria_id = c.id 
     *      WHERE c.nome = ? ORDER BY p.preco DESC
     */
    List<Produto> findByCategoriaNomeOrderByPrecoDesc(String categoriaNome);
    
    // ========================================
    // CONSULTAS DE CONTAGEM
    // ========================================
    
    /**
     * 10. Conta produtos em uma categoria específica
     * SQL: SELECT COUNT(*) FROM produtos p JOIN categorias c ON p.categoria_id = c.id WHERE c.nome = ?
     */
    long countByCategoriaNome(String categoriaNome);
    
    /**
     * 11. Conta produtos com preço maior que o valor
     * SQL: SELECT COUNT(*) FROM produtos WHERE preco > ?
     */
    long countByPrecoGreaterThan(Double preco);
    
    // ========================================
    // CONSULTAS COMPOSTAS (OR)
    // ========================================
    
    /**
     * 12. Busca produtos com preço menor OU nome contendo termo
     * SQL: SELECT * FROM produtos WHERE preco < ? OR nome LIKE %termo%
     */
    List<Produto> findByPrecoLessThanOrNomeContaining(Double preco, String termo);
    
    // ========================================
    // CONSULTAS TOP/LIMIT
    // ========================================
    
    /**
     * 16. Busca os 3 produtos mais caros
     * SQL: SELECT * FROM produtos ORDER BY preco DESC LIMIT 3
     */
    List<Produto> findTop3ByOrderByPrecoDesc();
    
    /**
     * 17. Busca os 5 produtos mais baratos de uma categoria
     * SQL: SELECT p.* FROM produtos p JOIN categorias c ON p.categoria_id = c.id 
     *      WHERE c.nome = ? ORDER BY p.preco ASC LIMIT 5
     */
    List<Produto> findTop5ByCategoriaNomeOrderByPrecoAsc(String categoriaNome);
}
