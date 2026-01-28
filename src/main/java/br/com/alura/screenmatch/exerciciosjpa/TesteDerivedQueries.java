package br.com.alura.screenmatch.exerciciosjpa;

import br.com.alura.screenmatch.exerciciosjpa.model.Categoria;
import br.com.alura.screenmatch.exerciciosjpa.model.Pedido;
import br.com.alura.screenmatch.exerciciosjpa.model.Produto;
import br.com.alura.screenmatch.exerciciosjpa.repository.CategoriaRepository;
import br.com.alura.screenmatch.exerciciosjpa.repository.PedidoRepository;
import br.com.alura.screenmatch.exerciciosjpa.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Classe para testar todas as Derived Query Methods implementadas
 * Demonstra os 17 exercícios de consultas avançadas com JPA
 */
@Component
public class TesteDerivedQueries {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Scanner scanner = new Scanner(System.in);

    public void executarTestes() {
        System.out.println("\n🧪 === TESTE DE DERIVED QUERIES ===");
        
        // Criar dados de teste se necessário
        criarDadosDeTeste();
        
        var opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1 -> testeConsultasBasicas();
                case 2 -> testeConsultasOrdenacao();
                case 3 -> testeConsultasContagem();
                case 4 -> testeConsultasCompostas();
                case 5 -> testeConsultasTopLimit();
                case 6 -> testeConsultasPedidos();
                case 0 -> System.out.println("Saindo dos testes...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("""
                
                === MENU DE TESTES ===
                
                1 - Consultas Básicas (1-5)
                2 - Consultas com Ordenação (8-9)
                3 - Consultas de Contagem (10-11)
                4 - Consultas Compostas (12)
                5 - Consultas Top/Limit (16-17)
                6 - Consultas de Pedidos (6-7, 13-15)
                
                0 - Voltar
                """);
    }

    private void testeConsultasBasicas() {
        System.out.println("\n📋 === CONSULTAS BÁSICAS ===");
        
        // 1. Buscar por nome exato
        System.out.println("1. Produtos com nome 'Notebook':");
        produtoRepository.findByNome("Notebook").forEach(System.out::println);
        
        // 2. Buscar por categoria
        System.out.println("\n2. Produtos da categoria 'Eletrônicos':");
        produtoRepository.findByCategoriaNome("Eletrônicos").forEach(System.out::println);
        
        // 3. Preço maior que
        System.out.println("\n3. Produtos com preço > 1000:");
        produtoRepository.findByPrecoGreaterThan(1000.0).forEach(System.out::println);
        
        // 4. Preço menor que
        System.out.println("\n4. Produtos com preço < 500:");
        produtoRepository.findByPrecoLessThan(500.0).forEach(System.out::println);
        
        // 5. Nome contendo termo
        System.out.println("\n5. Produtos com nome contendo 'book':");
        produtoRepository.findByNomeContaining("book").forEach(System.out::println);
    }

    private void testeConsultasOrdenacao() {
        System.out.println("\n📊 === CONSULTAS COM ORDENAÇÃO ===");
        
        // 8. Ordenação crescente por preço
        System.out.println("8. Eletrônicos ordenados por preço (crescente):");
        produtoRepository.findByCategoriaNomeOrderByPrecoAsc("Eletrônicos").forEach(System.out::println);
        
        // 9. Ordenação decrescente por preço
        System.out.println("\n9. Eletrônicos ordenados por preço (decrescente):");
        produtoRepository.findByCategoriaNomeOrderByPrecoDesc("Eletrônicos").forEach(System.out::println);
    }

    private void testeConsultasContagem() {
        System.out.println("\n🔢 === CONSULTAS DE CONTAGEM ===");
        
        // 10. Contar por categoria
        long countCategoria = produtoRepository.countByCategoriaNome("Eletrônicos");
        System.out.println("10. Total de produtos em 'Eletrônicos': " + countCategoria);
        
        // 11. Contar por preço
        long countPreco = produtoRepository.countByPrecoGreaterThan(1000.0);
        System.out.println("11. Total de produtos com preço > 1000: " + countPreco);
    }

    private void testeConsultasCompostas() {
        System.out.println("\n🔗 === CONSULTAS COMPOSTAS (OR) ===");
        
        // 12. Preço menor OU nome contendo
        System.out.println("12. Produtos com preço < 500 OU nome contendo 'Mouse':");
        produtoRepository.findByPrecoLessThanOrNomeContaining(500.0, "Mouse").forEach(System.out::println);
    }

    private void testeConsultasTopLimit() {
        System.out.println("\n🏆 === CONSULTAS TOP/LIMIT ===");
        
        // 16. Top 3 mais caros
        System.out.println("16. Top 3 produtos mais caros:");
        produtoRepository.findTop3ByOrderByPrecoDesc().forEach(System.out::println);
        
        // 17. Top 5 mais baratos de uma categoria
        System.out.println("\n17. Top 5 produtos mais baratos de 'Eletrônicos':");
        produtoRepository.findTop5ByCategoriaNomeOrderByPrecoAsc("Eletrônicos").forEach(System.out::println);
    }

    private void testeConsultasPedidos() {
        System.out.println("\n📦 === CONSULTAS DE PEDIDOS ===");
        
        // 6. Pedidos sem data de entrega
        System.out.println("6. Pedidos sem data de entrega:");
        pedidoRepository.findByDataEntregaIsNull().forEach(System.out::println);
        
        // 7. Pedidos com data de entrega
        System.out.println("\n7. Pedidos com data de entrega:");
        pedidoRepository.findByDataEntregaIsNotNull().forEach(System.out::println);
        
        // 13. Pedidos após data
        System.out.println("\n13. Pedidos após 2024-01-01:");
        pedidoRepository.findByDataAfter(LocalDate.of(2024, 1, 1)).forEach(System.out::println);
        
        // 14. Pedidos antes de data
        System.out.println("\n14. Pedidos antes de 2024-12-31:");
        pedidoRepository.findByDataBefore(LocalDate.of(2024, 12, 31)).forEach(System.out::println);
        
        // 15. Pedidos entre datas
        System.out.println("\n15. Pedidos entre 2024-01-01 e 2024-12-31:");
        pedidoRepository.findByDataBetween(
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 12, 31)
        ).forEach(System.out::println);
    }

    private void criarDadosDeTeste() {
        // Verifica se já existem dados
        if (produtoRepository.count() > 0) {
            return;
        }
        
        System.out.println("📝 Criando dados de teste...");
        
        // Criar categorias
        Categoria eletronicos = new Categoria("Eletrônicos");
        Categoria livros = new Categoria("Livros");
        categoriaRepository.save(eletronicos);
        categoriaRepository.save(livros);
        
        // Criar produtos
        Produto notebook = new Produto("Notebook", 2500.0, eletronicos);
        Produto mouse = new Produto("Mouse", 50.0, eletronicos);
        Produto teclado = new Produto("Teclado", 150.0, eletronicos);
        Produto monitor = new Produto("Monitor", 800.0, eletronicos);
        Produto livroJava = new Produto("Livro Java", 80.0, livros);
        
        produtoRepository.save(notebook);
        produtoRepository.save(mouse);
        produtoRepository.save(teclado);
        produtoRepository.save(monitor);
        produtoRepository.save(livroJava);
        
        // Criar pedidos
        Pedido pedido1 = new Pedido(LocalDate.of(2024, 6, 15));
        Pedido pedido2 = new Pedido(LocalDate.of(2024, 7, 20), LocalDate.of(2024, 7, 25));
        Pedido pedido3 = new Pedido(LocalDate.of(2024, 8, 10));
        
        pedidoRepository.save(pedido1);
        pedidoRepository.save(pedido2);
        pedidoRepository.save(pedido3);
        
        System.out.println("✅ Dados de teste criados!");
    }
}