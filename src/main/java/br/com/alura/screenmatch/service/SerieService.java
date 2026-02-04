package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SERVICE - Camada de Serviço (Lógica de Negócio)
 * 
 * ARQUITETURA EM CAMADAS (MVC):
 * 
 * Controller → Service → Repository → Database
 * 
 * RESPONSABILIDADES DE CADA CAMADA:
 * 
 * 1. CONTROLLER (SerieController):
 *    - Recebe requisições HTTP
 *    - Valida dados de entrada
 *    - Chama Service
 *    - Retorna resposta HTTP
 *    - NÃO acessa Repository diretamente!
 * 
 * 2. SERVICE (SerieService) ← VOCÊ ESTÁ AQUI:
 *    - Lógica de negócio
 *    - Regras da aplicação
 *    - Conversões (Serie → SerieDTO)
 *    - Chama Repository
 *    - NÃO conhece HTTP (sem @GetMapping, @PostMapping)
 * 
 * 3. REPOSITORY (SerieRepository):
 *    - Acesso ao banco de dados
 *    - Queries JPA/JPQL
 *    - CRUD básico
 * 
 * 4. MODEL (Serie, Episodio):
 *    - Entidades JPA
 *    - Representam tabelas do banco
 * 
 * POR QUE USAR SERVICE?
 * 
 * ✅ BAIXO ACOPLAMENTO:
 * - Controller NÃO depende diretamente do Repository
 * - Se mudar Repository, Controller não precisa mudar
 * - Fácil trocar implementação (ex: PostgreSQL → MongoDB)
 * 
 * ✅ ALTA COESÃO:
 * - Cada classe tem UMA responsabilidade bem definida
 * - Controller: HTTP
 * - Service: Lógica de negócio
 * - Repository: Banco de dados
 * 
 * ✅ REUTILIZAÇÃO:
 * - Service pode ser usado por múltiplos Controllers
 * - Lógica centralizada em um lugar
 * 
 * ✅ TESTABILIDADE:
 * - Fácil criar testes unitários
 * - Mock do Repository
 * 
 * EXEMPLO DE FLUXO:
 * 
 * 1. Cliente: GET http://localhost:8080/series
 *    ↓
 * 2. SerieController.obterSeries()
 *    ↓
 * 3. serieService.obterTodasAsSeries()  ← Chama Service
 *    ↓
 * 4. repository.findAll()  ← Service chama Repository
 *    ↓
 * 5. PostgreSQL retorna List<Serie>
 *    ↓
 * 6. Service converte Serie → SerieDTO
 *    ↓
 * 7. Service retorna List<SerieDTO>
 *    ↓
 * 8. Controller retorna JSON para cliente
 * 
 * ANOTAÇÕES:
 * - @Service: Marca como componente de serviço do Spring
 * - @Autowired: Injeção de dependência do Repository
 */
@Service
public class SerieService {

    // @Autowired: Injeção de dependência
    // Spring cria automaticamente uma instância de SerieRepository e injeta aqui
    // Repository agora está na camada Service, NÃO no Controller!
    @Autowired
    private SerieRepository repository;

    /**
     * Obtém todas as séries do banco e converte para DTO
     * 
     * @return Lista de SerieDTO (sem episódios)
     */
    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repository.findAll());
    }

    /**
     * Obtém as 5 séries com melhor avaliação
     * 
     * @return Lista com 5 SerieDTO (melhores avaliações)
     */
    public List<SerieDTO> obterTop5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    /**
     * Obtém as 5 séries com lançamentos mais recentes (VERSÃO CORRIGIDA)
     * 
     * PROBLEMA ANTERIOR:
     * - Derived Query podia retornar MENOS de 5 séries
     * - Se os 5 episódios mais recentes fossem da mesma série, retornava apenas 1
     * 
     * SOLUÇÃO:
     * - JPQL com GROUP BY garante 5 séries DISTINTAS
     * - MAX(e.dataLancamento) pega o episódio mais recente de cada série
     * - ORDER BY MAX(...) ordena séries pela data mais recente
     * 
     * SQL GERADO:
     * SELECT s.* FROM series s
     * INNER JOIN episodios e ON s.id = e.serie_id
     * GROUP BY s.id
     * ORDER BY MAX(e.data_lancamento) DESC
     * LIMIT 5
     * 
     * @return Lista com 5 SerieDTO (lançamentos mais recentes)
     */
    public List<SerieDTO> obterLancamentos() {
        return converteDados(repository.encontrarEpisodiosMaisRecentes());
    }

    /**
     * Obtém uma série específica pelo ID
     * 
     * Optional<Serie>:
     * - findById() retorna Optional porque o ID pode não existir no banco
     * - Optional evita NullPointerException
     * - Precisa verificar se existe com isPresent()
     * 
     * FLUXO:
     * 1. Busca série no banco: repository.findById(id)
     * 2. Verifica se existe: serie.isPresent()
     * 3. Se existe: Extrai objeto com serie.get()
     * 4. Converte Serie → SerieDTO
     * 5. Retorna SerieDTO
     * 6. Se NÃO existe: Retorna null
     * 
     * SQL GERADO:
     * SELECT * FROM series WHERE id = ?
     * 
     * ALTERNATIVA (mais elegante):
     * return repository.findById(id)
     *     .map(s -> new SerieDTO(...))
     *     .orElse(null);
     * 
     * @param id ID da série
     * @return SerieDTO ou null se não encontrar
     */
    public SerieDTO obterPorId(Long id) {
        // findById() retorna Optional<Serie> (pode estar vazio)
        Optional<Serie> serie = repository.findById(id);

        // Verifica se a série existe no banco
        if (serie.isPresent()) {
            // Extrai o objeto Serie do Optional
            Serie s = serie.get();
            
            // Converte Serie → SerieDTO manualmente
            // (não usa converteDados porque é apenas 1 objeto, não lista)
            return new SerieDTO(
                    s.getId(),
                    s.getTitulo(),
                    s.getTotalTemporadas(),
                    s.getAvaliacao(),
                    s.getGenero(),
                    s.getAtores(),
                    s.getPoster(),
                    s.getSinopse()
            );
        }
        
        // Se não encontrar, retorna null
        // Alternativa: throw new RuntimeException("Série não encontrada");
        return null;
    }

    /**
     * Obtém todos os episódios de todas as temporadas de uma série
     * 
     * FLUXO:
     * 1. Busca série no banco: repository.findById(id)
     * 2. Verifica se existe: serie.isPresent()
     * 3. Se existe: Extrai objeto com serie.get()
     * 4. Pega lista de episódios: s.getEpisodios()
     * 5. Converte cada Episodio → EpisodioDTO usando stream
     * 6. Retorna List<EpisodioDTO>
     * 7. Se NÃO existe: Retorna null
     * 
     * SQL GERADO:
     * SELECT * FROM series WHERE id = ?
     * SELECT * FROM episodios WHERE serie_id = ?
     * 
     * POR QUE USAR DTO?
     * - Expõe apenas: temporada, numeroEpisodio, titulo
     * - NÃO expõe: id, avaliacao, dataLancamento, serie (evita loop infinito)
     * - JSON menor e mais rápido
     * 
     * CONVERSÃO:
     * Episodio (entidade) → EpisodioDTO (DTO)
     * - e.getTemporada() → temporada
     * - e.getNumeroEpisodio() → numeroEpisodio
     * - e.getTitulo() → titulo
     * 
     * @param id ID da série
     * @return Lista de EpisodioDTO ou null se série não existir
     */
    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        // Busca série no banco
        Optional<Serie> serie = repository.findById(id);
        
        // Verifica se a série existe
        if (serie.isPresent()) {
            // Extrai o objeto Serie do Optional
            Serie s = serie.get();
            
            // Converte lista de Episodio → lista de EpisodioDTO
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(
                            e.getTemporada(),
                            e.getNumeroEpisodio(),
                            e.getTitulo()
                    ))
                    .collect(Collectors.toList());
        }
        
        // Se não encontrar, retorna null
        return null;
    }

    /**
     * Obtém episódios de uma temporada específica de uma série
     * 
     * FLUXO:
     * 1. Controller recebe id da série e número da temporada
     * 2. Service chama Repository: repository.obterEpisodiosPorTemporada(id, numero)
     * 3. Repository executa JPQL com JOIN e WHERE
     * 4. Service converte Episodio → EpisodioDTO
     * 5. Retorna List<EpisodioDTO>
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
     * 
     * CONVERSÃO:
     * Episodio (entidade) → EpisodioDTO (DTO)
     * - e.getTemporada() → temporada
     * - e.getNumeroEpisodio() → numeroEpisodio
     * - e.getTitulo() → titulo
     * 
     * @param id ID da série
     * @param numero Número da temporada (1, 2, 3...)
     * @return Lista de EpisodioDTO da temporada
     */
    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
        // Busca episódios da temporada específica no banco
        return repository.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(
                        e.getTemporada(),
                        e.getNumeroEpisodio(),
                        e.getTitulo()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Método privado para converter List<Serie> em List<SerieDTO>
     * 
     * DRY (Don't Repeat Yourself):
     * - Evita duplicação de código
     * - Centraliza lógica de conversão em um único lugar
     * - Se precisar mudar conversão, muda apenas aqui
     * 
     * REUTILIZAÇÃO:
     * - Usado por obterTodasAsSeries()
     * - Usado por obterTop5Series()
     * - Pode ser usado por futuros métodos
     * 
     * @param series Lista de entidades Serie do banco
     * @return Lista de SerieDTO (sem episódios)
     */
    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(
                        s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getAvaliacao(),
                        s.getGenero(),
                        s.getAtores(),
                        s.getPoster(),
                        s.getSinopse()
                ))
                .collect(Collectors.toList());
    }

    // FUTUROS MÉTODOS (exemplos):
    
    // public SerieDTO obterSeriePorId(Long id) {
    //     Serie serie = repository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Série não encontrada"));
    //     return new SerieDTO(...);
    // }
    
    // public List<SerieDTO> obterTop5Series() {
    //     return repository.findTop5ByOrderByAvaliacaoDesc()
    //             .stream()
    //             .map(s -> new SerieDTO(...))
    //             .collect(Collectors.toList());
    // }
    
    // public List<SerieDTO> buscarSeriesPorCategoria(Categoria categoria) {
    //     return repository.findByGenero(categoria)
    //             .stream()
    //             .map(s -> new SerieDTO(...))
    //             .collect(Collectors.toList());
    // }
}
