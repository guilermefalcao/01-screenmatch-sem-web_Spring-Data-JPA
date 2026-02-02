package br.com.alura.screenmatch;

import br.com.alura.screenmatch.exerciciosjpa.TesteExerciciosJPA;
import br.com.alura.screenmatch.principal.Principal;
import br.com.alura.screenmatch.repository.SerieRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BACKUP DA VERSÃO CONSOLE (SEM WEB)
 * 
 * Esta classe representa a versão ANTERIOR do projeto, quando era uma aplicação de linha de comando.
 * Foi mantida como backup para referência.
 * 
 * ATENÇÃO: @SpringBootApplication foi REMOVIDO para evitar conflito!
 * Para usar esta versão, você precisa:
 * 1. Descomentar @SpringBootApplication abaixo
 * 2. Comentar/remover a ScreenmatchApplication (versão web)
 * 3. Ou alterar o pom.xml para apontar para esta classe
 * 
 * DIFERENÇAS ENTRE VERSÃO CONSOLE E WEB:
 * 
 * 1. CONSOLE (esta classe - SemWeb):
 *    - Implementa CommandLineRunner
 *    - Executa o método run() automaticamente ao iniciar
 *    - Mostra menu interativo no terminal
 *    - Aplicação finaliza quando usuário escolhe "Sair"
 *    - Usa Scanner para ler entrada do usuário
 * 
 * 2. WEB (ScreenmatchApplication atual):
 *    - NÃO implementa CommandLineRunner
 *    - NÃO executa menu automaticamente
 *    - Servidor fica rodando esperando requisições HTTP
 *    - Usa Controllers (@RestController) para criar endpoints REST
 *    - Acesso via navegador/Postman: http://localhost:8080/series
 *    - Aplicação fica "no ar" até ser parada manualmente
 * 
 * QUANDO USAR CADA UMA:
 * - Console: Scripts, processamento batch, ferramentas CLI
 * - Web: APIs REST, aplicações web, microserviços
 */
// @SpringBootApplication // COMENTADO para não conflitar com a versão WEB
public class ScreenmatchApplicationSemWeb implements CommandLineRunner {
	
	// @Autowired: Injeção de dependência do Spring
	// O Spring cria automaticamente uma instância de SerieRepository e injeta aqui
	// Não precisamos fazer "new SerieRepository()" manualmente!
	@Autowired
	private SerieRepository repositorio;
	
	@Autowired
	private TesteExerciciosJPA testeExerciciosJPA;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplicationSemWeb.class, args);
	}

	// Método run() é executado automaticamente após a aplicação iniciar
	// Este é o ponto de entrada da aplicação CONSOLE
	@Override
	public void run(String... args) throws Exception {
		// Cria a classe Principal passando o repositório e o teste de exercícios
		// Isso permite que Principal acesse o banco de dados e execute os exercícios JPA
		Principal principal = new Principal(repositorio, testeExerciciosJPA);
		// Exibe o menu interativo no terminal
		principal.exibeMenu();
	}
}