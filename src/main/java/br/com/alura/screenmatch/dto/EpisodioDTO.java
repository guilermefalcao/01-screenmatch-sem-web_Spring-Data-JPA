package br.com.alura.screenmatch.dto;

/**
 * DTO (Data Transfer Object) para Episódio
 * 
 * POR QUE CRIAR UM DTO PARA EPISÓDIO?
 * 
 * 1. CONTROLE DE DADOS EXPOSTOS:
 *    - Expõe apenas: temporada, numeroEpisodio, titulo
 *    - NÃO expõe: id, avaliacao, dataLancamento, serie (relacionamento)
 *    - Front-end recebe apenas o necessário
 * 
 * 2. EVITA LOOP INFINITO:
 *    - Episodio tem referência para Serie
 *    - Serie tem lista de Episodios
 *    - Sem DTO: JSON infinito (Serie → Episodio → Serie → ...)
 * 
 * 3. PERFORMANCE:
 *    - JSON menor (menos campos)
 *    - Transferência mais rápida
 * 
 * 4. DESACOPLAMENTO:
 *    - Se mudar entidade Episodio, DTO não muda
 *    - API permanece estável
 * 
 * RECORD vs CLASS:
 * - Record: Imutável, gera automaticamente construtor, getters, equals, hashCode, toString
 * - Ideal para DTOs (apenas transferência de dados)
 * 
 * EXEMPLO DE USO:
 * new EpisodioDTO(1, 1, "Pilot")
 * → {"temporada":1,"numeroEpisodio":1,"titulo":"Pilot"}
 * 
 * @param temporada Número da temporada (1, 2, 3...)
 * @param numeroEpisodio Número do episódio na temporada (1, 2, 3...)
 * @param titulo Título do episódio
 */
public record EpisodioDTO(
        Integer temporada,
        Integer numeroEpisodio,
        String titulo
) {
    // Record gera automaticamente:
    // - Construtor: new EpisodioDTO(1, 1, "Pilot")
    // - Getters: temporada(), numeroEpisodio(), titulo()
    // - equals(), hashCode(), toString()
    // - É IMUTÁVEL (sem setters)
}
