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

    /**
     * Busca Top 5 episódios de uma série específica usando JPQL com JOIN, ORDER BY e LIMIT
     * 
     * JPQL SYNTAX:
     * - SELECT e: Seleciona apenas os episódios (entidade Episodio)
     * - FROM Serie s: Começa pela entidade Serie (alias 's')
     * - JOIN s.episodios e: Faz JOIN com a lista de episódios (alias 'e')
     *   - s.episodios: Atributo da entidade Serie (relacionamento @OneToMany)
     * - WHERE s = :serie: Filtra por série específica
     *   - s = :serie: Compara objeto Serie completo (usa ID internamente)
     * - ORDER BY e.avaliacao DESC: Ordena por avaliação (maior para menor)
     * - LIMIT 5: Retorna apenas os 5 primeiros resultados
     * 
     * SQL GERADO:
     * SELECT e.* FROM episodios e
     * JOIN series s ON e.serie_id = s.id
     * WHERE s.id = ?
     * ORDER BY e.avaliacao DESC
     * LIMIT 5
     * 
     * POR QUE USAR WHERE s = :serie?
     * - Comparação de objetos JPA (mais elegante)
     * - Hibernate converte automaticamente para WHERE s.id = serie.id
     * - Alternativa: WHERE s.id = :serieId (precisa passar ID manualmente)
     * 
     * DIFERENÇA: ORDER BY vs findTop5
     * - ORDER BY + LIMIT: JPQL manual (mais flexível)
     * - findTop5ByOrderBy...: Derived Query (automático, mas nome longo)
     * 
     * @param serie Série para buscar os melhores episódios
     * @return Lista com no máximo 5 episódios ordenados por avaliação
     * 
     * Exemplos de uso:
     * - topEpisodiosPorSerie(theBoysEntity) → Top 5 episódios de The Boys
     * - topEpisodiosPorSerie(friendsEntity) → Top 5 episódios de Friends
     * 
     * VANTAGENS:
     * ✅ Ordenação no banco (mais rápido que em memória)
     * ✅ LIMIT otimizado (não carrega todos os episódios)
     * ✅ Query específica para uma série (WHERE s = :serie)
     * ✅ Retorna apenas episódios (não série completa)
     */
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(@Param("serie") Serie serie);

    /**
     * Busca episódios de uma série lançados a partir de um ano específico
     * Usa JPQL com JOIN, WHERE e função YEAR() para filtrar por ano
     * 
     * JPQL SYNTAX:
     * - SELECT e: Seleciona apenas os episódios (entidade Episodio)
     * - FROM Serie s: Começa pela entidade Serie (alias 's')
     * - JOIN s.episodios e: Faz JOIN com a lista de episódios (alias 'e')
     *   - s.episodios: Atributo da entidade Serie (relacionamento @OneToMany)
     * - WHERE s = :serie: Filtra por série específica (compara objeto)
     * - AND YEAR(e.dataLancamento) >= :anoLancamento: Filtra por ano
     *   - YEAR(): Função JPQL que extrai o ano de uma data (LocalDate)
     *   - >=: Maior ou igual (episódios de 2020 em diante, por exemplo)
     * - ORDER BY e.dataLancamento: Ordena por data (cronológico)
     * 
     * SQL GERADO:
     * SELECT e.* FROM episodios e
     * JOIN series s ON e.serie_id = s.id
     * WHERE s.id = ?
     * AND EXTRACT(YEAR FROM e.data_lancamento) >= ?
     * ORDER BY e.data_lancamento
     * 
     * POR QUE USAR YEAR()?
     * - Mais fácil para o usuário (digita apenas o ano: 2022)
     * - Função portável (funciona em vários bancos)
     * - Hibernate converte para EXTRACT(YEAR FROM ...) no PostgreSQL
     * - Alternativa: Usar LocalDate.of(ano, 1, 1) e comparar datas completas
     * 
     * DIFERENÇA: YEAR() vs comparação de data completa
     * - YEAR(e.dataLancamento) >= 2022: Mais simples, filtra por ano
     * - e.dataLancamento >= '2022-01-01': Mais preciso, filtra por data exata
     * 
     * @param serie Série para buscar os episódios
     * @param anoLancamento Ano mínimo de lançamento (ex: 2022)
     * @return Lista de episódios lançados a partir do ano informado
     * 
     * Exemplos de uso:
     * - episodiosPorSerieEAno(theBoysEntity, 2022) → Episódios de The Boys de 2022 em diante
     * - episodiosPorSerieEAno(friendsEntity, 2000) → Episódios de Friends de 2000 em diante
     * 
     * VANTAGENS:
     * ✅ Filtra por ano (interface amigável)
     * ✅ Função YEAR() do JPQL (portável)
     * ✅ Ordenação cronológica
     * ✅ Query específica para uma série
     */
    /**
     * Busca episódios de uma série lançados a partir de um ano específico
     * Usa JPQL com JOIN, WHERE e função YEAR() para filtrar por ano
     * 
     * @param serie Série para buscar os episódios
     * @param anoLancamento Ano mínimo de lançamento (ex: 2022)
     * @return Lista de episódios lançados a partir do ano informado
     */
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento ORDER BY e.dataLancamento")
    List<Episodio> episodiosPorSerieEAno(@Param("serie") Serie serie, @Param("anoLancamento") Integer anoLancamento);

    /**
     * Busca Top 5 séries com episódios mais recentes (VERSÃO CORRIGIDA)
     * 
     * PROBLEMA DA DERIVED QUERY:
     * - findTop5ByOrderByEpisodiosDataLancamentoDesc() pode retornar MENOS de 5 séries
     * - Se os 5 episódios mais recentes forem da MESMA série, retorna apenas 1 série
     * - LEFT JOIN pega os 5 primeiros episódios, não 5 séries distintas
     * 
     * SOLUÇÃO COM JPQL:
     * - INNER JOIN: Garante que série tem episódios
     * - GROUP BY s: Agrupa por série (garante séries distintas)
     * - MAX(e.dataLancamento): Pega a data do episódio mais recente de cada série
     * - ORDER BY MAX(...) DESC: Ordena séries pela data do episódio mais recente
     * - LIMIT 5: Retorna 5 SÉRIES distintas
     * 
     * SQL GERADO:
     * SELECT s.* FROM series s
     * INNER JOIN episodios e ON s.id = e.serie_id
     * GROUP BY s.id
     * ORDER BY MAX(e.data_lancamento) DESC
     * LIMIT 5
     * 
     * DIFERENÇA:
     * - Derived Query: Pode retornar 1-5 séries (depende dos dados)
     * - JPQL com GROUP BY: SEMPRE retorna 5 séries distintas
     * 
     * @return Lista com 5 séries distintas (episódios mais recentes)
     */
    @Query("SELECT s FROM Serie s " +
            "JOIN s.episodios e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> encontrarEpisodiosMaisRecentes();

    /**
     * Busca episódios de uma temporada específica de uma série
     * Usa JPQL com JOIN e WHERE para filtrar por série E temporada
     * 
     * JPQL SYNTAX:
     * - SELECT e: Seleciona apenas os episódios (entidade Episodio)
     * - FROM Serie s: Começa pela entidade Serie (alias 's')
     * - JOIN s.episodios e: Faz JOIN com a lista de episódios (alias 'e')
     *   - s.episodios: Atributo da entidade Serie (relacionamento @OneToMany)
     * - WHERE s.id = :id: Filtra por ID da série
     * - AND e.temporada = :numero: Filtra por número da temporada
     * 
     * SQL GERADO:
     * SELECT e.* FROM series s
     * JOIN episodios e ON s.id = e.serie_id
     * WHERE s.id = ? AND e.temporada = ?
     * 
     * POR QUE USAR JPQL?
     * - Busca direta no banco (mais rápido)
     * - Filtra por série E temporada em uma única query
     * - Não carrega todos os episódios da série
     * - Retorna apenas episódios da temporada solicitada
     * 
     * ALTERNATIVA (menos eficiente):
     * - Buscar série completa: findById(id)
     * - Filtrar episódios em memória: serie.getEpisodios().stream().filter(...)
     * - Problema: Carrega TODOS os episódios da série
     * 
     * @param id ID da série
     * @param numero Número da temporada (1, 2, 3...)
     * @return Lista de episódios da temporada
     * 
     * Exemplos de uso:
     * - obterEpisodiosPorTemporada(7, 1) → Episódios da temporada 1 de Breaking Bad
     * - obterEpisodiosPorTemporada(1, 2) → Episódios da temporada 2 de The Boys
     * 
     * VANTAGENS:
     * ✅ Query otimizada (busca apenas o necessário)
     * ✅ Não carrega episódios de outras temporadas
     * ✅ Mais rápido que filtrar em memória
     * ✅ Menos uso de memória
     */
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodio> obterEpisodiosPorTemporada(@Param("id") Long id, @Param("numero") Long numero);

}
