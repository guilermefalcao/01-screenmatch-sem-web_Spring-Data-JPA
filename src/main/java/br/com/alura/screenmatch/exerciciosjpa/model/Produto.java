package br.com.alura.screenmatch.exerciciosjpa.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// EXERCÍCIOS 1, 2 e 3: Classe Produto com mapeamento JPA completo
@Entity
@Table(name = "produtos")
public class Produto {
    
    // EXERCÍCIO 1: Chave primária com @Id
    // EXERCÍCIO 2: @GeneratedValue com IDENTITY (auto-increment do banco)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // EXERCÍCIO 3: @Column com unique=true (não permite duplicados) e nullable=false (obrigatório)
    @Column(unique = true, nullable = false)
    private String nome;
    
    // EXERCÍCIO 3: @Column com name="valor" (nome da coluna no banco será "valor" em vez de "preco")
    @Column(name = "valor")
    private Double preco;
    
    // ========================================
    // EXERCÍCIO 2: RELACIONAMENTO BIDIRECIONAL
    // ========================================
    // @ManyToOne: MUITOS produtos pertencem a UMA categoria
    // Cria coluna "categoria_id" na tabela produtos (chave estrangeira)
    @ManyToOne
    private Categoria categoria;
    
    // ========================================
    // EXERCÍCIO 5: RELACIONAMENTO UNIDIRECIONAL
    // ========================================
    // @ManyToOne: MUITOS produtos pertencem a UM fornecedor
    // Relacionamento unidirecional (só Produto conhece Fornecedor)
    // Cria coluna "fornecedor_id" na tabela produtos
    @ManyToOne
    private Fornecedor fornecedor;
    
    // ========================================
    // EXERCÍCIO EXTRA: RELACIONAMENTO MUITOS-PARA-MUITOS
    // ========================================
    // @ManyToMany: MUITOS produtos podem estar em MUITOS pedidos
    // @JoinTable: Define a tabela intermediária "pedido_produto"
    // joinColumns: Coluna que referencia Produto (produto_id)
    // inverseJoinColumns: Coluna que referencia Pedido (pedido_id)
    @ManyToMany
    @JoinTable(
        name = "pedido_produto",
        joinColumns = @JoinColumn(name = "produto_id"),
        inverseJoinColumns = @JoinColumn(name = "pedido_id")
    )
    private List<Pedido> pedidos = new ArrayList<>();
    
    // Construtor padrão (obrigatório para JPA)
    public Produto() {}
    
    // Construtor com parâmetros
    public Produto(String nome, Double preco) {
        this.nome = nome;
        this.preco = preco;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public Double getPreco() {
        return preco;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    
    public List<Pedido> getPedidos() {
        return pedidos;
    }
    
    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
    
    @Override
    public String toString() {
        return "Produto{id=" + id + ", nome='" + nome + "', preco=" + preco + 
               ", categoria=" + (categoria != null ? categoria.getNome() : "null") +
               ", fornecedor=" + (fornecedor != null ? fornecedor.getNome() : "null") + "}";
    }
}
