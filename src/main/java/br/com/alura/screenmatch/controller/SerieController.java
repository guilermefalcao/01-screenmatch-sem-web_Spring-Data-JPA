package br.com.alura.screenmatch.controller;

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

}
