package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.exercicios.ExerciciosResolvidos;
import br.com.alura.screenmatch.exerciciosjpa.TesteExerciciosJPA;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    // 🔒 SEGURANÇA: API Key da variável de ambiente OMDB_API_KEY
    // Fallback temporário: Se não encontrar a variável, usa a chave do .env
    private final String API_KEY = "&apikey=" + (System.getenv("OMDB_API_KEY") != null ? System.getenv("OMDB_API_KEY") : "6585022c");

    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private List<Episodio> episodios = new ArrayList<>();

    private List<Serie> series = new ArrayList<>();

    // Repositório para acessar o banco de dados
    private SerieRepository repositorio;

    // Teste dos exercícios JPA
    private TesteExerciciosJPA testeExerciciosJPA;

    // Construtor que recebe o repositório por injeção de dependência
    // O Spring passa automaticamente o repositório quando cria esta classe
    public Principal(SerieRepository repositorio, TesteExerciciosJPA testeExerciciosJPA) {
        this.repositorio = repositorio;
        this.testeExerciciosJPA = testeExerciciosJPA;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao != 0) {

            var menu = """
                    
                    ==== MENU ====
                    
                    1 - Buscar séries
                    2 - Buscar episódios e salvar no banco
                    3 - Listar series buscadas
                    4 - Buscar série por titulo
                    5 - Buscar series por ator
                    6 - Top 5 series
                    7 - Limpar séries inválidas
                    
                    8 - Exercícios resolvidos
                    9 - Testar Exercícios JPA (Produto, Categoria, Pedido)

                    0 - Sair
                    
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    ListarSeriesBuscadas();
                    break;
                case 4:
                    buscarSerieporTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    limparSeriesInvalidas();
                    break;
                case 8:
                    ExerciciosResolvidos.executarTodos();
                    break;
                case 9:
                    testeExerciciosJPA.executar();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        // 1. Busca os dados da série na API OMDB
        DadosSerie dados = getDadosSerie();

        // 2. Converte os dados da API para um objeto Serie (entidade JPA)
        Serie serie = new Serie(dados);

        // 3. Salva a série no banco de dados usando o repositório
        // O método save() insere um novo registro ou atualiza se já existir
        repositorio.save(serie);

        // 4. Exibe os dados no console
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    /**
     * Método para buscar episódios de uma série
     * 
     * EVOLUÇÃO DO CÓDIGO:
     * ANTES (Aula 02 - início):
     *   - Buscava na lista em memória: series.stream().filter(...)
     *   - Problema: Lista pode estar desatualizada
     * 
     * AGORA (Aula 03 - Derived Query Methods):
     *   - Busca direto no banco: repositorio.findByTituloContainingIgnoreCase()
     *   - Vantagem: Sempre busca dados atualizados do banco
     *   - Mais eficiente: SQL otimizado pelo Spring Data JPA
     */
    private void buscarEpisodioPorSerie(){
        // 1. Lista as séries já salvas no banco (para o usuário visualizar)
        ListarSeriesBuscadas();
        
        // 2. Solicita o nome da série para buscar episódios
        System.out.println("Digite o nome da série para busca de episódios:");
        var nomeSerie = leitura.nextLine();

        // 3. NOVO: Busca a série DIRETO NO BANCO usando Derived Query Method
        // ANTES: Optional<Serie> serie = series.stream().filter(...).findFirst();
        // AGORA: Busca otimizada no banco de dados
        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        
        // Por que mudou?
        // - Busca direto no banco (sempre atualizado)
        // - Não depende da lista 'series' em memória
        // - SQL gerado: SELECT * FROM series WHERE LOWER(titulo) LIKE LOWER('%nomeSerie%')

        // 4. Verifica se a série foi encontrada
        if(serie.isPresent()) {
            // 5. Obtém a série encontrada do Optional
            var serieEncontrada = serie.get();
            
            // 6. Verifica se a série já tem episódios salvos
            if (!serieEncontrada.getEpisodios().isEmpty()) {
                System.out.println("⚠️  Esta série já possui " + serieEncontrada.getEpisodios().size() + " episódios salvos.");
                System.out.println("Deseja buscar novamente? Isso irá substituir os episódios existentes. (S/N)");
                var resposta = leitura.nextLine();
                if (!resposta.equalsIgnoreCase("S")) {
                    System.out.println("❌ Operação cancelada.");
                    return;
                }
                // Limpa os episódios antigos antes de buscar novos
                serieEncontrada.getEpisodios().clear();
            }
            
            // 7. Lista para armazenar dados de todas as temporadas
            List<DadosTemporada> temporadas = new ArrayList<>();

            // 8. Busca dados de cada temporada na API OMDB
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            // 9. Converte os dados das temporadas em objetos Episodio
            // flatMap: Achata a lista de listas em uma única lista
            // map: Transforma DadosEpisodio em Episodio
            // filter: Remove temporadas com dados nulos (erro da API)
            List<Episodio> episodios = temporadas.stream()
                .filter(t -> t.episodios() != null)  // Filtra temporadas válidas
                .flatMap(d -> d.episodios().stream()
                    .map(e -> new Episodio(d.numero(), e)))
                .collect(Collectors.toList());
            
            // Verifica se conseguiu buscar episódios
            if (episodios.isEmpty()) {
                System.out.println("❌ Erro: Não foi possível buscar episódios da API.");
                System.out.println("⚠️  Verifique se a API key está correta no arquivo .env");
                return;
            }
            
            // 10. Associa cada episódio à série (define o relacionamento)
            episodios.forEach(e -> e.setSerie(serieEncontrada));
            
            // 11. Define a lista de episódios na série
            serieEncontrada.setEpisodios(episodios);
            
            // 12. Salva a série com os episódios no banco
            // cascade = CascadeType.ALL: Salva automaticamente os episódios junto com a série
            repositorio.save(serieEncontrada);
            
            System.out.println("\n✅ Episódios salvos com sucesso! Total: " + episodios.size());

        } else {
            System.out.println("❌ Série não encontrada!");
        }
    }

    private void ListarSeriesBuscadas() {
        // 1. Cria uma lista vazia de objetos Serie
        series = repositorio.findAll(); // vai pegar no repositorio e trazer todos do banco

        // 2. Transforma a lista de DadosSerie em lista de Serie
        // series = dadosSeries.stream() // Cria um stream da lista dadosSeries
        // .map(d -> new Serie(d)) // Para cada DadosSerie (d), cria um novo objeto
        // Serie
        // .collect(Collectors.toList()); // Coleta todos os objetos Serie em uma lista

        // 3. Ordena e exibe as séries
        series.stream() // Cria um novo stream da lista series
                .sorted(Comparator.comparing(Serie::getGenero)) // Ordena por gênero (categoria)
                .forEach(System.out::println); // Imprime cada série no console
    }






    /**
     * Método para buscar série por título no banco de dados
     * Usa Derived Query Method do Spring Data JPA
     * 
     * Como funciona:
     * 1. Solicita nome da série ao usuário
     * 2. Busca no banco usando findByTituloContainingIgnoreCase()
     *    - Containing: Busca parcial (LIKE %nome%)
     *    - IgnoreCase: Ignora maiúsculas/minúsculas
     * 3. Retorna Optional<Serie> (pode estar vazio)
     * 4. Verifica se encontrou e exibe resultado
     * 
     * Exemplo SQL gerado:
     * SELECT * FROM series WHERE LOWER(titulo) LIKE LOWER('%nome%')
     */
    private void buscarSerieporTitulo(){
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();
        
        // Busca no banco usando método derivado do Spring Data JPA
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());
        } else {
            System.out.println("❌ Série não encontrada!");
        }
    }




    /**
     * Método para buscar séries por ator/atriz E avaliação mínima
     * Usa Derived Query Method COMPOSTO do Spring Data JPA
     * 
     * EVOLUÇÃO DO CÓDIGO:
     * ANTES: Buscava apenas por ator
     *   - findByAtoresContainingIgnoreCase(nomeAtor)
     * 
     * AGORA: Busca por ator E avaliação mínima
     *   - findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao)
     *   - And: Combina duas condições (WHERE ... AND ...)
     *   - GreaterThanEqual: Maior ou igual (>=)
     * 
     * Como funciona:
     * 1. Solicita nome do ator ao usuário
     * 2. Solicita avaliação mínima
     * 3. Converte String para Double
     * 4. Busca no banco com DUAS condições:
     *    - Atores contém o nome (case-insensitive)
     *    - Avaliação >= valor informado
     * 5. Exibe séries encontradas com avaliação
     * 
     * Exemplo SQL gerado:
     * SELECT * FROM series 
     * WHERE LOWER(atores) LIKE LOWER('%nomeAtor%') 
     * AND avaliacao >= 8.0
     * 
     * Exemplos de uso:
     * - Ator: "Karl", Avaliação: 8.0 → Encontra "The Boys" (8.7)
     * - Ator: "Jennifer", Avaliação: 9.0 → Não encontra nada (Friends tem 8.9)
     */
    private void buscarSeriesPorAtor() {
        System.out.println("Qual o nome do ator/atriz para busca: ");
        var nomeAtor = leitura.nextLine();

        System.out.println("Avaliações a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine(); // Limpa o buffer do scanner
        
        // Busca no banco usando Derived Query Method COMPOSTO
        // Combina duas condições: ator E avaliação mínima
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        
        // Verifica se encontrou séries
        if (seriesEncontradas.isEmpty()) {
            System.out.println("❌ Nenhuma série encontrada com o ator " + nomeAtor + " e avaliação >= " + avaliacao);
        } else {
            System.out.println("\n✅ Séries encontradas com " + nomeAtor + " e avaliação >= " + avaliacao + ":");
            seriesEncontradas.forEach(s -> 
                System.out.println("- " + s.getTitulo() + " (" + s.getGenero() + ") - Avaliação: " + s.getAvaliacao() + " - Atores: " + s.getAtores())
            );
            System.out.println(); // Linha em branco após resultado
        }
    }


    /**
     * Método para buscar Top 5 séries com melhor avaliação
     * Usa Derived Query Method com LIMIT e ORDER BY
     * 
     * Como funciona:
     * 1. Busca no banco usando findTop5ByOrderByAvaliacaoDesc()
     *    - findTop5: Limita resultado a 5 registros (LIMIT 5)
     *    - By: Separador
     *    - OrderBy: Ordenação
     *    - Avaliacao: Campo para ordenar
     *    - Desc: Ordem decrescente (maior para menor)
     * 2. Retorna List<Serie> com no máximo 5 séries
     * 3. Exibe título e avaliação de cada série
     * 
     * Exemplo SQL gerado:
     * SELECT * FROM series 
     * ORDER BY avaliacao DESC 
     * LIMIT 5
     * 
     * Exemplo de uso:
     * - Retorna as 5 séries com maior avaliação
     * - Útil para criar rankings
     * 
     * Variações:
     * - findTop10By... → Top 10
     * - findFirst3By... → Primeiros 3
     * - ...OrderByAvaliacaoAsc() → Ordem crescente (pior para melhor)
     */
    private void buscarTop5Series() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        System.out.println("\n🏆 Top 5 Séries:");
        seriesTop.forEach(s -> 
            System.out.println("- " + s.getTitulo() + " - Avaliação: " + s.getAvaliacao())
        );
        System.out.println();
    }

    /**
     * Método para limpar séries inválidas do banco
     * Remove séries com título nulo ou vazio
     */
    private void limparSeriesInvalidas() {
        List<Serie> todasSeries = repositorio.findAll();
        List<Serie> seriesInvalidas = todasSeries.stream()
            .filter(s -> s.getTitulo() == null || s.getTitulo().trim().isEmpty())
            .toList();
        
        if (seriesInvalidas.isEmpty()) {
            System.out.println("✅ Não há séries inválidas no banco.");
        } else {
            repositorio.deleteAll(seriesInvalidas);
            System.out.println("🗑️  " + seriesInvalidas.size() + " série(s) inválida(s) removida(s) do banco.");
        }
    }

}
