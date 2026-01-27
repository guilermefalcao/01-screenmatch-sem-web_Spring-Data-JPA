package br.com.alura.screenmatch.exerciciosjpa;

import br.com.alura.screenmatch.exerciciosjpa.model.Categoria;
import br.com.alura.screenmatch.exerciciosjpa.model.Fornecedor;
import br.com.alura.screenmatch.exerciciosjpa.model.Pedido;
import br.com.alura.screenmatch.exerciciosjpa.model.Produto;
import br.com.alura.screenmatch.exerciciosjpa.repository.CategoriaRepository;
import br.com.alura.screenmatch.exerciciosjpa.repository.FornecedorRepository;
import br.com.alura.screenmatch.exerciciosjpa.repository.PedidoRepository;
import br.com.alura.screenmatch.exerciciosjpa.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TesteExerciciosJPA {
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    public void executar() {
        System.out.println("\n========================================");
        System.out.println("EXERCÍCIOS JPA - RELACIONAMENTOS");
        System.out.println("========================================\n");
        
        // Limpar dados anteriores
        pedidoRepository.deleteAll();
        categoriaRepository.deleteAll();
        fornecedorRepository.deleteAll();
        System.out.println("🗑️  Dados anteriores removidos\n");
        
        // EXERCÍCIO 4: Criar Fornecedor
        Fornecedor fornecedor1 = new Fornecedor("Dell Inc.");
        Fornecedor fornecedor2 = new Fornecedor("Samsung Electronics");
        fornecedorRepository.save(fornecedor1);
        fornecedorRepository.save(fornecedor2);
        System.out.println("✅ Fornecedores salvos");
        
        // EXERCÍCIO 1 e 2: Categoria com Produtos (1:N bidirecional)
        Categoria eletronicos = new Categoria("Eletrônicos");
        Categoria informatica = new Categoria("Informática");
        
        Produto notebook = new Produto("Notebook Dell Inspiron", 3500.00);
        Produto mouse = new Produto("Mouse Logitech", 150.00);
        Produto monitor = new Produto("Monitor Samsung 24\"", 800.00);
        
        // EXERCÍCIO 5 e 6: Associar Fornecedor a Produto
        notebook.setFornecedor(fornecedor1);
        mouse.setFornecedor(fornecedor1);
        monitor.setFornecedor(fornecedor2);
        
        // Associar produtos à categoria
        eletronicos.adicionarProduto(notebook);
        eletronicos.adicionarProduto(monitor);
        informatica.adicionarProduto(mouse);
        
        // Salvar categoria (cascade salva produtos)
        categoriaRepository.save(eletronicos);
        categoriaRepository.save(informatica);
        System.out.println("✅ Categorias e Produtos salvos (cascade)");
        
        // EXERCÍCIO EXTRA: Pedido com Produtos (N:M)
        Pedido pedido1 = new Pedido(LocalDate.now());
        Pedido pedido2 = new Pedido(LocalDate.now().minusDays(1));
        
        pedido1.adicionarProduto(notebook);
        pedido1.adicionarProduto(mouse);
        pedido2.adicionarProduto(monitor);
        
        pedidoRepository.save(pedido1);
        pedidoRepository.save(pedido2);
        System.out.println("✅ Pedidos salvos com produtos associados");
        
        System.out.println("\n========================================");
        System.out.println("LISTANDO DADOS COM RELACIONAMENTOS");
        System.out.println("========================================\n");
        
        System.out.println("📂 CATEGORIAS COM PRODUTOS:");
        categoriaRepository.findAll().forEach(c -> {
            System.out.println(c);
            c.getProdutos().forEach(p -> System.out.println("  └─ " + p));
        });
        
        System.out.println("\n🛒 PEDIDOS COM PRODUTOS:");
        pedidoRepository.findAll().forEach(p -> {
            System.out.println(p);
            p.getProdutos().forEach(prod -> System.out.println("  └─ " + prod));
        });
        
        System.out.println("\n🏭 FORNECEDORES:");
        fornecedorRepository.findAll().forEach(System.out::println);
        
        System.out.println("\n========================================");
        System.out.println("✅ TESTES CONCLUÍDOS COM SUCESSO!");
        System.out.println("========================================\n");
    }
}
