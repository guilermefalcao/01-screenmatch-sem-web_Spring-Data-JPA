package br.com.alura.screenmatch.exerciciosjpa.repository;

import br.com.alura.screenmatch.exerciciosjpa.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório JPA para a entidade Pedido
 * Extende JpaRepository para operações CRUD automáticas
 * 
 * DERIVED QUERY METHODS - EXERCÍCIOS AVANÇADOS
 * O Spring Data JPA cria automaticamente a implementação baseada no nome do método
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // ========================================
    // CONSULTAS DE DATA DE ENTREGA
    // ========================================
    
    /**
     * 6. Busca pedidos que ainda não possuem data de entrega
     * SQL: SELECT * FROM pedidos WHERE data_entrega IS NULL
     */
    List<Pedido> findByDataEntregaIsNull();
    
    /**
     * 7. Busca pedidos com data de entrega preenchida
     * SQL: SELECT * FROM pedidos WHERE data_entrega IS NOT NULL
     */
    List<Pedido> findByDataEntregaIsNotNull();
    
    // ========================================
    // CONSULTAS DE DATA DO PEDIDO
    // ========================================
    
    /**
     * 13. Busca pedidos feitos após uma data específica
     * SQL: SELECT * FROM pedidos WHERE data > ?
     */
    List<Pedido> findByDataAfter(LocalDate data);
    
    /**
     * 14. Busca pedidos feitos antes de uma data específica
     * SQL: SELECT * FROM pedidos WHERE data < ?
     */
    List<Pedido> findByDataBefore(LocalDate data);
    
    /**
     * 15. Busca pedidos feitos em um intervalo de datas
     * SQL: SELECT * FROM pedidos WHERE data BETWEEN ? AND ?
     */
    List<Pedido> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);
}
