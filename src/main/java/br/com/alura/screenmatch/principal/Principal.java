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

    // 🔒 SEGURANÇA: API Key agora vem da variável de ambiente OMDB_API_KEY
    // Se a variável não existir, usa uma string vazia (evita erro de compilação)
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
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar series buscadas

                    4 - Exercícios resolvidos
                    5 - Testar Exercícios JPA (Produto, Categoria, Pedido)

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
                    ListarSeriesBuscadas(); // aula 1 criado o metodo para listar series buscadas
                    break;
                case 4:
                    ExerciciosResolvidos.executarTodos();
                    break;
                case 5:
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

    private void buscarEpisodioPorSerie(){
        // 1. Lista as séries já salvas no banco
        ListarSeriesBuscadas();
        
        // 2. Solicita o nome da série para buscar episódios
        System.out.println("Digite o nome da série para busca de episódios:");
        var nomeSerie = leitura.nextLine();

        // 3. Busca a série no banco de dados (lista 'series' foi carregada no método anterior)
        // Optional: Pode conter um valor ou estar vazio (evita NullPointerException)
        Optional<Serie> serieBuscada = series.stream()
            .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
            .findFirst();

        // 4. Verifica se a série foi encontrada
        if(serieBuscada.isPresent()) {
            // 5. Obtém a série encontrada do Optional
            var serieEncontrada = serieBuscada.get();
            
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

}
