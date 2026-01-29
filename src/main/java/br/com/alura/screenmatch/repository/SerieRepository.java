package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    /**
     * Busca séries por ator/atriz E avaliação mínima (DERIVED QUERY METHOD COMPOSTO)
     * 
     * Nomenclatura do método:
     * - findBy: Indica que é uma busca
     * - Atores: Primeiro critério de busca
     * - Containing: Busca parcial no campo atores
     * - IgnoreCase: Ignora maiúsculas/minúsculas
     * - And: Combina dois critérios (WHERE ... AND ...)
     * - Avaliacao: Segundo critério de busca
     * - GreaterThanEqual: Maior ou igual (>=)
     * 
     * SQL gerado automaticamente:
     * SELECT * FROM series 
     * WHERE LOWER(atores) LIKE LOWER('%nomeAtor%') 
     * AND avaliacao >= :avaliacao
     * 
     * @param nomeAtor Nome ou parte do nome do ator
     * @param avaliacao Avaliação mínima (ex: 8.0)
     * @return List<Serie> - Lista de séries que atendem AMBOS os critérios
     * 
     * Exemplos de uso:
     * - findBy...(...("Karl", 8.0) → Séries com Karl Urban E avaliação >= 8.0
     * - findBy...(...("Jennifer", 9.0) → Séries com Jennifer Aniston E avaliação >= 9.0
     * 
     * Outros operadores disponíveis:
     * - GreaterThan: Maior que (>)
     * - LessThan: Menor que (<)
     * - LessThanEqual: Menor ou igual (<=)
     * - Between: Entre dois valores
     */
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    /**
     * Busca Top 5 séries com melhor avaliação (LIMIT + ORDER BY)
     * 
     * Nomenclatura do método:
     * - findTop5: Limita resultado a 5 registros (LIMIT 5)
     * - By: Separador (obrigatório mesmo sem condição WHERE)
     * - OrderBy: Indica ordenação
     * - Avaliacao: Campo para ordenar
     * - Desc: Ordem decrescente (maior para menor)
     * 
     * SQL gerado automaticamente:
     * SELECT * FROM series 
     * ORDER BY avaliacao DESC 
     * LIMIT 5
     * 
     * @return List<Serie> - Lista com no máximo 5 séries ordenadas por avaliação
     * 
     * Exemplos de uso:
     * - findTop5ByOrderByAvaliacaoDesc() → Top 5 séries
     * - findTop10ByOrderByAvaliacaoDesc() → Top 10 séries
     * - findFirst3ByOrderByTituloAsc() → Primeiras 3 séries por título (A-Z)
     * 
     * Variações:
     * - Top5, Top10, First3, etc.
     * - Desc (decrescente) ou Asc (crescente)
     * - Pode combinar com WHERE: findTop5ByGeneroOrderByAvaliacaoDesc(Categoria genero)
     */
    List<Serie> findTop5ByOrderByAvaliacaoDesc();
    
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
    
    // Busca top 5 séries por avaliação (implementado acima)
    // List<Serie> findTop5ByOrderByAvaliacaoDesc();
    
    // Busca por gênero e avaliação mínima
    // List<Serie> findByGeneroAndAvaliacaoGreaterThanEqual(Categoria genero, Double avaliacao);
    
    // Busca por número de temporadas
    // List<Serie> findByTotalTemporadasLessThanEqual(Integer temporadas);

    List<Serie> findByGenero(Categoria categoria);

    /**
     * Busca séries por número máximo de temporadas E avaliação mínima
     * Usa Derived Query Method COMPOSTO do Spring Data JPA
     * 
     * Nomenclatura do método:
     * - findBy: Indica que é uma busca
     * - TotalTemporadas: Primeiro critério de busca
     * - LessThanEqual: Menor ou igual (<=)
     * - And: Combina dois critérios (WHERE ... AND ...)
     * - Avaliacao: Segundo critério de busca
     * - GreaterThanEqual: Maior ou igual (>=)
     * 
     * SQL gerado automaticamente:
     * SELECT * FROM series 
     * WHERE total_temporadas <= :totalTemporadas 
     * AND avaliacao >= :avaliacao
     * 
     * @param totalTemporadas Número máximo de temporadas (ex: 3)
     * @param avaliacao Avaliação mínima (ex: 8.0)
     * @return List<Serie> - Lista de séries que atendem AMBOS os critérios
     * 
     * Exemplos de uso:
     * - findBy...(3, 8.0) → Séries com até 3 temporadas E avaliação >= 8.0
     * - findBy...(5, 9.0) → Séries com até 5 temporadas E avaliação >= 9.0
     */
    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalTemporadas, Double avaliacao);

    // ========================================
    // JPQL (Java Persistence Query Language)
    // ========================================
    // JPQL permite escrever queries personalizadas usando nomes de classes Java
    // em vez de tabelas SQL. Oferece mais flexibilidade que Derived Queries.
    
    /**
     * COMPARAÇÃO: Derived Query vs JPQL
     * 
     * DERIVED QUERY METHOD (implementado acima):
     * List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer, Double);
     * 
     * JPQL (implementado abaixo):
     * @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :temporadas AND s.avaliacao >= :avaliacao")
     * List<Serie> seriesPorTemporadaEAvaliacao(@Param("temporadas") Integer totalTemporadas, @Param("avaliacao") Double avaliacao);
     * 
     * VANTAGENS DO JPQL:
     * ✅ Nome do método livre (não precisa seguir convenção)
     * ✅ Query mais legível e explícita
     * ✅ Permite queries complexas (subconsultas, joins, funções)
     * ✅ Controle total sobre a consulta
     * ✅ Reutilização de queries complexas
     * 
     * DESVANTAGENS DO JPQL:
     * ❌ Mais código para escrever
     * ❌ Possibilidade de erros de sintaxe
     * ❌ Requer conhecimento de JPQL
     * ❌ Menos "mágico" que Derived Queries
     * 
     * QUANDO USAR JPQL:
     * - Queries muito complexas
     * - Joins personalizados
     * - Subconsultas
     * - Funções agregadas avançadas
     * - Quando Derived Query fica muito longa
     */
    
    /**
     * Busca séries por temporadas e avaliação usando JPQL
     * 
     * JPQL Syntax:
     * - SELECT s FROM Serie s: Seleciona entidade Serie (alias 's')
     * - WHERE s.totalTemporadas <= :temporadas: Usa atributo Java (não coluna SQL)
     * - :temporadas: Parâmetro nomeado (binding seguro)
     * - AND s.avaliacao >= :avaliacao: Segunda condição
     * 
     * SQL equivalente gerado:
     * SELECT * FROM series 
     * WHERE total_temporadas <= ? 
     * AND avaliacao >= ?
     * 
     * @param temporadas Número máximo de temporadas
     * @param avaliacao Avaliação mínima
     * @return Lista de séries filtradas
     * 
     * Exemplos de uso:
     * - seriesPorTemporadaEAvaliacao(3, 8.0) → Séries até 3 temporadas, avaliação >= 8.0
     * - seriesPorTemporadaEAvaliacao(5, 9.0) → Séries até 5 temporadas, avaliação >= 9.0
     */
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :temporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(@Param("temporadas") Integer totalTemporadas, @Param("avaliacao") Double avaliacao);

    /**
     * Busca episódios por trecho do título usando JPQL com JOIN
     * 
     * JPQL SYNTAX:
     * - SELECT e: Seleciona apenas os episódios (entidade Episodio)
     * - FROM Serie s: Começa pela entidade Serie (alias 's')
     * - JOIN s.episodios e: Faz JOIN com a lista de episódios (alias 'e')
     *   - s.episodios: Atributo da entidade Serie (relacionamento @OneToMany)
     * - WHERE e.titulo ILIKE %:trechoEpisodio%: Busca parcial case-insensitive
     *   - ILIKE: Case-insensitive LIKE (PostgreSQL)
     *   - %:trechoEpisodio%: Parâmetro nomeado com wildcards
     * 
     * SQL GERADO:
     * SELECT e.* FROM episodios e
     * JOIN series s ON e.serie_id = s.id
     * WHERE LOWER(e.titulo) LIKE LOWER('%trecho%')
     * 
     * POR QUE USAR JOIN?
     * - Busca episódios em TODAS as séries de uma vez
     * - Retorna apenas episódios (não séries completas)
     * - Query otimizada no banco de dados
     * 
     * DIFERENÇA: ILIKE vs LIKE
     * - ILIKE: Case-insensitive (PostgreSQL específico)
     * - LIKE: Case-sensitive
     * - Para outros bancos, use: LOWER(e.titulo) LIKE LOWER(CONCAT('%', :trechoEpisodio, '%'))
     * 
     * @param trechoEpisodio Trecho do título do episódio
     * @return Lista de episódios que contém o trecho no título
     * 
     * Exemplos de uso:
     * - episodiosPorTrecho("pilot") → Todos os episódios com "pilot" no título
     * - episodiosPorTrecho("finale") → Todos os episódios finais
     */
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(@Param("trechoEpisodio") String trechoEpisodio);

}
