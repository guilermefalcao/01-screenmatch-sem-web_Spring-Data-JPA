package br.com.alura.screenmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * VERSÃO WEB - API REST
 * 
 * Esta é a versão WEB do projeto Screenmatch.
 * Agora a aplicação funciona como uma API REST, não mais como console.
 * 
 * MUDANÇAS DA VERSÃO CONSOLE PARA WEB:
 * 
 * 1. REMOVIDO:
 *    - implements CommandLineRunner (não queremos executar código ao iniciar)
 *    - @Override run() (não há mais menu interativo no terminal)
 *    - @Autowired SerieRepository (agora vai nos Controllers)
 * 
 * 2. ADICIONADO (em outras classes):
 *    - Controllers com @RestController
 *    - Endpoints REST (@GetMapping, @PostMapping, etc.)
 *    - Servidor Tomcat embutido (via spring-boot-starter-web)
 * 
 * 3. COMPORTAMENTO:
 *    - Aplicação inicia e fica "no ar" na porta 8080
 *    - NÃO exibe menu no terminal
 *    - Aguarda requisições HTTP dos clientes
 *    - Acesso via navegador/Postman: http://localhost:8080/
 * 
 * EXEMPLO DE USO:
 * - GET http://localhost:8080/series -> Lista todas as séries
 * - GET http://localhost:8080/series/top5 -> Top 5 séries
 * - GET http://localhost:8080/series/1 -> Busca série por ID
 */
@SpringBootApplication  // DESCOMENTADO - Versão web ativa
public class ScreenmatchApplication {

	public static void main(String[] args) {
		// Inicia a aplicação Spring Boot
		// Sobe o servidor Tomcat na porta 8080
		// Fica aguardando requisições HTTP
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	// NÃO há mais método run() aqui!
	// A lógica agora está nos CONTROLLERS (SerieController, etc.)
}
