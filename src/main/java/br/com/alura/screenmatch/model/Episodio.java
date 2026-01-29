package br.com.alura.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.persistence.*;

// @Entity: Marca esta classe como uma ENTIDADE JPA (será mapeada para uma tabela no banco)
@Entity
// @Table: Define o nome da tabela no banco de dados
@Table(name = "episodios")
public class Episodio {

    // ========================================
    // ATRIBUTOS DA ENTIDADE
    // ========================================
    
    // @Id: Define este campo como CHAVE PRIMÁRIA da tabela
    @Id
    // @GeneratedValue: O banco gera o valor automaticamente (auto-increment)
    // IDENTITY: Usa a estratégia de auto-incremento do próprio banco (SERIAL no PostgreSQL)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate dataLancamento;

    // ========================================
    // RELACIONAMENTO MUITOS-PARA-UM (Many-to-One)
    // ========================================
    // @ManyToOne: Indica que MUITOS episódios pertencem a UMA série
    // Exemplo: 10 episódios (MANY) -> 1 série (ONE)
    // Isso cria uma coluna "serie_id" na tabela episodios (chave estrangeira)
    @ManyToOne
    private Serie serie;
    
    // ========================================
    // CONSTRUTORES
    // ========================================
    
    // Construtor padrão vazio (OBRIGATÓRIO para o JPA funcionar!)
    public Episodio() {}

    // Construtor com parâmetros (usado para criar episódio a partir dos dados da API)
    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numero();

        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (DateTimeParseException ex) {
            this.dataLancamento = null;
        }
    }

    // ========================================
    // GETTERS E SETTERS
    // ========================================
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "serie=" + (serie != null ? serie.getTitulo() : "N/A") +
                ", temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento ;
    }
}
