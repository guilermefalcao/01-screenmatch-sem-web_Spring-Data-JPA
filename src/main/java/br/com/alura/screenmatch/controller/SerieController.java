package br.com.alura.screenmatch.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * CONTROLLER REST - Camada de Apresentação
 * 
 * Esta classe é responsável por receber requisições HTTP e retornar respostas.
 * É a "porta de entrada" da aplicação web.
 * 
 * ANOTAÇÕES:
 * - @RestController: Combina @Controller + @ResponseBody
 *   - Indica que esta classe é um controller REST
 *   - Retorna dados diretamente (JSON/texto), não páginas HTML
 *   - Spring converte automaticamente objetos Java para JSON
 * 
 * - @GetMapping: Define um endpoint HTTP GET
 *   - Mapeia uma URL para um método Java
 *   - Exemplo: GET http://localhost:8080/series
 * 
 * - @RequestParam: Captura parâmetros da URL (query string)
 *   - Exemplo: /series?nomedaserie=Breaking+Bad
 *   - required = false: parâmetro é opcional
 * 
 * FLUXO DE REQUISIÇÃO:
 * 1. Cliente faz requisição: GET http://localhost:8080/series?nomedaserie=Lost
 * 2. Spring identifica o @GetMapping("/series")
 * 3. Spring chama o método obterSeries("Lost")
 * 4. Método retorna: "Série informada: Lost"
 * 5. Spring envia resposta HTTP 200 com o texto
 * 
 * EXEMPLOS DE USO:
 * - http://localhost:8080/series → "Nenhuma série informada"
 * - http://localhost:8080/series?nomedaserie=Lost → "Série informada: Lost"
 */
@RestController
public class SerieController {

    /**
     * Endpoint GET /series
     * 
     * Recebe um parâmetro opcional "nomedaserie" via query string.
     * 
     * @param nomedaserie Nome da série (opcional)
     * @return Mensagem com o nome da série ou aviso se não informado
     * 
     * TESTES:
     * - Navegador: http://localhost:8080/series
     * - Navegador: http://localhost:8080/series?nomedaserie=Breaking%20Bad
     * - Postman: GET http://localhost:8080/series?nomedaserie=Lost
     */
    @GetMapping("/series")
    public String obterSeries(@RequestParam(required = false) String nomedaserie) {
        // Se o parâmetro não foi enviado na URL
        if (nomedaserie == null) {
            return "Nenhuma série informada";
        }
        // Retorna o nome da série informada
        return "Série informada: " + nomedaserie;
    }

}
