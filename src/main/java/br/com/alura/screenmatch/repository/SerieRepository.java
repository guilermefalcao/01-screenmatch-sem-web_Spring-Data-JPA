package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


// Interface que extende JpaRepository para operações de banco de dados
// JpaRepository<Serie, Long>:
//   - Serie: Tipo da entidade que será gerenciada
//   - Long: Tipo da chave primária (id) da entidade
// 
// Ao estender JpaRepository, ganhamos automaticamente métodos como:
//   - save(serie): Salva ou atualiza uma série no banco
//   - findById(id): Busca uma série pelo ID
//   - findAll(): Retorna todas as séries
//   - delete(serie): Remove uma série do banco
//   - count(): Conta quantas séries existem
//   - E muitos outros métodos prontos!
//
// NÃO precisamos implementar nada! O Spring Data JPA cria a implementação automaticamente
public interface SerieRepository extends JpaRepository<Serie, Long> {
    
    // ========================================
    // DERIVED QUERY METHODS (Métodos Derivados)
    // ========================================
    // O Spring Data JPA cria a implementação automaticamente baseado no NOME do método!
    // Não precisa escrever SQL ou JPQL!
    
    /**
     * Busca série por título (busca parcial, case-insensitive)
     * 
     * Nomenclatura do método:
     * - findBy: Indica que é uma busca
     * - Titulo: Nome do atributo da entidade Serie
     * - Containing: Busca parcial (SQL LIKE %valor%)
     * - IgnoreCase: Ignora maiúsculas/minúsculas
     * 
     * SQL gerado automaticamente:
     * SELECT * FROM series WHERE LOWER(titulo) LIKE LOWER('%nomeSerie%')
     * 
     * @param nomeSerie Nome ou parte do nome da série
     * @return Optional<Serie> - Pode estar vazio se não encontrar
     * 
     * Exemplos de uso:
     * - findByTituloContainingIgnoreCase("boys") → Encontra "The Boys"
     * - findByTituloContainingIgnoreCase("FRIENDS") → Encontra "Friends"
     */
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);
    
    // ========================================
    // OUTROS EXEMPLOS DE DERIVED QUERY METHODS
    // ========================================
    // Descomente para usar:
    
    // Busca exata por título
    // Optional<Serie> findByTitulo(String titulo);
    
    // Busca por gênero
    // List<Serie> findByGenero(Categoria genero);
    
    // Busca séries com avaliação maior ou igual
    // List<Serie> findByAvaliacaoGreaterThanEqual(Double avaliacao);
    
    // Busca top 5 séries por avaliação
    // List<Serie> findTop5ByOrderByAvaliacaoDesc();
    
    // Busca por gênero e avaliação mínima
    // List<Serie> findByGeneroAndAvaliacaoGreaterThanEqual(Categoria genero, Double avaliacao);

}
