package br.com.alura.screenmatch.exerciciosjpa.model;

import jakarta.persistence.*;

// EXERCÍCIO 4: Classe Fornecedor como entidade JPA
@Entity
@Table(name = "fornecedores")
public class Fornecedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    // Construtor padrão (obrigatório para JPA)
    public Fornecedor() {}
    
    // Construtor com parâmetros
    public Fornecedor(String nome) {
        this.nome = nome;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    @Override
    public String toString() {
        return "Fornecedor{id=" + id + ", nome='" + nome + "'}";
    }
}
