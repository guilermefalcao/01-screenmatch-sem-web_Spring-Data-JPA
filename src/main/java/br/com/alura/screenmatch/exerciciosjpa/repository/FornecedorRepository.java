package br.com.alura.screenmatch.exerciciosjpa.repository;

import br.com.alura.screenmatch.exerciciosjpa.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository para Fornecedor
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
}
