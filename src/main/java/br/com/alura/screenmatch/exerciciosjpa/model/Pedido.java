package br.com.alura.screenmatch.exerciciosjpa.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// EXERCÍCIO 5: Classe Pedido como entidade JPA
@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate data;
    
    // ========================================
    // EXERCÍCIO EXTRA: RELACIONAMENTO MUITOS-PARA-MUITOS (lado inverso)
    // ========================================
    // @ManyToMany: MUITOS pedidos podem ter MUITOS produtos
    // mappedBy = "pedidos": Relacionamento mapeado pelo atributo "pedidos" em Produto
    // fetch = FetchType.EAGER: Carrega produtos IMEDIATAMENTE
    // Produto é o dono do relacionamento (tem @JoinTable)
    @ManyToMany(mappedBy = "pedidos", fetch = FetchType.EAGER)
    private List<Produto> produtos = new ArrayList<>();
    
    // Construtor padrão (obrigatório para JPA)
    public Pedido() {}
    
    // Construtor com parâmetros
    public Pedido(LocalDate data) {
        this.data = data;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public LocalDate getData() {
        return data;
    }
    
    public List<Produto> getProdutos() {
        return produtos;
    }
    
    // Método auxiliar para adicionar produto ao pedido
    public void adicionarProduto(Produto produto) {
        this.produtos.add(produto);
        produto.getPedidos().add(this);  // Mantém relacionamento bidirecional
    }
    
    @Override
    public String toString() {
        return "Pedido{id=" + id + ", data=" + data + ", produtos=" + produtos.size() + "}";
    }
}
