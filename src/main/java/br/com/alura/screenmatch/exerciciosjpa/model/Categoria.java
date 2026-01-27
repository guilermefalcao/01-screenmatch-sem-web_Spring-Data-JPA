package br.com.alura.screenmatch.exerciciosjpa.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// EXERCÍCIO 4: Classe Categoria como entidade JPA
@Entity
@Table(name = "categorias")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    // ========================================
    // EXERCÍCIO 1: RELACIONAMENTO UM-PARA-MUITOS
    // ========================================
    // @OneToMany: UMA categoria tem MUITOS produtos
    // mappedBy = "categoria": Relacionamento mapeado pelo atributo "categoria" em Produto
    // cascade = CascadeType.ALL: Ao salvar categoria, salva produtos automaticamente
    // fetch = FetchType.EAGER: Carrega produtos IMEDIATAMENTE (evita LazyInitializationException)
    // orphanRemoval = true: Remove produtos órfãos (sem categoria)
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Produto> produtos = new ArrayList<>();
    
    // Construtor padrão (obrigatório para JPA)
    public Categoria() {}
    
    // Construtor com parâmetros
    public Categoria(String nome) {
        this.nome = nome;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public List<Produto> getProdutos() {
        return produtos;
    }
    
    // Método auxiliar para adicionar produto e manter relacionamento bidirecional
    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        produto.setCategoria(this);  // Define a categoria no produto
    }
    
    @Override
    public String toString() {
        return "Categoria{id=" + id + ", nome='" + nome + "', produtos=" + produtos.size() + "}";
    }
}
