package br.com.alura.screenmatch.exerciciosjpa.repository;

import br.com.alura.screenmatch.exerciciosjpa.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repositório JPA para a entidade Categoria
 * EXERCÍCIOS JPQL - AULA 03
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    /**
     * EXERCÍCIO 8: Contar número de produtos por categoria
     * JPQL: SELECT c.nome, COUNT(p) FROM Categoria c JOIN c.produtos p GROUP BY c.nome
     * Retorna Object[] com [nome_categoria, quantidade]
     */
    @Query("SELECT c.nome, COUNT(p) FROM Categoria c JOIN c.produtos p GROUP BY c.nome")
    List<Object[]> contarProdutosPorCategoria();
    
    /**
     * EXERCÍCIO 9: Filtrar categorias com mais de 10 produtos
     * JPQL: SELECT c FROM Categoria c WHERE SIZE(c.produtos) > 10
     * Função SIZE() retorna o tamanho da coleção
     */
    @Query("SELECT c FROM Categoria c WHERE SIZE(c.produtos) > 10")
    List<Categoria> findCategoriasComMaisDe10Produtos();
}
