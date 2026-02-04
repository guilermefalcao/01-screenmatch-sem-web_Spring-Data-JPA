package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * CONTROLLER REST - Camada de Apresentação
 * 
 * RESPONSABILIDADE DO CONTROLLER:
 * - Receber requisições HTTP
 * - Validar dados de entrada
 * - Chamar Service (NÃO Repository diretamente!)
 * - Retornar resposta HTTP
 * 
 * ARQUITETURA EM CAMADAS (MVC):
 * 
 * Cliente (Navegador/Postman)
 *    ↓ HTTP Request
 * Controller (SerieController) ← VOCÊ ESTÁ AQUI
 *    ↓ Chama
 * Service (SerieService) ← Lógica de negócio
 *    ↓ Chama
 * Repository (SerieRepository) ← Acesso ao banco
 *    ↓ SQL
 * Database (PostgreSQL)
 * 
 * BAIXO ACOPLAMENTO:
 * - Controller NÃO conhece Repository
 * - Controller só conhece Service
 * - Se mudar Repository, Controller não muda
 * - Fácil trocar implementação
 * 
 * ALTA COESÃO:
 * - Controller: Apenas HTTP (receber/retornar)
 * - Service: Apenas lógica de negócio
 * - Repository: Apenas banco de dados
 * - Cada classe tem UMA responsabilidade
 * 
 * ANTES (ERRADO - Alto Acoplamento):
 * Controller → Repository (direto)
 * - Controller conhece detalhes do banco
 * - Difícil testar
 * - Difícil trocar implementação
 * 
 * AGORA (CORRETO - Baixo Acoplamento):
 * Controller → Service → Repository
 * - Controller só conhece Service
 * - Fácil testar (mock do Service)
 * - Fácil trocar implementação
 * 
 * ANOTAÇÕES:
 * - @RestController: Controller REST (retorna dados, não HTML)
 * - @GetMapping: Mapeia requisição GET para método
 * - @Autowired: Injeção de dependência do Service
 */
@RestController
public class SerieController {

    // @Autowired: Injeção de dependência do Spring
    // Spring cria automaticamente uma instância de SerieService e injeta aqui
    // Controller agora depende de SERVICE, NÃO de Repository!
    @Autowired
    private SerieService servico;

    // REMOVIDO: Repository não fica mais no Controller!
    // @Autowired
    // private SerieRepository repositorio;  ← ERRADO! Alto acoplamento
    //
    // POR QUE REMOVER?
    // - Controller não deve acessar banco diretamente
    // - Viola princípio de Separação de Responsabilidades
    // - Dificulta testes unitários
    // - Repository agora está no Service (lugar correto)

    /**
     * Endpoint GET /inicio
     * 
     * Endpoint simples para TESTAR o DevTools (hot reload automático).
     * Retorna apenas uma mensagem de texto.
     * 
     * @return Mensagem de boas-vindas (texto simples)
     * 
     * TESTE:
     * http://localhost:8080/inicio
     */
    @GetMapping("/inicio")
    public String inicio() {
        return "Bem-vindo ao Screenmatch!";
    }

    /**
     * Endpoint GET /series
     * 
     * Retorna todas as séries cadastradas no banco de dados em formato JSON.
     * Usa DTO para expor apenas dados necessários (SEM episódios).
     * 
     * FLUXO:
     * 1. Controller recebe requisição HTTP
     * 2. Controller chama Service: servico.obterTodasAsSeries()
     * 3. Service busca no Repository: repository.findAll()
     * 4. Service converte Serie → SerieDTO
     * 5. Service retorna List<SerieDTO>
     * 6. Controller retorna JSON para cliente
     * 
     * BAIXO ACOPLAMENTO:
     * - Controller NÃO conhece Repository
     * - Controller NÃO conhece detalhes de conversão
     * - Controller apenas chama Service e retorna resultado
     * 
     * @return Lista de SerieDTO (convertida automaticamente para JSON)
     * 
     * TESTE:
     * http://localhost:8080/series
     * 
     * RESPOSTA:
     * [{"id":1,"titulo":"Breaking Bad",...}]
     */
    @GetMapping("/series")
    public List<SerieDTO> obterSeries() {
        // Controller apenas chama Service e retorna resultado
        // Toda lógica de negócio está no Service
        return servico.obterTodasAsSeries();
    }

    /**
     * Endpoint GET /series/top5
     * 
     * Retorna as 5 séries com melhor avaliação em ordem decrescente.
     * 
     * @return Lista com 5 SerieDTO (melhores avaliações)
     * 
     * TESTE:
     * http://localhost:8080/series/top5
     */
    @GetMapping("/series/top5")
    public List<SerieDTO> obterTop5Series() {
        return servico.obterTop5Series();
    }

    /**
     * Endpoint GET /series/lancamentos
     * 
     * Retorna as 5 séries com lançamentos mais recentes.
     * Ordena pelas datas de lançamento dos episódios (mais recente primeiro).
     * 
     * FLUXO:
     * 1. Controller recebe requisição HTTP
     * 2. Controller chama Service: servico.obterLancamentos()
     * 3. Service busca no Repository: repository.findTop5ByOrderByEpisodiosDataLancamentoDesc()
     * 4. Repository faz JOIN com episodios e ordena por data_lancamento DESC
     * 5. Service converte Serie → SerieDTO
     * 6. Controller retorna JSON para cliente
     * 
     * DERIVED QUERY METHOD:
     * - findTop5: Limita a 5 registros
     * - ByOrderBy: Ordenação
     * - Episodios: Navega para relacionamento @OneToMany (Serie.episodios)
     * - DataLancamento: Campo do Episodio
     * - Desc: Ordem decrescente (mais recente primeiro)
     * 
     * SQL GERADO:
     * SELECT DISTINCT s.* FROM series s
     * JOIN episodios e ON s.id = e.serie_id
     * ORDER BY e.data_lancamento DESC
     * LIMIT 5
     * 
     * USO:
     * - Mostrar "Novidades" ou "Lançamentos Recentes" na API
     * - Séries que tiveram episódios lançados recentemente
     * 
     * @return Lista com 5 SerieDTO (lançamentos mais recentes)
     * 
     * TESTE:
     * http://localhost:8080/series/lancamentos
     * 
     * RESPOSTA:
     * [
     *   {"id":1,"titulo":"The Boys","totalTemporadas":4,...},
     *   {"id":2,"titulo":"Breaking Bad","totalTemporadas":5,...},
     *   ...
     * ]
     */
    @GetMapping("/series/lancamentos")
    public List<SerieDTO> obterLancamentos() {
        return servico.obterLancamentos();
    }

    /**
     * Endpoint GET /series/{id}
     * 
     * Retorna UMA série específica pelo ID.
     * 
     * @PathVariable: Indica que o parâmetro vem do caminho da URL
     * - URL: /series/1 → id = 1
     * - URL: /series/42 → id = 42
     * 
     * FLUXO:
     * 1. Cliente: GET http://localhost:8080/series/1
     * 2. Controller recebe id = 1 via @PathVariable
     * 3. Controller chama Service: servico.obterPorId(1)
     * 4. Service busca no Repository: repository.findById(1)
     * 5. Service converte Serie → SerieDTO
     * 6. Controller retorna JSON para cliente
     * 
     * @param id ID da série (vem da URL)
     * @return SerieDTO ou null se não encontrar
     * 
     * TESTE:
     * http://localhost:8080/series/1
     * 
     * RESPOSTA:
     * {"id":1,"titulo":"Breaking Bad","totalTemporadas":5,...}
     */
    @GetMapping("/series/{id}")
    public SerieDTO obterPorId(@PathVariable Long id) {
        // @PathVariable: Extrai o {id} da URL e passa como parâmetro
        return servico.obterPorId(id);
    }

    /**
     * Endpoint GET /series/{id}/temporadas/todas
     * 
     * Retorna TODOS os episódios de TODAS as temporadas de uma série.
     * 
     * @PathVariable: Captura o ID da série da URL
     * - URL: /series/1/temporadas/todas → id = 1
     * - URL: /series/7/temporadas/todas → id = 7
     * 
     * FLUXO:
     * 1. Cliente: GET http://localhost:8080/series/1/temporadas/todas
     * 2. Controller recebe id = 1 via @PathVariable
     * 3. Controller chama Service: servico.obterTodasTemporadas(1)
     * 4. Service busca série no banco: repository.findById(1)
     * 5. Service pega lista de episódios: serie.getEpisodios()
     * 6. Service converte Episodio → EpisodioDTO (apenas temporada, numero, titulo)
     * 7. Controller retorna JSON para cliente
     * 
     * POR QUE USAR EpisodioDTO?
     * - Expõe apenas: temporada, numeroEpisodio, titulo
     * - NÃO expõe: id, avaliacao, dataLancamento, serie (evita loop infinito)
     * - JSON menor e mais rápido
     * 
     * SQL GERADO:
     * SELECT * FROM series WHERE id = 1
     * SELECT * FROM episodios WHERE serie_id = 1
     * 
     * @param id ID da série (vem da URL)
     * @return Lista de EpisodioDTO ou null se série não existir
     * 
     * TESTE:
     * http://localhost:8080/series/1/temporadas/todas
     * http://localhost:8080/series/7/temporadas/todas (Breaking Bad)
     * 
     * RESPOSTA:
     * [
     *   {"temporada":1,"numeroEpisodio":1,"titulo":"Pilot"},
     *   {"temporada":1,"numeroEpisodio":2,"titulo":"Cat's in the Bag..."},
     *   {"temporada":2,"numeroEpisodio":1,"titulo":"Seven Thirty-Seven"},
     *   ...
     * ]
     */
    @GetMapping("/series/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id) {
        // @PathVariable: Extrai o {id} da URL e passa como parâmetro
        return servico.obterTodasTemporadas(id);
    }

    /**
     * Endpoint GET /series/{id}/temporadas/{numero}
     * 
     * Retorna episódios de UMA temporada específica de uma série.
     * 
     * @PathVariable: Captura DOIS parâmetros da URL
     * - {id}: ID da série
     * - {numero}: Número da temporada
     * 
     * EXEMPLOS DE URL:
     * - /series/7/temporadas/1 → id = 7, numero = 1 (Breaking Bad, temporada 1)
     * - /series/1/temporadas/2 → id = 1, numero = 2 (The Boys, temporada 2)
     * 
     * FLUXO:
     * 1. Cliente: GET http://localhost:8080/series/7/temporadas/1
     * 2. Controller recebe id = 7 e numero = 1 via @PathVariable
     * 3. Controller chama Service: servico.obterTemporadasPorNumero(7, 1)
     * 4. Service chama Repository: repository.obterEpisodiosPorTemporada(7, 1)
     * 5. Repository executa JPQL: SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = 7 AND e.temporada = 1
     * 6. Service converte Episodio → EpisodioDTO
     * 7. Controller retorna JSON para cliente
     * 
     * SQL GERADO:
     * SELECT e.* FROM series s
     * JOIN episodios e ON s.id = e.serie_id
     * WHERE s.id = 7 AND e.temporada = 1
     * 
     * POR QUE USAR JPQL?
     * - Busca direta no banco (mais rápido)
     * - Filtra por série E temporada em uma única query
     * - Não carrega todos os episódios da série
     * - Retorna apenas episódios da temporada solicitada
     * 
     * @param id ID da série (vem da URL)
     * @param numero Número da temporada (vem da URL)
     * @return Lista de EpisodioDTO da temporada
     * 
     * TESTE:
     * http://localhost:8080/series/7/temporadas/1 (Breaking Bad, temporada 1)
     * http://localhost:8080/series/1/temporadas/2 (The Boys, temporada 2)
     * http://localhost:8080/series/8/temporadas/3 (Game of Thrones, temporada 3)
     * 
     * RESPOSTA:
     * [
     *   {"temporada":1,"numeroEpisodio":1,"titulo":"Pilot"},
     *   {"temporada":1,"numeroEpisodio":2,"titulo":"Cat's in the Bag..."},
     *   {"temporada":1,"numeroEpisodio":3,"titulo":"...And the Bag's in the River"},
     *   ...
     * ]
     */
    @GetMapping("/series/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadaPorNumero(@PathVariable Long id, @PathVariable Long numero) {
        // @PathVariable: Extrai {id} e {numero} da URL e passa como parâmetros
        return servico.obterTemporadasPorNumero(id, numero);
    }

}
