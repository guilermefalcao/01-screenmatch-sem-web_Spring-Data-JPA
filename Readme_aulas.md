# 📚 Roteiro do Curso - Spring Data JPA

Guia passo a passo de tudo que foi implementado no curso.

---

## 🎯 AULA 01 - Modelando a Aplicação

### 1. Criação da Classe Serie
**Arquivo:** `model/Serie.java`

**O que faz:** Representa uma série de TV como um objeto Java

**Passos:**
1. Criar classe com atributos: titulo, totalTemporadas, avaliacao, genero, atores, poster, sinopse
2. Criar construtor que recebe `DadosSerie` (dados da API)
3. Converter avaliação usando `OptionalDouble` para tratar erros
4. Criar getters e setters para todos os atributos

**Conceitos aprendidos:**
- Modelagem de classes
- Conversão de tipos com Optional
- Encapsulamento

---

### 2. Criação do Enum Categoria
**Arquivo:** `model/Categoria.java`

**O que faz:** Define os gêneros de séries de forma tipada e segura

**Passos:**
1. Criar enum com valores: ACAO, ROMANCE, COMEDIA, DRAMA, CRIME, etc.
2. Adicionar atributos: categoriaOmdb (inglês) e categoriaPortugues
3. Criar método `fromString()` para converter String da API em enum
4. Criar getters para acessar os valores

**Conceitos aprendidos:**
- Enums com atributos
- Métodos em enums
- Conversão de String para enum

---

### 3. Integração com API de Tradução
**Arquivos:** `service/traducao/ConsultaMyMemory.java`, `DadosTraducao.java`, `DadosResposta.java`

**O que faz:** Traduz sinopses do inglês para português automaticamente

**Passos:**
1. Criar records `DadosTraducao` e `DadosResposta` para mapear JSON da API
2. Criar classe `ConsultaMyMemory` com método `obterTraducao()`
3. Usar `URLEncoder` para codificar o texto
4. Consumir API MyMemory (gratuita, 5000 caracteres/dia)
5. Processar resposta JSON com Jackson

**Conceitos aprendidos:**
- Consumo de APIs REST
- Processamento de JSON
- Records para DTOs

---

### 4. Menu Interativo
**Arquivo:** `principal/Principal.java`

**O que faz:** Menu com loop para buscar múltiplas séries

**Passos:**
1. Criar loop `while` que roda até usuário escolher sair
2. Usar `switch-case` para navegar entre opções
3. Métodos privados para cada funcionalidade (encapsulamento)
4. Scanner para ler entrada do usuário

**Conceitos aprendidos:**
- Loops e controle de fluxo
- Encapsulamento com métodos privados
- Interação com usuário

---

### 5. Exercícios Resolvidos
**Arquivos:** `exercicios/ExerciciosResolvidos.java`, `Mes.java`, `Moeda.java`, `CodigoErro.java`

**O que faz:** 8 exercícios sobre manipulação de dados e enums

**Exercícios:**
1. Converter lista de strings para números (ignorando inválidos)
2. Processar número em Optional
3. Obter primeiro e último nome
4. Verificar palíndromo
5. Converter emails para minúsculas
6. Enum Mes com dias do mês
7. Enum Moeda com conversão
8. Enum CodigoErro HTTP

**Conceitos aprendidos:**
- Streams e lambdas
- Optional
- Manipulação de Strings
- Enums avançados

---

## 🗄️ AULA 02 - Persistência de Dados com JPA

### 1. Configuração do Banco de Dados
**Arquivo:** `src/main/resources/application.properties`

**O que faz:** Configura conexão com PostgreSQL

**Passos:**
1. Adicionar dependências no `pom.xml`:
   - `spring-boot-starter-data-jpa`
   - `postgresql` (driver)
2. Criar arquivo `application.properties`
3. Configurar URL, usuário, senha e porta do banco
4. Configurar Hibernate (ddl-auto, show-sql, dialect)

**Configurações importantes:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/alura_series
spring.datasource.username=postgres
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update  # Cria/atualiza tabelas automaticamente
spring.jpa.show-sql=true              # Mostra SQL no console
```

**Tipos de Bancos de Dados:**
- **Relacionais (SQL):** PostgreSQL, MySQL, Oracle - Dados estruturados em tabelas com relacionamentos
- **NoSQL:** MongoDB (documentos), Redis (chave-valor), Cassandra (colunas) - Dados não estruturados
- **Por que PostgreSQL?** Open source, robusto, suporta JSON, ótimo para aplicações Spring

**Conceitos aprendidos:**
- Configuração de DataSource
- Hibernate DDL (create, update, validate)
- Dialetos SQL
- Diferença entre bancos relacionais e NoSQL

---

### 2. Transformar Serie em Entidade JPA
**Arquivo:** `model/Serie.java`

**O que faz:** Mapeia a classe Serie para uma tabela no banco

**Passos:**
1. Adicionar anotação `@Entity` na classe
2. Adicionar `@Table(name = "series")` para definir nome da tabela
3. Criar campo `id` com anotações:
   - `@Id` - Define como chave primária
   - `@GeneratedValue(strategy = GenerationType.IDENTITY)` - Auto-increment
4. Adicionar `@Column(unique = true)` no titulo
5. Adicionar `@Enumerated(EnumType.STRING)` no genero
6. Adicionar `@Transient` na lista de episódios (não persiste no banco)
7. Criar construtor padrão vazio (obrigatório para JPA)
8. Criar getters e setters para id e episodios

**Anotações JPA:**
- `@Entity` - Marca como entidade JPA
- `@Table` - Define nome da tabela
- `@Id` - Chave primária
- `@GeneratedValue` - Valor gerado automaticamente
- `@Column` - Configurações da coluna
- `@Enumerated` - Como salvar enum (STRING ou ORDINAL)
- `@Transient` - Campo não persistido

**Conceitos aprendidos:**
- Mapeamento objeto-relacional (ORM)
- Anotações JPA
- Estratégias de geração de ID

---

### 3. Criar Repository
**Arquivo:** `repository/SerieRepository.java`

**O que faz:** Interface para operações de banco de dados

**Passos:**
1. Criar interface que estende `JpaRepository<Serie, Long>`
2. Não precisa implementar nada! Spring cria automaticamente

**Métodos disponíveis automaticamente:**
- `save(serie)` - Salva ou atualiza
- `findById(id)` - Busca por ID
- `findAll()` - Lista todas
- `delete(serie)` - Remove
- `count()` - Conta registros

**Conceitos aprendidos:**
- Spring Data JPA
- Repositories
- Métodos CRUD automáticos

---

### 4. Injeção de Dependência
**Arquivos:** `ScreenmatchApplication.java` e `Principal.java`

**O que faz:** Conecta o repositório com a aplicação

**Passos em ScreenmatchApplication:**
1. Adicionar `@Autowired` no repositório
2. Passar repositório para Principal no método `run()`

**Passos em Principal:**
1. Criar atributo `SerieRepository repositorio`
2. Criar construtor que recebe o repositório
3. Usar `repositorio.save(serie)` para salvar no banco

**Conceitos aprendidos:**
- Injeção de dependência
- @Autowired
- Inversão de controle (IoC)

---

### 5. Salvar Série no Banco
**Arquivo:** `principal/Principal.java` - método `buscarSerieWeb()`

**O que faz:** Busca série na API e salva no banco

**Fluxo:**
1. Usuário digita nome da série
2. Busca dados na API OMDB
3. Converte `DadosSerie` para `Serie` (entidade)
4. Chama `repositorio.save(serie)` - salva no banco
5. Hibernate executa INSERT automaticamente

**SQL gerado automaticamente:**
```sql
INSERT INTO series (titulo, total_temporadas, avaliacao, genero, atores, poster, sinopse)
VALUES ('Friends', 10, 8.9, 'COMEDIA', 'Jennifer Aniston...', 'https://...', 'A vida...');
```

**Conceitos aprendidos:**
- Persistência de dados
- ORM em ação
- SQL gerado automaticamente

---

### 6. Verificar Dados no Banco
**Ferramenta:** DBeaver ou pgAdmin

**Comandos SQL:**
```sql
-- Ver todas as tabelas
SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';

-- Ver estrutura da tabela
SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'series';

-- Ver dados salvos
SELECT * FROM series;

-- Buscar por gênero
SELECT titulo, genero, avaliacao FROM series WHERE genero = 'COMEDIA';

-- Contar séries
SELECT COUNT(*) FROM series;
```

**Conceitos aprendidos:**
- Consultas SQL básicas
- Verificação de dados
- Estrutura de tabelas

---

## 📊 Estrutura do Banco de Dados

### Tabela: series

| Coluna | Tipo | Restrições |
|--------|------|------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| titulo | VARCHAR | UNIQUE, NOT NULL |
| total_temporadas | INTEGER | |
| avaliacao | DOUBLE | |
| genero | VARCHAR | (salva como texto: "ACAO", "COMEDIA") |
| atores | VARCHAR | |
| poster | VARCHAR | |
| sinopse | TEXT | |

---

## 🔄 Fluxo Completo da Aplicação

```
1. Usuário escolhe opção 1 (Buscar séries)
   ↓
2. Digite nome da série
   ↓
3. ConsumoApi busca na API OMDB
   ↓
4. ConverteDados converte JSON para DadosSerie
   ↓
5. ConsultaMyMemory traduz sinopse
   ↓
6. Cria objeto Serie (entidade JPA)
   ↓
7. repositorio.save(serie) salva no banco
   ↓
8. Hibernate gera e executa SQL INSERT
   ↓
9. Dados salvos no PostgreSQL
   ↓
10. Pode consultar no DBeaver: SELECT * FROM series;
```

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.1.1**
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados relacional
- **Hibernate** - ORM (implementação do JPA)
- **Jackson** - Processamento JSON
- **Maven** - Gerenciamento de dependências
- **API OMDB** - Busca de séries
- **API MyMemory** - Tradução gratuita

---

### 7. Segurança: Variáveis de Ambiente
**Arquivos:** `.env`, `.env.example`, `.gitignore`

**O que faz:** Protege credenciais sensíveis (senhas, API keys)

**Problema:** Credenciais hardcoded no código são expostas no Git
```java
// ❌ INSEGURO
private final String API_KEY = "&apikey=6585022c";
```

**Solução:** Usar variáveis de ambiente
```java
// ✅ SEGURO
private final String API_KEY = "&apikey=" + System.getenv("OMDB_API_KEY");
```

**Passos:**
1. Criar arquivo `.env` com credenciais reais (NÃO sobe no Git)
```properties
OMDB_API_KEY=6585022c
DB_URL=jdbc:postgresql://localhost:5433/alura_series
DB_USERNAME=postgres
DB_PASSWORD=1234
```

2. Criar `.env.example` como template público (sobe no Git)
```properties
OMDB_API_KEY=sua-chave-aqui
DB_PASSWORD=sua-senha-aqui
```

3. Adicionar `.env` no `.gitignore`
```
.env
.env.local
*.env
```

4. Usar variáveis no `application.properties`
```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5433/alura_series}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:1234}
```

5. Usar variáveis no código Java
```java
private final String API_KEY = "&apikey=" + System.getenv("OMDB_API_KEY");
```

**Sintaxe Spring:**
- `${VARIAVEL:valor_padrao}` - Busca variável de ambiente, se não encontrar usa valor padrão

**O que proteger:**
- ✅ API Keys (OMDB, OpenAI, AWS)
- ✅ Senhas de banco de dados
- ✅ Tokens de autenticação
- ✅ Chaves de criptografia
- ✅ Credenciais SMTP

**Conceitos aprendidos:**
- Variáveis de ambiente
- System.getenv()
- Segurança de credenciais
- .gitignore
- Boas práticas de segurança

---

## 📋 Resumo da Aula 02

### ✅ O que você aprendeu:

1. **Configurar ambiente PostgreSQL**
   - Instalação do banco de dados
   - Diferença entre bancos relacionais e NoSQL
   - Criação do banco `alura_series`

2. **Preparar aplicação para banco de dados**
   - Adicionar dependências JPA e PostgreSQL no `pom.xml`
   - Configurar `application.properties`

3. **Mapear entidades com Hibernate**
   - Anotações: @Entity, @Table, @Id, @GeneratedValue
   - @Column, @Enumerated, @Transient
   - Construtor padrão obrigatório

4. **Trabalhar com Repository**
   - Interface JpaRepository
   - Métodos CRUD automáticos
   - save(), findAll(), findById(), delete()

5. **Injeção de dependências**
   - @Autowired
   - Inversão de controle (IoC)
   - Classes gerenciadas pelo Spring

6. **Variáveis de ambiente**
   - Proteger credenciais sensíveis
   - Arquivo .env (não sobe no Git)
   - System.getenv() e ${VARIAVEL}
   - .gitignore para segurança

---

### 8. Exercícios Práticos JPA
**Pasta:** `exerciciosjpa/`

**O que faz:** Exercícios práticos para comparar funcionalidades da JPA

**Estrutura criada:**
```
exerciciosjpa/
├── model/
│   ├── Produto.java
│   ├── Categoria.java
│   └── Pedido.java
├── repository/
│   ├── ProdutoRepository.java
│   ├── CategoriaRepository.java
│   └── PedidoRepository.java
└── TesteExerciciosJPA.java
```

**Passos:**

1. **Criar entidades com diferentes configurações:**

**Produto.java** - Exercícios 1, 2 e 3:
```java
@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;
    
    @Column(unique = true, nullable = false)  // Único e obrigatório
    private String nome;
    
    @Column(name = "valor")  // Nome da coluna no banco
    private Double preco;
}
```

**Categoria.java** - Exercício 4:
```java
@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
}
```

**Pedido.java** - Exercício 5:
```java
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate data;  // JPA converte para DATE no PostgreSQL
}
```

2. **Criar repositórios** - Exercício 7:
```java
public interface ProdutoRepository extends JpaRepository<Produto, Long> {}
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}
public interface PedidoRepository extends JpaRepository<Pedido, Long> {}
```

3. **Criar classe de teste** - Exercício 8:
```java
@Component  // Marca como componente Spring (IMPORTANTE!)
public class TesteExerciciosJPA {
    
    @Autowired  // Injeção de dependência (OBRIGATÓRIO!)
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    public void executar() {
        // Criar e salvar objetos
        Produto produto = new Produto("Notebook Dell", 3500.00);
        produtoRepository.save(produto);
        
        Categoria categoria = new Categoria("Eletrônicos");
        categoriaRepository.save(categoria);
        
        Pedido pedido = new Pedido(LocalDate.now());
        pedidoRepository.save(pedido);
        
        // Listar todos
        produtoRepository.findAll().forEach(System.out::println);
        categoriaRepository.findAll().forEach(System.out::println);
        pedidoRepository.findAll().forEach(System.out::println);
    }
}
```

4. **Integrar ao menu principal:**

**ScreenmatchApplication.java:**
```java
@Autowired
private SerieRepository repositorio;

@Autowired
private TesteExerciciosJPA testeExerciciosJPA;  // Injetar teste

public void run(String... args) {
    Principal principal = new Principal(repositorio, testeExerciciosJPA);
    principal.exibeMenu();
}
```

**Principal.java:**
```java
private TesteExerciciosJPA testeExerciciosJPA;

public Principal(SerieRepository repositorio, TesteExerciciosJPA testeExerciciosJPA) {
    this.repositorio = repositorio;
    this.testeExerciciosJPA = testeExerciciosJPA;
}

// Adicionar opção 5 no menu
case 5:
    testeExerciciosJPA.executar();
    break;
```

**Conceitos aprendidos:**
- Parâmetros de @Column (unique, nullable, name)
- GenerationType.IDENTITY vs AUTO vs SEQUENCE
- LocalDate para datas
- @Component para classes de teste
- Múltiplos repositórios na mesma aplicação
- Injeção de dependência múltipla

**Como testar:**
1. Execute a aplicação
2. Escolha opção **5** no menu
3. Veja dados sendo salvos no console
4. Verifique no DBeaver:
```sql
SELECT * FROM produtos;
SELECT * FROM categorias;
SELECT * FROM pedidos;
```

**Documentação completa:** `exerciciosjpa/README_EXERCICIOS_JPA.md`

---

### 9. Relacionamentos JPA: @OneToMany e @ManyToOne
**Arquivos:** `model/Serie.java`, `model/Episodio.java`, `principal/Principal.java`

**O que faz:** Cria relacionamento bidirecional entre Série e Episódios

**Relacionamento:**
- UMA série tem MUITOS episódios (@OneToMany)
- MUITOS episódios pertencem a UMA série (@ManyToOne)

**Passos:**

1. **Transformar Episodio em entidade JPA:**
```java
@Entity
@Table(name = "episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate dataLancamento;
    
    // @ManyToOne: MUITOS episódios pertencem a UMA série
    // Cria coluna "serie_id" na tabela episodios (chave estrangeira)
    @ManyToOne
    private Serie serie;
    
    // Construtor padrão obrigatório para JPA
    public Episodio() {}
}
```

2. **Adicionar relacionamento em Serie:**
```java
@Entity
@Table(name = "series")
public class Serie {
    // ... outros atributos
    
    // @OneToMany: UMA série tem MUITOS episódios
    // mappedBy = "serie": Relacionamento mapeado pelo atributo "serie" em Episodio
    // cascade = CascadeType.ALL: Operações na série afetam episódios (salvar, deletar)
    // fetch = FetchType.EAGER: Carrega episódios IMEDIATAMENTE junto com a série
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();
    
    // Setter com manipulação de chave estrangeira
    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));  // Associa série a cada episódio
        this.episodios = episodios;
    }
}
```

3. **Criar método para buscar e salvar episódios:**
```java
private void buscarEpisodioPorSerie() {
    // 1. Lista séries do banco
    ListarSeriesBuscadas();
    
    // 2. Busca série escolhida
    Optional<Serie> serieBuscada = series.stream()
        .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
        .findFirst();
    
    // 3. Verifica se já tem episódios (evita duplicação)
    if (!serieEncontrada.getEpisodios().isEmpty()) {
        System.out.println("⚠️  Esta série já possui episódios salvos.");
        // Pergunta se deseja substituir
    }
    
    // 4. Busca episódios na API OMDB
    for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
        // Busca cada temporada
    }
    
    // 5. Converte para objetos Episodio (filtra dados nulos da API)
    List<Episodio> episodios = temporadas.stream()
        .filter(t -> t.episodios() != null)  // Filtra temporadas inválidas
        .flatMap(d -> d.episodios().stream()
            .map(e -> new Episodio(d.numero(), e)))
        .collect(Collectors.toList());
    
    // 6. Define lista de episódios na série (setter associa automaticamente)
    serieEncontrada.setEpisodios(episodios);
    
    // 7. Salva série (cascade salva episódios automaticamente)
    repositorio.save(serieEncontrada);
}
```

**Estrutura no banco:**
```
Tabela: series
- id (PK)
- titulo
- total_temporadas
- ...

Tabela: episodios
- id (PK)
- temporada
- titulo
- numero_episodio
- avaliacao
- data_lancamento
- serie_id (FK) → series.id
```

**Verificar no DBeaver:**
```sql
-- Ver episódios com série
SELECT 
    s.titulo AS serie,
    e.temporada,
    e.numero_episodio,
    e.titulo AS episodio,
    e.avaliacao
FROM series s
JOIN episodios e ON s.id = e.serie_id
WHERE s.titulo = 'The Boys'
ORDER BY e.temporada, e.numero_episodio;

-- Contar episódios por série
SELECT 
    s.titulo,
    COUNT(e.id) AS total_episodios
FROM series s
LEFT JOIN episodios e ON s.id = e.serie_id
GROUP BY s.titulo;
```

**Conceitos aprendidos:**
- Relacionamento bidirecional (@OneToMany + @ManyToOne)
- Chave estrangeira (Foreign Key)
- cascade = CascadeType.ALL (persistência em cascata)
- fetch = FetchType.EAGER vs LAZY
- mappedBy (lado não-dono do relacionamento)
- Manipulação de chave estrangeira no setter
- Evitar duplicação de dados
- Filtrar dados nulos da API
- JOIN entre tabelas

---

### 10. Exercícios Avançados: Relacionamentos JPA
**Pasta:** `exerciciosjpa/`

**O que faz:** Implementa 3 tipos de relacionamentos entre entidades

**Relacionamentos implementados:**

#### 1. @OneToMany Bidirecional (Categoria → Produto)
**Categoria.java:**
```java
@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    // UMA categoria tem MUITOS produtos
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, 
               fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Produto> produtos = new ArrayList<>();
    
    // Método auxiliar para manter relacionamento bidirecional
    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        produto.setCategoria(this);  // Associa categoria ao produto
    }
}
```

**Produto.java:**
```java
@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String nome;
    
    @Column(name = "valor")
    private Double preco;
    
    // MUITOS produtos pertencem a UMA categoria
    @ManyToOne
    private Categoria categoria;
}
```

**Resultado no banco:**
- Tabela `produtos` ganha coluna `categoria_id` (FK → categorias.id)
- Salvar Categoria com cascade salva todos os Produtos automaticamente

---

#### 2. @ManyToOne Unidirecional (Produto → Fornecedor)
**Fornecedor.java:**
```java
@Entity
@Table(name = "fornecedores")
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
}
```

**Produto.java (adicionar):**
```java
@ManyToOne
private Fornecedor fornecedor;  // MUITOS produtos de UM fornecedor
```

**Resultado no banco:**
- Tabela `produtos` ganha coluna `fornecedor_id` (FK → fornecedores.id)
- Relacionamento unidirecional: Produto conhece Fornecedor, mas Fornecedor não conhece Produtos

---

#### 3. @ManyToMany com Tabela Intermediária (Produto ↔ Pedido)
**Produto.java (adicionar):**
```java
// MUITOS produtos em MUITOS pedidos
@ManyToMany
@JoinTable(
    name = "pedido_produto",  // Nome da tabela intermediária
    joinColumns = @JoinColumn(name = "produto_id"),  // FK para produtos
    inverseJoinColumns = @JoinColumn(name = "pedido_id")  // FK para pedidos
)
private List<Pedido> pedidos = new ArrayList<>();
```

**Pedido.java:**
```java
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate data;
    
    // MUITOS pedidos têm MUITOS produtos
    @ManyToMany(mappedBy = "pedidos", fetch = FetchType.EAGER)
    private List<Produto> produtos = new ArrayList<>();
    
    // Método auxiliar para relacionamento bidirecional
    public void adicionarProduto(Produto produto) {
        this.produtos.add(produto);
        produto.getPedidos().add(this);
    }
}
```

**Resultado no banco:**
- Cria tabela intermediária `pedido_produto` com:
  - `produto_id` (FK → produtos.id)
  - `pedido_id` (FK → pedidos.id)
  - Chave primária composta (produto_id, pedido_id)

---

**Teste completo (TesteExerciciosJPA.java):**
```java
@Component
public class TesteExerciciosJPA {
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private FornecedorRepository fornecedorRepository;
    
    public void executar() {
        // Limpar dados anteriores (evita erro de constraint unique)
        pedidoRepository.deleteAll();
        categoriaRepository.deleteAll();
        fornecedorRepository.deleteAll();
        
        // 1. Criar fornecedores
        Fornecedor dell = new Fornecedor("Dell Inc.");
        Fornecedor samsung = new Fornecedor("Samsung Electronics");
        fornecedorRepository.save(dell);
        fornecedorRepository.save(samsung);
        
        // 2. Criar categorias e produtos (1:N bidirecional)
        Categoria eletronicos = new Categoria("Eletrônicos");
        Produto notebook = new Produto("Notebook Dell Inspiron", 3500.00);
        Produto monitor = new Produto("Monitor Samsung 24\"", 800.00);
        
        // 3. Associar fornecedor (N:1 unidirecional)
        notebook.setFornecedor(dell);
        monitor.setFornecedor(samsung);
        
        // 4. Associar categoria (método auxiliar mantém bidirecionalidade)
        eletronicos.adicionarProduto(notebook);
        eletronicos.adicionarProduto(monitor);
        
        // 5. Salvar categoria (cascade salva produtos)
        categoriaRepository.save(eletronicos);
        
        // 6. Criar pedidos com produtos (N:M)
        Pedido pedido1 = new Pedido(LocalDate.now());
        pedido1.adicionarProduto(notebook);
        pedido1.adicionarProduto(monitor);
        pedidoRepository.save(pedido1);
        
        // 7. Listar dados com relacionamentos
        categoriaRepository.findAll().forEach(c -> {
            System.out.println(c);
            c.getProdutos().forEach(p -> System.out.println("  └─ " + p));
        });
    }
}
```

**Verificar no DBeaver:**
```sql
-- Ver produtos com todos os relacionamentos
SELECT 
    p.nome AS produto,
    p.valor,
    c.nome AS categoria,
    f.nome AS fornecedor
FROM produtos p
LEFT JOIN categorias c ON p.categoria_id = c.id
LEFT JOIN fornecedores f ON p.fornecedor_id = f.id;

-- Ver tabela intermediária pedido_produto
SELECT * FROM pedido_produto;

-- Ver pedidos com produtos
SELECT 
    ped.id AS pedido,
    ped.data,
    p.nome AS produto,
    p.valor
FROM pedidos ped
JOIN pedido_produto pp ON ped.id = pp.pedido_id
JOIN produtos p ON pp.produto_id = p.produto_id
ORDER BY ped.id;
```

**Conceitos aprendidos:**
- @OneToMany bidirecional com cascade e orphanRemoval
- @ManyToOne unidirecional (sem lista no lado "um")
- @ManyToMany com @JoinTable
- Métodos auxiliares para manter relacionamentos bidirecionais
- fetch = FetchType.EAGER para evitar LazyInitializationException
- deleteAll() para limpar dados e evitar constraint unique
- Chave primária composta em tabela intermediária

---

## 📊 Resumo dos Relacionamentos JPA

| Tipo | Anotação | Exemplo | Chave Estrangeira | Tabela Intermediária |
|------|----------|---------|-------------------|----------------------|
| 1:N Bidirecional | @OneToMany + @ManyToOne | Categoria → Produtos | No lado "muitos" (produtos.categoria_id) | Não |
| N:1 Unidirecional | @ManyToOne | Produto → Fornecedor | No lado "muitos" (produtos.fornecedor_id) | Não |
| N:M Bidirecional | @ManyToMany + @JoinTable | Produto ↔ Pedido | Não | Sim (pedido_produto) |
| 1:1 | @OneToOne | Usuário → Perfil | Em qualquer lado | Não |

**Atributos importantes:**
- `mappedBy`: Indica lado não-dono do relacionamento bidirecional
- `cascade`: Propaga operações (ALL, PERSIST, REMOVE, MERGE, REFRESH)
- `fetch`: EAGER (carrega imediatamente) ou LAZY (carrega sob demanda)
- `orphanRemoval`: Remove entidades órfãs (sem pai)

---

## 📝 Próximas Aulas

- [ ] Consultas personalizadas com JPQL
- [ ] Queries nativas com @Query
- [ ] Paginação e ordenação
- [ ] Projeções e DTOs

---

## 🎯 AULA 03 - Derived Query Methods

### O que são Derived Query Methods?

São métodos que o **Spring Data JPA cria automaticamente** baseado no **nome do método**.

Você escreve o nome do método seguindo uma convenção, e o Spring gera o SQL automaticamente!

**Exemplo:**
```java
// Você escreve:
Optional<Serie> findByTituloContainingIgnoreCase(String titulo);

// Spring gera automaticamente:
SELECT * FROM series WHERE LOWER(titulo) LIKE LOWER('%titulo%');
```

**Vantagens:**
- ✅ Não precisa escrever SQL
- ✅ Type-safe (erros em tempo de compilação)
- ✅ SQL otimizado automaticamente
- ✅ Código limpo e legível

---

### 1. Busca por Título (Opção 4)
**Arquivo:** `repository/SerieRepository.java`

**O que faz:** Busca série por título (busca parcial, case-insensitive)

**Passos:**

1. **Adicionar método no repositório:**
```java
public interface SerieRepository extends JpaRepository<Serie, Long> {
    
    // Busca por título (parcial, case-insensitive)
    // findBy: Inicia query
    // Titulo: Campo da entidade Serie
    // Containing: LIKE %valor%
    // IgnoreCase: LOWER() no SQL
    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);
}
```

**SQL gerado automaticamente:**
```sql
SELECT * FROM series 
WHERE LOWER(titulo) LIKE LOWER('%boys%');
```

2. **Usar no menu (Principal.java):**
```java
private void buscarSerieporTitulo() {
    System.out.println("Escolha uma serie pelo nome: ");
    var nomeSerie = leitura.nextLine();
    
    // Busca no banco usando Derived Query Method
    Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

    if (serieBuscada.isPresent()) {
        System.out.println("✅ Dados da série: " + serieBuscada.get());
    } else {
        System.out.println("❌ Série não encontrada!");
    }
}
```

**Características:**
- ✅ Busca **parcial**: "boys" encontra "The Boys"
- ✅ **Case-insensitive**: "BOYS", "boys", "Boys" funcionam igual
- ✅ Retorna `Optional<Serie>` (pode estar vazio)
- ✅ Busca **apenas no banco** (não usa API)

**Conceitos aprendidos:**
- Derived Query Methods
- Nomenclatura: findBy + Campo + Containing + IgnoreCase
- Optional para tratar resultado vazio
- Busca parcial com LIKE

---

### 2. Otimização: Busca de Episódios (Opção 2)
**Arquivo:** `principal/Principal.java` - método `buscarEpisodioPorSerie()`

**O que mudou:** Substituiu busca em memória por busca no banco

**ANTES (Aula 02):**
```java
// Buscava na lista em memória
Optional<Serie> serie = series.stream()
    .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
    .findFirst();
```

**Problemas:**
- ❌ Dependia da lista `series` em memória
- ❌ Lista podia estar desatualizada
- ❌ Menos eficiente (itera toda a lista)

**AGORA (Aula 03):**
```java
// Busca direto no banco usando Derived Query Method
Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
```

**Vantagens:**
- ✅ Busca direto no banco (sempre atualizado)
- ✅ SQL otimizado pelo Spring Data JPA
- ✅ Não depende de lista em memória
- ✅ Mais eficiente (usa índice do banco)

**Conceitos aprendidos:**
- Otimização: banco vs memória
- Reutilização de Derived Query Methods
- Consistência de dados

---

### 3. Busca por Ator e Avaliação Mínima (Opção 5)
**Arquivo:** `repository/SerieRepository.java`

**O que faz:** Busca séries com ator específico E avaliação mínima

**Passos:**

1. **Adicionar método COMPOSTO no repositório:**
```java
public interface SerieRepository extends JpaRepository<Serie, Long> {
    
    // Busca por ator E avaliação mínima (query composta)
    // findBy: Inicia query
    // Atores: Campo da entidade
    // Containing: LIKE %valor%
    // IgnoreCase: LOWER()
    // And: Combina condições (WHERE ... AND ...)
    // Avaliacao: Campo da entidade
    // GreaterThanEqual: >= (maior ou igual)
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(
        String nomeAtor, 
        Double avaliacao
    );
}
```

**SQL gerado automaticamente:**
```sql
SELECT * FROM series 
WHERE LOWER(atores) LIKE LOWER('%karl%') 
AND avaliacao >= 8.0;
```

2. **Usar no menu (Principal.java):**
```java
private void buscarSeriesPorAtor() {
    System.out.println("Qual o nome do ator/atriz para busca: ");
    var nomeAtor = leitura.nextLine();

    System.out.println("Avaliações a partir de que valor? ");
    var avaliacao = leitura.nextDouble();
    leitura.nextLine(); // Limpa buffer do scanner
    
    // Busca no banco com DUAS condições (AND)
    List<Serie> seriesEncontradas = repositorio
        .findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
    
    if (seriesEncontradas.isEmpty()) {
        System.out.println("❌ Nenhuma série encontrada");
    } else {
        System.out.println("\n✅ Séries encontradas:");
        seriesEncontradas.forEach(s -> 
            System.out.println("- " + s.getTitulo() + " - Avaliação: " + s.getAvaliacao())
        );
    }
}
```

**Palavras-chave para queries compostas:**
- `And` → WHERE campo1 = ? AND campo2 = ?
- `Or` → WHERE campo1 = ? OR campo2 = ?
- `Between` → WHERE campo BETWEEN ? AND ?
- `LessThan` → WHERE campo < ?
- `GreaterThan` → WHERE campo > ?
- `LessThanEqual` → WHERE campo <= ?
- `GreaterThanEqual` → WHERE campo >= ?

**Conceitos aprendidos:**
- Queries compostas com AND
- Múltiplos parâmetros
- Comparações numéricas (>=, <=, >, <)
- Combinação de Containing + GreaterThanEqual

---

### 4. Top 5 Séries (Opção 6)
**Arquivo:** `repository/SerieRepository.java`

**O que faz:** Busca as 5 séries com melhor avaliação

**Passos:**

1. **Adicionar método com LIMIT e ORDER BY:**
```java
public interface SerieRepository extends JpaRepository<Serie, Long> {
    
    // Top 5 séries por avaliação
    // findTop5: Limita resultado a 5 registros (LIMIT 5)
    // By: Separador
    // OrderBy: Ordenação
    // Avaliacao: Campo para ordenar
    // Desc: Ordem decrescente (maior para menor)
    List<Serie> findTop5ByOrderByAvaliacaoDesc();
}
```

**SQL gerado automaticamente:**
```sql
SELECT * FROM series 
ORDER BY avaliacao DESC 
LIMIT 5;
```

2. **Usar no menu (Principal.java):**
```java
private void buscarTop5Series() {
    List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
    System.out.println("\n🏆 Top 5 Séries:");
    seriesTop.forEach(s -> 
        System.out.println("- " + s.getTitulo() + " - Avaliação: " + s.getAvaliacao())
    );
}
```

**Variações:**
- `findTop10By...` → Top 10
- `findFirst3By...` → Primeiros 3
- `...OrderByAvaliacaoAsc()` → Ordem crescente (pior para melhor)
- `...OrderByTituloAsc()` → Ordena por título (A-Z)

**Conceitos aprendidos:**
- Top N queries (LIMIT)
- Ordenação (ORDER BY)
- Desc vs Asc
- Rankings e listas top

---

### 5. Tratamento de Dados Nulos da API
**Arquivo:** `model/Serie.java` - construtor

**Problema:** API OMDB pode retornar campos nulos (avaliação, gênero, sinopse)

**Erros comuns:**
```
Cannot invoke String.split() because return value is null
Cannot invoke String.trim() because "in" is null
```

**Solução: Verificar nulls antes de processar**

```java
public Serie(DadosSerie dadosSerie) {
    this.titulo = dadosSerie.titulo();
    this.totalTemporadas = dadosSerie.totalTemporadas();
    
    // ✅ TRATAMENTO DE AVALIAÇÃO NULA
    if (dadosSerie.avaliacao() != null && 
        !dadosSerie.avaliacao().isEmpty() && 
        !dadosSerie.avaliacao().equalsIgnoreCase("N/A")) {
        this.avaliacao = Double.valueOf(dadosSerie.avaliacao());
    } else {
        this.avaliacao = 0.0;  // Valor padrão
    }
    
    // ✅ TRATAMENTO DE GÊNERO NULO
    if (dadosSerie.genero() != null && !dadosSerie.genero().isEmpty()) {
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
    } else {
        this.genero = Categoria.ACAO;  // Categoria padrão
    }
    
    // ✅ TRATAMENTO DE SINOPSE NULA
    if (dadosSerie.sinopse() != null && !dadosSerie.sinopse().isEmpty()) {
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse()).trim();
    } else {
        this.sinopse = "Sinopse não disponível";
    }
    
    this.atores = dadosSerie.atores();
    this.poster = dadosSerie.poster();
}
```

**Conceitos aprendidos:**
- Validação de nulls
- Valores padrão (fallback)
- Tratamento de erros da API
- Robustez do código

---

### 6. Limpeza de Séries Inválidas (Opção 7)
**Arquivo:** `principal/Principal.java`

**O que faz:** Remove séries com título nulo ou vazio do banco

**Problema:** Quando API retorna dados inválidos, séries com nulls são salvas

**Solução:**

```java
private void limparSeriesInvalidas() {
    // 1. Busca todas as séries do banco
    List<Serie> todasSeries = repositorio.findAll();
    
    // 2. Filtra séries inválidas (título nulo ou vazio)
    List<Serie> seriesInvalidas = todasSeries.stream()
        .filter(s -> s.getTitulo() == null || s.getTitulo().trim().isEmpty())
        .toList();
    
    // 3. Verifica se há séries inválidas
    if (seriesInvalidas.isEmpty()) {
        System.out.println("✅ Não há séries inválidas no banco.");
    } else {
        // 4. Remove séries inválidas
        repositorio.deleteAll(seriesInvalidas);
        System.out.println("🗑️  " + seriesInvalidas.size() + " série(s) inválida(s) removida(s).");
    }
}
```

**SQL gerado:**
```sql
-- Busca séries inválidas
SELECT * FROM series WHERE titulo IS NULL OR titulo = '';

-- Remove séries inválidas
DELETE FROM series WHERE id IN (3, 4);
```

**Conceitos aprendidos:**
- deleteAll() com lista filtrada
- Stream filter para validação
- Limpeza de dados inconsistentes
- Manutenção do banco de dados

---

## 📊 Tabela de Derived Query Methods

| Método | SQL Gerado | Uso |
|--------|------------|-----|
| findByTitulo(String) | WHERE titulo = ? | Busca exata |
| findByTituloContaining(String) | WHERE titulo LIKE %?% | Busca parcial |
| findByTituloIgnoreCase(String) | WHERE LOWER(titulo) = LOWER(?) | Case-insensitive |
| findByTituloContainingIgnoreCase(String) | WHERE LOWER(titulo) LIKE LOWER(%?%) | Parcial + case-insensitive |
| findByAvaliacaoGreaterThan(Double) | WHERE avaliacao > ? | Maior que |
| findByAvaliacaoGreaterThanEqual(Double) | WHERE avaliacao >= ? | Maior ou igual |
| findByAvaliacaoLessThan(Double) | WHERE avaliacao < ? | Menor que |
| findByAvaliacaoBetween(Double, Double) | WHERE avaliacao BETWEEN ? AND ? | Entre valores |
| findByGenero(Categoria) | WHERE genero = ? | Enum |
| findByAtoresContainingAndAvaliacaoGreaterThan | WHERE atores LIKE %?% AND avaliacao > ? | Múltiplas condições |
| findTop5ByOrderByAvaliacaoDesc() | ORDER BY avaliacao DESC LIMIT 5 | Top N |
| findByTituloOrderByAvaliacaoDesc(String) | WHERE titulo = ? ORDER BY avaliacao DESC | Busca + ordenação |

---

## 🔍 Verificar no DBeaver

### Queries úteis após Aula 03:

```sql
-- Ver todas as séries
SELECT * FROM series ORDER BY avaliacao DESC;

-- Buscar por título (como opção 4)
SELECT * FROM series WHERE LOWER(titulo) LIKE LOWER('%boys%');

-- Buscar por ator e avaliação (como opção 5)
SELECT * FROM series 
WHERE LOWER(atores) LIKE LOWER('%karl%') 
AND avaliacao >= 8.0;

-- Top 5 séries (como opção 6)
SELECT titulo, avaliacao FROM series 
ORDER BY avaliacao DESC 
LIMIT 5;

-- Encontrar séries inválidas (como opção 7)
SELECT * FROM series WHERE titulo IS NULL OR titulo = '';

-- Deletar séries inválidas
DELETE FROM series WHERE titulo IS NULL OR titulo = '';
```

---

## 📝 Resumo da Aula 03

### ✅ O que você aprendeu:

1. **Derived Query Methods**
   - Spring Data JPA gera SQL automaticamente
   - Nomenclatura: findBy + Campo + Operador
   - Type-safe e otimizado

2. **Busca por título**
   - findByTituloContainingIgnoreCase
   - Busca parcial (LIKE %texto%)
   - Case-insensitive (LOWER)

3. **Queries compostas**
   - Múltiplos critérios com AND
   - findBy...And...
   - Comparações numéricas (>=, <=, >, <)

4. **Top N queries**
   - findTop5ByOrderBy...
   - LIMIT e ORDER BY
   - Rankings e listas top

5. **Otimização**
   - Busca direta no banco vs memória
   - Reutilização de métodos
   - Consistência de dados

6. **Tratamento de nulls**
   - Validação antes de processar
   - Valores padrão (fallback)
   - Robustez contra erros da API

7. **Limpeza de dados**
   - deleteAll() com lista filtrada
   - Manutenção do banco
   - Remoção de dados inválidos

---

## 🔍 AULA 03 - Consultas JPQL Avançadas

### O que é JPQL?

**JPQL (Java Persistence Query Language)** é uma linguagem de consulta orientada a objetos para JPA.

**Diferenças entre JPQL e SQL:**
- **SQL:** Trabalha com tabelas e colunas
- **JPQL:** Trabalha com entidades e atributos Java

**Exemplo:**
```java
// SQL
SELECT * FROM series WHERE titulo LIKE '%boys%';

// JPQL
SELECT s FROM Serie s WHERE s.titulo LIKE '%boys%';
```

**Quando usar JPQL:**
- ✅ Queries complexas com JOIN
- ✅ Funções agregadas (AVG, MAX, COUNT)
- ✅ Subconsultas
- ✅ Queries que Derived Methods não conseguem expressar

---

### 10. Buscar Episódio por Trecho (Opção 9)
**Arquivo:** `repository/SerieRepository.java`

**O que faz:** Busca episódios por trecho do título usando JOIN

**Passos:**

1. **Adicionar método com @Query no repositório:**
```java
public interface SerieRepository extends JpaRepository<Serie, Long> {
    
    // JPQL com JOIN
    // @Query: Define consulta JPQL personalizada
    // SELECT e: Retorna episódios (não séries)
    // FROM Serie s: Entidade Serie (alias s)
    // JOIN s.episodios e: JOIN na lista de episódios
    // WHERE e.titulo: Filtra por título do episódio
    // ILIKE: Case-insensitive LIKE (PostgreSQL)
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);
}
```

**SQL gerado:**
```sql
SELECT e.* 
FROM series s 
INNER JOIN episodios e ON s.id = e.serie_id 
WHERE LOWER(e.titulo) LIKE LOWER('%trecho%');
```

2. **Usar no menu (Principal.java):**
```java
private void buscarEpisodioPorTrecho() {
    System.out.println("Qual o nome do episódio para busca?");
    var trechoEpisodio = leitura.nextLine();
    
    // Busca com JPQL JOIN
    List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
    
    if (episodiosEncontrados.isEmpty()) {
        System.out.println("❌ Nenhum episódio encontrado");
    } else {
        System.out.println("\n✅ Episódios encontrados:");
        episodiosEncontrados.forEach(e ->
            System.out.println("Série: " + e.getSerie().getTitulo() +
                " - S" + e.getTemporada() + "E" + e.getNumeroEpisodio() +
                " - " + e.getTitulo())
        );
    }
}
```

**Conceitos aprendidos:**
- @Query para JPQL personalizada
- JOIN entre entidades
- Retornar entidade diferente (Episodio, não Serie)
- ILIKE para case-insensitive no PostgreSQL
- Parâmetros nomeados (:trechoEpisodio)

---

### 11. Top 5 Episódios por Série (Opção 10)
**Arquivo:** `repository/SerieRepository.java`

**O que faz:** Busca os 5 melhores episódios de uma série específica

**Passos:**

1. **Adicionar método com JPQL + ORDER BY + LIMIT:**
```java
public interface SerieRepository extends JpaRepository<Serie, Long> {
    
    // JPQL com WHERE usando objeto + ORDER BY + LIMIT
    // SELECT e: Retorna episódios
    // FROM Serie s: Entidade Serie
    // JOIN s.episodios e: JOIN na lista de episódios
    // WHERE s = :serie: Filtra por objeto Serie completo
    // AND e.avaliacao > 0.0: Ignora episódios sem avaliação
    // ORDER BY e.avaliacao DESC: Ordena por avaliação (maior primeiro)
    // LIMIT 5: Limita a 5 resultados
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND e.avaliacao > 0.0 ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);
}
```

**SQL gerado:**
```sql
SELECT e.* 
FROM series s 
INNER JOIN episodios e ON s.id = e.serie_id 
WHERE s.id = ? AND e.avaliacao > 0.0 
ORDER BY e.avaliacao DESC 
LIMIT 5;
```

2. **Usar no menu com reutilização de variável:**
```java
private Serie serieBusca;  // Variável de instância (reutilizada)

private void buscarTop5Episodios() {
    // Busca série (reutiliza método)
    buscarSerieporTitulo();
    
    // Verifica se série foi encontrada
    if (serieBusca != null) {
        // Busca top 5 episódios usando JPQL
        List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serieBusca);
        
        if (topEpisodios.isEmpty()) {
            System.out.println("❌ Nenhum episódio encontrado");
        } else {
            System.out.println("\n🏆 Top 5 episódios de " + serieBusca.getTitulo() + ":");
            topEpisodios.forEach(e ->
                System.out.println("S" + e.getTemporada() + "E" + e.getNumeroEpisodio() +
                    " - " + e.getTitulo() + " - Avaliação: " + e.getAvaliacao())
            );
        }
    }
}

private void buscarSerieporTitulo() {
    System.out.println("Escolha uma serie pelo nome: ");
    var nomeSerie = leitura.nextLine();
    
    Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

    if (serieBuscada.isPresent()) {
        serieBusca = serieBuscada.get();  // Armazena na variável de instância
        System.out.println("✅ Dados da série: " + serieBusca);
    } else {
        System.out.println("❌ Série não encontrada!");
        serieBusca = null;
    }
}
```

**Conceitos aprendidos:**
- WHERE com objeto completo (s = :serie)
- ORDER BY + LIMIT em JPQL
- Reutilização de variáveis de instância
- Filtrar avaliações inválidas (> 0.0)
- Composição de métodos

---

### 12. Buscar Episódios por Ano (Opção 11)
**Arquivo:** `repository/SerieRepository.java`

**O que faz:** Busca episódios de uma série a partir de um ano específico

**Passos:**

1. **Adicionar método com função YEAR():**
```java
public interface SerieRepository extends JpaRepository<Serie, Long> {
    
    // JPQL com função YEAR() para filtrar por ano
    // SELECT e: Retorna episódios
    // FROM Serie s: Entidade Serie
    // JOIN s.episodios e: JOIN na lista de episódios
    // WHERE s = :serie: Filtra por série
    // AND YEAR(e.dataLancamento) >= :anoLancamento: Função YEAR() extrai ano da data
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
```

**SQL gerado:**
```sql
SELECT e.* 
FROM series s 
INNER JOIN episodios e ON s.id = e.serie_id 
WHERE s.id = ? AND EXTRACT(YEAR FROM e.data_lancamento) >= ?;
```

2. **Usar no menu:**
```java
private void buscarEpisodiosPorAno() {
    // Busca série (reutiliza método)
    buscarSerieporTitulo();
    
    if (serieBusca != null) {
        System.out.println("Digite o ano limite de lançamento: ");
        var anoLancamento = leitura.nextInt();
        leitura.nextLine();
        
        // Busca episódios usando JPQL com YEAR()
        List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serieBusca, anoLancamento);
        
        if (episodiosAno.isEmpty()) {
            System.out.println("❌ Nenhum episódio encontrado a partir de " + anoLancamento);
        } else {
            System.out.println("\n✅ Episódios de " + serieBusca.getTitulo() + " a partir de " + anoLancamento + ":");
            episodiosAno.forEach(e ->
                System.out.println("S" + e.getTemporada() + "E" + e.getNumeroEpisodio() +
                    " - " + e.getTitulo() + " (" + e.getDataLancamento().getYear() + ")")
            );
        }
    }
}
```

**Conceitos aprendidos:**
- Função YEAR() em JPQL
- Filtrar por ano de data
- Múltiplos parâmetros em @Query
- Reutilização de serieBusca

---

### 13. Exercícios JPQL Avançados (11 Exercícios)
**Pasta:** `exerciciosjpa/`

**O que faz:** Implementa 11 exercícios avançados de JPQL

**Estrutura atualizada:**
```
exerciciosjpa/
├── repository/
│   ├── ProdutoRepository.java (+ 6 JPQL queries)
│   └── PedidoRepository.java (+ 5 JPQL queries)
└── TesteJPQL.java (novo - menu interativo)
```

**ProdutoRepository - 6 JPQL Queries:**

```java
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // ===== FUNÇÕES AGREGADAS =====
    
    // 1. Média de preços por categoria
    @Query("SELECT AVG(p.preco) FROM Produto p WHERE p.categoria.nome = :categoriaNome")
    Double calcularPrecoMedioPorCategoria(String categoriaNome);
    
    // 2. Produto mais caro
    @Query("SELECT p FROM Produto p WHERE p.preco = (SELECT MAX(p2.preco) FROM Produto p2)")
    Optional<Produto> encontrarProdutoMaisCaro();
    
    // 3. Contar produtos por categoria (GROUP BY)
    @Query("SELECT p.categoria.nome, COUNT(p) FROM Produto p GROUP BY p.categoria.nome")
    List<Object[]> contarProdutosPorCategoria();
    
    // ===== RELACIONAMENTOS =====
    
    // 4. Produtos com pedidos (SIZE > 0)
    @Query("SELECT p FROM Produto p WHERE SIZE(p.pedidos) > 0")
    List<Produto> encontrarProdutosComPedidos();
    
    // 5. Produtos sem pedidos (SIZE = 0)
    @Query("SELECT p FROM Produto p WHERE SIZE(p.pedidos) = 0")
    List<Produto> encontrarProdutosSemPedidos();
    
    // ===== SQL NATIVO =====
    
    // 6. Produtos com preço acima da média (SQL nativo)
    @Query(value = "SELECT * FROM produtos WHERE valor > (SELECT AVG(valor) FROM produtos)", 
           nativeQuery = true)
    List<Produto> encontrarProdutosAcimaDaMedia();
}
```

**PedidoRepository - 5 JPQL Queries:**

```java
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // ===== FUNÇÕES AGREGADAS =====
    
    // 7. Total de pedidos por mês (GROUP BY)
    @Query("SELECT MONTH(p.data), COUNT(p) FROM Pedido p GROUP BY MONTH(p.data) ORDER BY MONTH(p.data)")
    List<Object[]> contarPedidosPorMes();
    
    // 8. Pedidos com mais de N produtos (HAVING)
    @Query("SELECT p FROM Pedido p WHERE SIZE(p.produtos) > :quantidade")
    List<Pedido> encontrarPedidosComMaisDeProdutos(int quantidade);
    
    // ===== RELACIONAMENTOS =====
    
    // 9. Pedidos de uma categoria específica (JOIN)
    @Query("SELECT DISTINCT p FROM Pedido p JOIN p.produtos prod WHERE prod.categoria.nome = :categoriaNome")
    List<Pedido> encontrarPedidosPorCategoria(String categoriaNome);
    
    // 10. Pedidos com produto específico (JOIN)
    @Query("SELECT p FROM Pedido p JOIN p.produtos prod WHERE prod.nome = :nomeProduto")
    List<Pedido> encontrarPedidosComProduto(String nomeProduto);
    
    // ===== SQL NATIVO =====
    
    // 11. Pedidos do último mês (SQL nativo)
    @Query(value = "SELECT * FROM pedidos WHERE data >= CURRENT_DATE - INTERVAL '30 days'", 
           nativeQuery = true)
    List<Pedido> encontrarPedidosUltimoMes();
}
```

**TesteJPQL - Menu Interativo:**

```java
@Component
public class TesteJPQL {
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private PedidoRepository pedidoRepository;
    
    public void executarTestes() {
        // Menu com 4 categorias:
        // 1 - Funções Agregadas (AVG, MAX, COUNT, GROUP BY)
        // 2 - Relacionamentos (SIZE, JOIN)
        // 3 - SQL Nativo (nativeQuery = true)
        // 4 - Executar todos os testes
        
        // Exemplos de saída:
        // Preço médio: R$ 2.450,00
        // Produto mais caro: Notebook Dell - R$ 3.500,00
        // Eletrônicos: 3 produtos
        // Produtos com pedidos: [Notebook, Monitor]
        // Pedidos em Janeiro: 5
    }
}
```

**Como testar:**
1. Menu Principal → Opção 13 (Exercícios JPQL)
2. Escolha categoria de teste (1-4)
3. Veja consultas JPQL sendo executadas

**Conceitos aprendidos:**
- **Funções agregadas:** AVG(), MAX(), COUNT()
- **GROUP BY:** Agrupar resultados
- **HAVING:** Filtrar grupos
- **SIZE():** Contar elementos de coleção
- **DISTINCT:** Remover duplicatas
- **Subconsultas:** SELECT dentro de SELECT
- **SQL Nativo:** nativeQuery = true
- **MONTH():** Extrair mês de data
- **INTERVAL:** Operações com datas
- **Object[]:** Retorno de múltiplas colunas

---

## 📊 Comparação: Derived Queries vs JPQL vs SQL Nativo

| Aspecto | Derived Queries | JPQL | SQL Nativo |
|---------|----------------|------|------------|
| **Sintaxe** | Nome do método | Orientada a objetos | SQL puro |
| **Complexidade** | ✅ Simples | ⚠️ Média | ❌ Complexa |
| **Portabilidade** | ✅ Total | ✅ Total | ❌ Depende do banco |
| **Flexibilidade** | ❌ Limitada | ✅ Alta | ✅ Total |
| **Type-safe** | ✅ Sim | ⚠️ Parcial | ❌ Não |
| **Quando usar** | Queries simples | Queries complexas | Otimizações específicas |

**Exemplos:**

```java
// Derived Query - Simples e direto
List<Serie> findByGenero(Categoria categoria);

// JPQL - Complexo com JOIN
@Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trecho%")
List<Episodio> episodiosPorTrecho(String trecho);

// SQL Nativo - Funções específicas do PostgreSQL
@Query(value = "SELECT * FROM series WHERE data >= CURRENT_DATE - INTERVAL '30 days'", 
       nativeQuery = true)
List<Serie> seriesRecentes();
```

---

## 📝 Funções JPQL Úteis

### Funções de String:
- `UPPER(s.titulo)` - Maiúsculas
- `LOWER(s.titulo)` - Minúsculas
- `CONCAT(s.titulo, ' - ', s.genero)` - Concatenar
- `SUBSTRING(s.titulo, 1, 10)` - Substring
- `LENGTH(s.titulo)` - Tamanho

### Funções de Data:
- `YEAR(e.dataLancamento)` - Extrair ano
- `MONTH(e.dataLancamento)` - Extrair mês
- `DAY(e.dataLancamento)` - Extrair dia
- `CURRENT_DATE` - Data atual
- `CURRENT_TIMESTAMP` - Data/hora atual

### Funções Agregadas:
- `AVG(p.preco)` - Média
- `MAX(p.preco)` - Máximo
- `MIN(p.preco)` - Mínimo
- `SUM(p.preco)` - Soma
- `COUNT(p)` - Contagem

### Funções de Coleção:
- `SIZE(s.episodios)` - Tamanho da lista
- `IS EMPTY` - Lista vazia
- `MEMBER OF` - Pertence à lista

---

## 🔍 Verificar no DBeaver - JPQL

### Queries equivalentes às JPQL:

```sql
-- Episódios por trecho (Opção 9)
SELECT e.* 
FROM series s 
INNER JOIN episodios e ON s.id = e.serie_id 
WHERE LOWER(e.titulo) LIKE LOWER('%trecho%');

-- Top 5 episódios por série (Opção 10)
SELECT e.* 
FROM series s 
INNER JOIN episodios e ON s.id = e.serie_id 
WHERE s.id = 1 AND e.avaliacao > 0.0 
ORDER BY e.avaliacao DESC 
LIMIT 5;

-- Episódios por ano (Opção 11)
SELECT e.* 
FROM series s 
INNER JOIN episodios e ON s.id = e.serie_id 
WHERE s.id = 1 AND EXTRACT(YEAR FROM e.data_lancamento) >= 2020;

-- Preço médio por categoria
SELECT c.nome, AVG(p.valor) 
FROM produtos p 
JOIN categorias c ON p.categoria_id = c.id 
GROUP BY c.nome;

-- Produtos com pedidos
SELECT p.*, COUNT(pp.pedido_id) AS total_pedidos
FROM produtos p
LEFT JOIN pedido_produto pp ON p.id = pp.produto_id
GROUP BY p.id
HAVING COUNT(pp.pedido_id) > 0;

-- Pedidos por mês
SELECT EXTRACT(MONTH FROM data) AS mes, COUNT(*) AS total
FROM pedidos
GROUP BY EXTRACT(MONTH FROM data)
ORDER BY mes;
```

---

## 📝 Resumo da Aula 03 - JPQL Completo

### ✅ O que você aprendeu:

1. **Derived Query Methods (Parte 1)**
   - 17 tipos de consultas automáticas
   - Nomenclatura padronizada
   - Busca, filtros, ordenação, contagem

2. **JPQL - Java Persistence Query Language (Parte 2)**
   - @Query para consultas personalizadas
   - JOIN entre entidades
   - WHERE com objetos
   - ORDER BY + LIMIT
   - Funções: YEAR(), MONTH(), AVG(), MAX(), COUNT()

3. **Funções Agregadas**
   - AVG() para médias
   - MAX() e MIN() para extremos
   - COUNT() para contagem
   - GROUP BY para agrupamentos
   - HAVING para filtrar grupos

4. **Relacionamentos em JPQL**
   - JOIN para navegar entre entidades
   - SIZE() para contar coleções
   - DISTINCT para remover duplicatas
   - Queries em relacionamentos N:M

5. **SQL Nativo**
   - nativeQuery = true
   - Funções específicas do banco
   - INTERVAL para datas
   - Otimizações avançadas

6. **Boas Práticas**
   - Reutilização de variáveis (serieBusca)
   - Tratamento de resultados vazios
   - Filtrar dados inválidos (avaliacao > 0.0)
   - Comparação: Derived vs JPQL vs SQL Nativo

---

**Desenvolvido por:** Guilherme Falcão  
**Curso:** Alura - Formação Avançando com Java  
**Última atualização:** Aula 03 - JPQL Avançado (Derived Queries + JPQL + SQL Nativo)

---

### 7. Busca por Categoria (Opção 7)
**Arquivo:** `repository/SerieRepository.java`

**O que faz:** Busca séries por categoria/gênero usando enum

**Passos:**

1. **Adicionar método no repositório:**
```java
public interface SerieRepository extends JpaRepository<Serie, Long> {
    
    // Busca por categoria (enum)
    // findBy: Inicia query
    // Genero: Campo da entidade Serie (tipo Categoria)
    List<Serie> findByGenero(Categoria categoria);
}
```

**SQL gerado automaticamente:**
```sql
SELECT * FROM series WHERE genero = 'ACAO';
```

2. **Melhorar enum Categoria para aceitar variações:**
```java
public enum Categoria {
    ACAO("Action", "Ação"),
    COMEDIA("Comedy", "Comédia"),
    // ...
    
    public static Categoria fromPortugues(String text) {
        // Normaliza texto e aceita variações
        String textNormalizado = text.toLowerCase().trim();
        
        return switch (textNormalizado) {
            case "ação", "acao", "açao", "action" -> ACAO;
            case "comédia", "comedia", "comedy" -> COMEDIA;
            // ... outras variações
            default -> throw new IllegalArgumentException("Categoria não encontrada: " + text);
        };
    }
}
```

3. **Usar no menu com tratamento de erro:**
```java
private void buscarSeriePorCategoria() {
    System.out.println("Digite uma categoria/gênero: ");
    var nomeGenero = leitura.nextLine();
    
    try {
        // Converte texto para enum
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        
        // Busca no banco
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        
        if (seriesPorCategoria.isEmpty()) {
            System.out.println("❌ Nenhuma série encontrada para: " + nomeGenero);
        } else {
            System.out.println("\n✅ Séries da categoria " + nomeGenero + ":");
            seriesPorCategoria.forEach(System.out::println);
        }
    } catch (IllegalArgumentException e) {
        System.out.println("❌ Categoria não encontrada: " + nomeGenero);
        System.out.println("📋 Categorias disponíveis: Ação, Romance, Comédia...");
    }
}
```

**Conceitos aprendidos:**
- Busca por enum
- Tratamento de entrada do usuário
- Variações de texto (com/sem acento)
- Exception handling
- Interface amigável

---

### 8. Filtrar Séries por Temporadas e Avaliação (Opção 8)
**Arquivo:** `repository/SerieRepository.java`

**O que faz:** Busca séries com número máximo de temporadas E avaliação mínima

**Passos:**

1. **Adicionar método COMPOSTO no repositório:**
```java
public interface SerieRepository extends JpaRepository<Serie, Long> {
    
    // Filtro por temporadas E avaliação
    // findBy: Inicia query
    // TotalTemporadas: Campo da entidade
    // LessThanEqual: <= (menor ou igual)
    // And: Combina condições
    // Avaliacao: Campo da entidade
    // GreaterThanEqual: >= (maior ou igual)
    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(
        Integer totalTemporadas, 
        Double avaliacao
    );
}
```

**SQL gerado automaticamente:**
```sql
SELECT * FROM series 
WHERE total_temporadas <= 3 
AND avaliacao >= 8.0;
```

2. **Usar no menu:**
```java
private void filtrarSeriesPorTemporadaEAvaliacao() {
    System.out.println("Filtrar séries até quantas temporadas? ");
    var totalTemporadas = leitura.nextInt();
    leitura.nextLine();
    
    System.out.println("Com avaliação a partir de que valor? ");
    var avaliacao = leitura.nextDouble();
    leitura.nextLine();
    
    // Busca com duas condições
    List<Serie> filtroSeries = repositorio
        .findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(
            totalTemporadas, avaliacao
        );
    
    if (filtroSeries.isEmpty()) {
        System.out.println("❌ Nenhuma série encontrada");
    } else {
        System.out.println("\n✅ *** Séries filtradas ***");
        filtroSeries.forEach(s -> 
            System.out.println("- " + s.getTitulo() + 
                " (" + s.getTotalTemporadas() + " temporadas) - " +
                "Avaliação: " + s.getAvaliacao())
        );
    }
}
```

**Exemplos de uso:**
- Até 3 temporadas, avaliação >= 8.0 → Séries curtas e bem avaliadas
- Até 5 temporadas, avaliação >= 9.0 → Séries médias e excelentes

**Conceitos aprendidos:**
- Queries com múltiplas condições numéricas
- LessThanEqual vs GreaterThanEqual
- Filtros personalizados
- Combinação de critérios diferentes

---

### 9. Exercícios Avançados: 17 Derived Queries
**Pasta:** `exerciciosjpa/`

**O que faz:** Implementa 17 exercícios de consultas avançadas com JPA

**Estrutura atualizada:**
```
exerciciosjpa/
├── model/
│   ├── Produto.java (atualizado)
│   ├── Categoria.java
│   └── Pedido.java (+ dataEntrega)
├── repository/
│   ├── ProdutoRepository.java (12 queries)
│   └── PedidoRepository.java (5 queries)
├── TesteDerivedQueries.java (novo)
└── TesteExerciciosJPA.java (menu atualizado)
```

**ProdutoRepository - 12 Derived Queries:**

```java
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // ===== CONSULTAS BÁSICAS =====
    List<Produto> findByNome(String nome);                           // 1. Nome exato
    List<Produto> findByCategoriaNome(String categoriaNome);         // 2. Por categoria
    List<Produto> findByPrecoGreaterThan(Double preco);              // 3. Preço >
    List<Produto> findByPrecoLessThan(Double preco);                 // 4. Preço <
    List<Produto> findByNomeContaining(String termo);                // 5. Nome contém
    
    // ===== ORDENAÇÃO =====
    List<Produto> findByCategoriaNomeOrderByPrecoAsc(String cat);    // 8. Crescente
    List<Produto> findByCategoriaNomeOrderByPrecoDesc(String cat);   // 9. Decrescente
    
    // ===== CONTAGEM =====
    long countByCategoriaNome(String categoriaNome);                 // 10. Count categoria
    long countByPrecoGreaterThan(Double preco);                      // 11. Count preço
    
    // ===== COMPOSTAS (OR) =====
    List<Produto> findByPrecoLessThanOrNomeContaining(Double p, String t); // 12. OR
    
    // ===== TOP/LIMIT =====
    List<Produto> findTop3ByOrderByPrecoDesc();                      // 16. Top 3 caros
    List<Produto> findTop5ByCategoriaNomeOrderByPrecoAsc(String c);  // 17. Top 5 baratos
}
```

**PedidoRepository - 5 Derived Queries:**

```java
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // ===== DATA DE ENTREGA =====
    List<Pedido> findByDataEntregaIsNull();                          // 6. Sem entrega
    List<Pedido> findByDataEntregaIsNotNull();                       // 7. Com entrega
    
    // ===== DATA DO PEDIDO =====
    List<Pedido> findByDataAfter(LocalDate data);                    // 13. Após data
    List<Pedido> findByDataBefore(LocalDate data);                   // 14. Antes data
    List<Pedido> findByDataBetween(LocalDate inicio, LocalDate fim); // 15. Entre datas
}
```

**TesteDerivedQueries - Menu Interativo:**

```java
@Component
public class TesteDerivedQueries {
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private PedidoRepository pedidoRepository;
    
    public void executarTestes() {
        // Cria dados de teste automaticamente
        criarDadosDeTeste();
        
        // Menu com 6 categorias:
        // 1 - Consultas Básicas (1-5)
        // 2 - Consultas com Ordenação (8-9)
        // 3 - Consultas de Contagem (10-11)
        // 4 - Consultas Compostas (12)
        // 5 - Consultas Top/Limit (16-17)
        // 6 - Consultas de Pedidos (6-7, 13-15)
    }
}
```

**Como testar:**
1. Menu Principal → Opção 10 (Exercícios JPA)
2. Submenu → Opção 2 (Derived Queries)
3. Escolha categoria de teste (1-6)
4. Veja consultas sendo executadas automaticamente

**Conceitos aprendidos:**
- 17 tipos diferentes de Derived Queries
- IsNull vs IsNotNull
- After, Before, Between para datas
- Count queries (retorna long)
- Or em queries compostas
- Top N com ordenação
- Relacionamentos em queries (CategoriaNome)
- Criação automática de dados de teste

---

## 📊 Comparação: Streams vs Derived Queries

| Aspecto | Streams (Memória) | Derived Queries (Banco) |
|---------|-------------------|-------------------------|
| **Performance** | ❌ Lenta para grandes volumes | ✅ Rápida (usa índices) |
| **Memória** | ❌ Carrega todos os dados | ✅ Carrega apenas resultado |
| **Atualização** | ❌ Pode estar desatualizada | ✅ Sempre atualizada |
| **Complexidade** | ✅ Fácil de escrever | ✅ Nomenclatura padronizada |
| **Otimização** | ❌ Não otimizada | ✅ SQL otimizado |
| **Escalabilidade** | ❌ Limitada | ✅ Escala bem |

**Quando usar cada um:**
- **Streams:** Manipulação de dados já carregados, transformações complexas
- **Derived Queries:** Busca de dados, filtros, ordenação, contagem

---

## 📝 Tipos de Retorno em Derived Queries

| Retorno | Quando Usar | Exemplo |
|---------|-------------|----------|
| `Optional<T>` | Pode não encontrar (0 ou 1) | `findByTitulo(String)` |
| `List<T>` | Pode retornar vários (0 ou N) | `findByGenero(Categoria)` |
| `T` | Sempre encontra (1) | `getById(Long)` |
| `long` | Contagem | `countByGenero(Categoria)` |
| `boolean` | Existência | `existsByTitulo(String)` |

**Boas práticas:**
- Use `Optional<T>` quando resultado pode estar vazio
- Use `List<T>` para múltiplos resultados
- Sempre trate `Optional.empty()` e listas vazias

---

## 📊 Resumo da Aula 03 - Atualizado

### ✅ O que você aprendeu:

1. **Derived Query Methods Avançados**
   - 17 tipos diferentes de consultas
   - Nomenclatura padronizada
   - SQL gerado automaticamente

2. **Busca por categoria com enum**
   - Tratamento de variações de texto
   - Exception handling
   - Interface amigável

3. **Filtros compostos avançados**
   - Múltiplas condições numéricas
   - LessThanEqual + GreaterThanEqual
   - Filtros personalizados

4. **Exercícios práticos completos**
   - 17 derived queries implementadas
   - Menu interativo de testes
   - Dados de teste automáticos

5. **Comparação streams vs banco**
   - Performance e escalabilidade
   - Quando usar cada abordagem
   - Otimização de consultas

6. **Tipos de retorno**
   - Optional vs List vs primitivos
   - Tratamento de resultados vazios
   - Boas práticas

---


## 🌐 AULA 04 - Desenvolvimento Web com Spring Boot

### O que é uma Aplicação Web?

**Aplicação Console (Aulas 01-03):**
- Interface de linha de comando (terminal)
- Usuário interage via Scanner
- Executa e finaliza
- Uso: Scripts, ferramentas CLI, processamento batch

**Aplicação Web (Aula 04):**
- Interface HTTP (navegador, Postman, apps mobile)
- Usuário faz requisições HTTP
- Servidor fica "no ar" aguardando requisições
- Uso: APIs REST, sites, microserviços

---

### 1. Configuração do Spring Boot Web
**Arquivo:** `pom.xml`

**O que faz:** Adiciona dependência para criar aplicações web

**Passos:**

1. **Adicionar dependência spring-boot-starter-web:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

**O que essa dependência traz:**
- ✅ **Apache Tomcat** (servidor web embutido)
- ✅ **Spring MVC** (framework para criar controllers)
- ✅ **Jackson** (conversão automática JSON ↔ Java)
- ✅ **Validação** (Bean Validation)
- ✅ **Recursos web** (servir arquivos estáticos)

**Porta padrão:** 8080 (http://localhost:8080)

**Conceitos aprendidos:**
- Starters do Spring Boot
- Servidor embutido vs servidor externo
- Configuração zero (convenção sobre configuração)

---

### 2. Transformar Aplicação Console em Web
**Arquivos:** `ScreenmatchApplication.java`, `ScreenmatchApplicationSemWeb.java`

**O que mudou:** Removeu CommandLineRunner para virar aplicação web

**ANTES (Console):**
```java
@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
    
    @Autowired
    private SerieRepository repositorio;
    
    @Override
    public void run(String... args) {
        Principal principal = new Principal(repositorio);
        principal.exibeMenu();  // Menu interativo
    }
}
```

**Problemas da versão console:**
- ❌ Executa e finaliza
- ❌ Apenas um usuário por vez
- ❌ Interface limitada (terminal)
- ❌ Não escalável

**AGORA (Web):**
```java
@SpringBootApplication
public class ScreenmatchApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
        // Inicia servidor Tomcat na porta 8080
        // Fica aguardando requisições HTTP
    }
    
    // NÃO há mais método run()!
    // Lógica agora está nos CONTROLLERS
}
```

**Vantagens da versão web:**
- ✅ Servidor fica "no ar" 24/7
- ✅ Múltiplos usuários simultâneos
- ✅ Acesso via navegador/app mobile
- ✅ Escalável (pode adicionar mais servidores)

**Backup da versão console:**
```java
// ScreenmatchApplicationSemWeb.java
// @SpringBootApplication  // COMENTADO para não conflitar
public class ScreenmatchApplicationSemWeb implements CommandLineRunner {
    // Código original mantido como backup
}
```

**Conceitos aprendidos:**
- CommandLineRunner vs aplicação web
- Ciclo de vida da aplicação
- Servidor HTTP vs execução única

---

### 3. Configurar Classe Principal no Maven
**Arquivo:** `pom.xml`

**Problema:** Maven encontrou duas classes com @SpringBootApplication

**Erro:**
```
Unable to find a single main class from the following candidates:
[ScreenmatchApplicationSemWeb, ScreenmatchApplication]
```

**Solução: Especificar qual classe é a principal**

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <!-- Define qual classe é a principal (versão WEB) -->
                <mainClass>br.com.alura.screenmatch.ScreenmatchApplication</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

**Conceitos aprendidos:**
- Configuração do Maven
- Resolução de conflitos
- Classe principal (main class)

---

### 4. Arquitetura MVC (Model-View-Controller)

**O que é MVC?**

Padrão de arquitetura que separa aplicação em 3 camadas:

```
┌─────────────────────────────────────────────┐
│              CLIENTE (Navegador)            │
│         http://localhost:8080/series        │
└─────────────────┬───────────────────────────┘
                  │ HTTP Request
                  ↓
┌─────────────────────────────────────────────┐
│         CONTROLLER (SerieController)        │
│  - Recebe requisições HTTP                  │
│  - Valida dados de entrada                  │
│  - Chama Service                            │
│  - Retorna resposta HTTP                    │
└─────────────────┬───────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────┐
│         SERVICE (SerieService)              │
│  - Lógica de negócio                        │
│  - Regras da aplicação                      │
│  - Chama Repository                         │
└─────────────────┬───────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────┐
│      REPOSITORY (SerieRepository)           │
│  - Acesso ao banco de dados                 │
│  - Queries JPA/JPQL                         │
└─────────────────┬───────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────┐
│         MODEL (Serie, Episodio)             │
│  - Entidades JPA                            │
│  - Representam tabelas do banco             │
└─────────────────────────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────┐
│         DATABASE (PostgreSQL)               │
│  - Tabelas: series, episodios               │
└─────────────────────────────────────────────┘
```

**Responsabilidades:**

| Camada | Responsabilidade | Exemplo |
|--------|------------------|---------|
| **Controller** | Receber requisições HTTP | @GetMapping("/series") |
| **Service** | Lógica de negócio | Validar, calcular, processar |
| **Repository** | Acesso ao banco | findAll(), save() |
| **Model** | Representar dados | @Entity Serie |

**Conceitos aprendidos:**
- Separação de responsabilidades
- Arquitetura em camadas
- Baixo acoplamento, alta coesão

---

### 5. Criar Primeiro Controller
**Arquivo:** `controller/SerieController.java`

**O que faz:** Cria endpoint REST para receber requisições HTTP

**Passos:**

1. **Criar classe com @RestController:**
```java
package br.com.alura.screenmatch.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController  // Marca como controller REST
public class SerieController {
    
    @GetMapping("/series")  // Mapeia GET http://localhost:8080/series
    public String obterSeries(@RequestParam(required = false) String nomedaserie) {
        if (nomedaserie == null) {
            return "Nenhuma série informada";
        }
        return "Série informada: " + nomedaserie;
    }
}
```

**Anotações:**

| Anotação | Função |
|----------|--------|
| `@RestController` | Combina @Controller + @ResponseBody (retorna dados, não HTML) |
| `@GetMapping("/series")` | Mapeia requisição GET para método Java |
| `@RequestParam` | Captura parâmetro da URL (?nomedaserie=Lost) |
| `required = false` | Parâmetro é opcional |

**Fluxo de requisição:**

```
1. Cliente: GET http://localhost:8080/series?nomedaserie=Lost
   ↓
2. Tomcat recebe requisição HTTP
   ↓
3. Spring identifica @GetMapping("/series")
   ↓
4. Spring chama obterSeries("Lost")
   ↓
5. Método retorna: "Série informada: Lost"
   ↓
6. Spring converte para HTTP Response
   ↓
7. Cliente recebe: HTTP 200 OK
   Body: "Série informada: Lost"
```

**Conceitos aprendidos:**
- @RestController vs @Controller
- Mapeamento de rotas
- Captura de parâmetros
- Retorno automático de dados

---

### 6. Testar Endpoint no Navegador

**Como testar:**

1. **Iniciar aplicação:**
```bash
mvn spring-boot:run
```

**Console mostrará:**
```
Tomcat started on port(s): 8080 (http)
Started ScreenmatchApplication in 2.5 seconds
```

2. **Abrir navegador:**

**Teste 1 - Sem parâmetro:**
```
http://localhost:8080/series
```
**Resposta:**
```
Nenhuma série informada
```

**Teste 2 - Com parâmetro:**
```
http://localhost:8080/series?nomedaserie=Breaking Bad
```
**Resposta:**
```
Série informada: Breaking Bad
```

**Teste 3 - Múltiplos parâmetros (futuro):**
```
http://localhost:8080/series?nomedaserie=Lost&temporada=1
```

**Ferramentas de teste:**
- ✅ **Navegador** (Chrome, Firefox) - Simples para GET
- ✅ **Postman** - Completo (GET, POST, PUT, DELETE)
- ✅ **cURL** - Linha de comando
- ✅ **Thunder Client** (VS Code) - Extensão

**Conceitos aprendidos:**
- Testar APIs REST
- Query parameters
- HTTP status codes (200 OK)
- Ferramentas de teste

---

### 7. Diferença: Aplicação Console vs Web

| Aspecto | Console (Aulas 01-03) | Web (Aula 04) |
|---------|----------------------|---------------|
| **Interface** | Terminal (Scanner) | HTTP (navegador/Postman) |
| **Execução** | Roda e finaliza | Fica "no ar" |
| **Usuários** | Um por vez | Múltiplos simultâneos |
| **Acesso** | Local (mesmo PC) | Remoto (rede/internet) |
| **Entrada** | Scanner.nextLine() | @RequestParam, @RequestBody |
| **Saída** | System.out.println() | return (JSON/texto) |
| **Escalabilidade** | Limitada | Alta (load balancer) |
| **Uso** | Scripts, batch | APIs, sites, apps |

**Exemplo prático:**

**Console:**
```java
System.out.println("Digite o nome da série:");
String nome = scanner.nextLine();
System.out.println("Série: " + nome);
```

**Web:**
```java
@GetMapping("/series")
public String obterSeries(@RequestParam String nome) {
    return "Série: " + nome;
}
```

**Conceitos aprendidos:**
- Paradigmas de aplicação
- Quando usar cada tipo
- Escalabilidade e concorrência

---

## 📊 Resumo da Aula 04

### ✅ O que você aprendeu:

1. **Configuração Web**
   - Dependência spring-boot-starter-web
   - Servidor Tomcat embutido
   - Porta 8080 padrão

2. **Transformação Console → Web**
   - Remover CommandLineRunner
   - Remover método run()
   - Criar Controllers

3. **Arquitetura MVC**
   - Separação em camadas
   - Controller, Service, Repository, Model
   - Responsabilidades de cada camada

4. **Controllers REST**
   - @RestController
   - @GetMapping
   - @RequestParam
   - Retorno automático de dados

5. **Testes**
   - Navegador para GET
   - Postman para APIs completas
   - Query parameters

6. **Boas Práticas**
   - Backup da versão console
   - Configuração de classe principal
   - Separação de responsabilidades

---

## 🔜 Próximas Aulas

- [ ] Retornar JSON (List<Serie>)
- [ ] Injetar SerieRepository no Controller
- [ ] Criar mais endpoints (top5, buscar por ID)
- [ ] DTOs (Data Transfer Objects)
- [ ] Tratamento de erros (@ExceptionHandler)
- [ ] CORS (Cross-Origin Resource Sharing)
- [ ] Conectar com front-end

---

**Desenvolvido por:** Guilherme Falcão  
**Curso:** Alura - Formação Avançando com Java  
**Última atualização:** Aula 04 - Desenvolvimento Web (Spring Boot + REST)


## 🌐 AULA 04 - Desenvolvimento Web com Spring Boot (COMPLETA)

### 1. Adicionar Dependência Spring Boot Web
**Arquivo:** `pom.xml`

**O que faz:** Transforma aplicação console em aplicação web

**Dependências adicionadas:**
```xml
<!-- Spring Boot Web: Tomcat + Spring MVC + Jackson -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- DevTools: Hot reload automático -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

**O que spring-boot-starter-web traz:**
- ✅ Apache Tomcat (servidor web embutido)
- ✅ Spring MVC (framework para controllers)
- ✅ Jackson (conversão JSON ↔ Java)
- ✅ Validação de dados
- ✅ Recursos web (arquivos estáticos)

**Conceitos aprendidos:**
- Starters do Spring Boot
- Servidor embutido vs externo
- Convenção sobre configuração

---

### 2. Transformar Aplicação Console em Web
**Arquivos:** `ScreenmatchApplication.java`, `ScreenmatchApplicationSemWeb.java`

**ANTES (Console):**
```java
@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
    @Autowired
    private SerieRepository repositorio;
    
    @Override
    public void run(String... args) {
        Principal principal = new Principal(repositorio);
        principal.exibeMenu();
    }
}
```

**AGORA (Web):**
```java
@SpringBootApplication
public class ScreenmatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
        // Inicia servidor Tomcat na porta 8080
        // Fica aguardando requisições HTTP
    }
}
```

**Backup (ScreenmatchApplicationSemWeb):**
```java
// @SpringBootApplication  // Comentado para não conflitar
public class ScreenmatchApplicationSemWeb implements CommandLineRunner {
    // Código original mantido como backup
}
```

**Configurar classe principal no pom.xml:**
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>br.com.alura.screenmatch.ScreenmatchApplication</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

**Conceitos aprendidos:**
- CommandLineRunner vs aplicação web
- Ciclo de vida da aplicação
- Servidor HTTP vs execução única

---

### 3. Criar Controller REST
**Arquivo:** `controller/SerieController.java`

**O que faz:** Recebe requisições HTTP e retorna respostas

**Código:**
```java
@RestController
public class SerieController {
    
    @Autowired
    private SerieRepository repositorio;
    
    // Endpoint de teste
    @GetMapping("/inicio")
    public String inicio() {
        return "Bem-vindo ao Screenmatch!";
    }
    
    // Endpoint que retorna séries
    @GetMapping("/series")
    public List<SerieDTO> obterSeries() {
        return repositorio.findAll()
                .stream()
                .map(s -> new SerieDTO(...))
                .collect(Collectors.toList());
    }
}
```

**Anotações:**
- `@RestController` - Controller REST (retorna dados, não HTML)
- `@GetMapping` - Mapeia requisição GET para método
- `@Autowired` - Injeção de dependência

**Conceitos aprendidos:**
- Controllers REST
- Mapeamento de rotas
- Retorno automático de JSON

---

### 4. Criar DTO (Data Transfer Object)
**Arquivo:** `dto/SerieDTO.java`

**O que faz:** Controla quais dados são expostos na API (SEM episódios)

**Código:**
```java
public record SerieDTO(
        Long id,
        String titulo,
        Integer totalTemporadas,
        Double avaliacao,
        Categoria genero,
        String atores,
        String poster,
        String sinopse
) {
    // Record gera automaticamente:
    // - Construtor
    // - Getters (id(), titulo(), etc.)
    // - equals(), hashCode(), toString()
    // - É IMUTÁVEL (sem setters)
}
```

**Por que usar DTO?**
- ✅ Evita expor relacionamentos complexos (episódios)
- ✅ Evita loop infinito de serialização JSON
- ✅ Controla dados expostos
- ✅ Melhora performance
- ✅ Desacopla API da estrutura do banco

**Conversão Serie → SerieDTO:**
```java
return repositorio.findAll()
        .stream()
        .map(s -> new SerieDTO(
                s.getId(),
                s.getTitulo(),
                s.getTotalTemporadas(),
                s.getAvaliacao(),
                s.getGenero(),
                s.getAtores(),
                s.getPoster(),
                s.getSinopse()
        ))
        .collect(Collectors.toList());
```

**Conceitos aprendidos:**
- DTOs para transferência de dados
- Records em Java
- Stream API para conversão
- Serialização JSON

---

### 5. Evitar Loop Infinito com @JsonIgnore
**Arquivo:** `model/Episodio.java`

**Problema:** Relacionamento bidirecional causa loop infinito

```
Serie → episodios → Serie → episodios → ...
```

**Solução:**
```java
@Entity
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @JsonIgnore: Não inclui este campo no JSON
    @JsonIgnore
    @ManyToOne
    private Serie serie;
    
    // ... outros campos
}
```

**Conceitos aprendidos:**
- Serialização circular
- @JsonIgnore
- Controle de serialização JSON

---

### 6. Configurar CORS
**Arquivo:** `config/CorsConfiguration.java`

**O que faz:** Permite front-end acessar back-end

**Problema sem CORS:**
```
Front-end (http://127.0.0.1:5501) 
    ↓ tenta acessar
Back-end (http://localhost:8080)
    ↓
❌ BLOQUEADO pelo navegador!
```

**Solução:**
```java
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Todas as rotas
                .allowedOrigins("http://127.0.0.1:5501")  // Autoriza Live Server
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
```

**Configurações:**
- `addMapping("/**")` - Aplica em todas as rotas
- `allowedOrigins()` - Quais domínios podem acessar
- `allowedMethods()` - Quais métodos HTTP são permitidos

**Conceitos aprendidos:**
- CORS (Cross-Origin Resource Sharing)
- Segurança de navegadores
- Autorização de origens
- Pré-flight requests (OPTIONS)

---

### 7. DevTools para Hot Reload
**Arquivo:** `pom.xml`

**O que faz:** Reinicia aplicação automaticamente ao salvar arquivo

**Fluxo:**
```
1. Editar código
2. Salvar (Ctrl+S)
3. DevTools detecta mudança
4. Reinicia aplicação (2-5 segundos)
5. Testar no navegador
```

**Vantagens:**
- ⚡ Reinício rápido (2-5s vs 10-30s)
- 🔄 Automático
- 🚀 Mais produtividade
- 🔒 Não vai para produção

**Conceitos aprendidos:**
- Hot reload
- Desenvolvimento ágil
- ClassLoaders (base + restart)

---

## 📊 Arquitetura MVC

```
┌─────────────────────────────────────┐
│  CLIENTE (Navegador/Postman)        │
│  http://localhost:8080/series       │
└─────────────┬───────────────────────┘
              │ HTTP Request
              ↓
┌─────────────────────────────────────┐
│  CONTROLLER (SerieController)       │
│  @RestController                    │
│  @GetMapping("/series")             │
└─────────────┬───────────────────────┘
              │
              ↓
┌─────────────────────────────────────┐
│  REPOSITORY (SerieRepository)       │
│  findAll()                          │
└─────────────┬───────────────────────┘
              │
              ↓
┌─────────────────────────────────────┐
│  MODEL (Serie, Episodio)            │
│  @Entity                            │
└─────────────┬───────────────────────┘
              │
              ↓
┌─────────────────────────────────────┐
│  DATABASE (PostgreSQL)              │
│  Tabelas: series, episodios         │
└─────────────────────────────────────┘
              │
              ↓ (conversão)
┌─────────────────────────────────────┐
│  DTO (SerieDTO)                     │
│  Dados expostos na API              │
└─────────────┬───────────────────────┘
              │
              ↓ JSON
┌─────────────────────────────────────┐
│  CLIENTE recebe JSON                │
│  [{"id":1,"titulo":"Breaking Bad"}] │
└─────────────────────────────────────┘
```

---

## 🧪 Testando a API

### Endpoints Disponíveis

| Endpoint | Método | Retorno | Descrição |
|----------|--------|---------|-----------|
| `/inicio` | GET | Texto | Teste DevTools |
| `/series` | GET | JSON | Lista séries (DTO) |

### Teste 1: Endpoint /inicio
```
http://localhost:8080/inicio
```
**Resposta:**
```
Bem-vindo ao Screenmatch!
```

### Teste 2: Endpoint /series
```
http://localhost:8080/series
```
**Resposta:**
```json
[
  {
    "id": 1,
    "titulo": "Breaking Bad",
    "totalTemporadas": 5,
    "avaliacao": 9.5,
    "genero": "DRAMA",
    "atores": "Bryan Cranston, Aaron Paul",
    "poster": "https://...",
    "sinopse": "Um professor..."
  }
]
```

**Nota:** Campo `episodios` NÃO aparece (controlado pelo DTO)

---

## 📝 Resumo da Aula 04

### ✅ O que você aprendeu:

1. **Spring Boot Web**
   - Dependência spring-boot-starter-web
   - Servidor Tomcat embutido
   - Porta 8080 padrão

2. **Controllers REST**
   - @RestController
   - @GetMapping
   - Retorno automático de JSON

3. **DTOs**
   - Data Transfer Objects
   - Records em Java
   - Conversão com stream().map()

4. **CORS**
   - Cross-Origin Resource Sharing
   - Autorização de origens
   - Segurança de navegadores

5. **DevTools**
   - Hot reload automático
   - Economia de tempo
   - Desenvolvimento ágil

6. **Arquitetura MVC**
   - Separação de camadas
   - Controller, Service, Repository, Model
   - Responsabilidades bem definidas

---

**Desenvolvido por:** Guilherme Falcão  
**Curso:** Alura - Formação Avançando com Java  
**Última atualização:** Aula 04 - Desenvolvimento Web (Completa)


## 🌐 AULA 04 - Parte 2: Service Layer e Busca por ID

### 8. Criar Service Layer (Camada de Serviço)
**Arquivo:** `service/SerieService.java`

**O que faz:** Centraliza lógica de negócio e conversões

**Por que criar Service?**

**ANTES (Controller acessava Repository diretamente):**
```java
@RestController
public class SerieController {
    @Autowired
    private SerieRepository repositorio;  // ❌ Alto acoplamento
    
    @GetMapping("/series")
    public List<SerieDTO> obterSeries() {
        // ❌ Lógica de conversão no Controller
        return repositorio.findAll()
                .stream()
                .map(s -> new SerieDTO(...))
                .collect(Collectors.toList());
    }
}
```

**Problemas:**
- ❌ Controller conhece detalhes de conversão
- ❌ Código duplicado em múltiplos endpoints
- ❌ Difícil testar
- ❌ Alto acoplamento

**AGORA (Controller chama Service):**
```java
@RestController
public class SerieController {
    @Autowired
    private SerieService servico;  // ✅ Baixo acoplamento
    
    @GetMapping("/series")
    public List<SerieDTO> obterSeries() {
        return servico.obterTodasAsSeries();  // ✅ Simples e limpo
    }
}
```

**Service (SerieService.java):**
```java
@Service
public class SerieService {
    @Autowired
    private SerieRepository repository;
    
    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repository.findAll());
    }
    
    public List<SerieDTO> obterTop5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }
    
    public List<SerieDTO> obterLancamentos() {
        return converteDados(repository.encontrarEpisodiosMaisRecentes());
    }
    
    // Método privado para evitar duplicação (DRY)
    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(
                        s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getAvaliacao(),
                        s.getGenero(),
                        s.getAtores(),
                        s.getPoster(),
                        s.getSinopse()
                ))
                .collect(Collectors.toList());
    }
}
```

**Vantagens:**
- ✅ **Baixo acoplamento:** Controller não conhece Repository
- ✅ **Alta coesão:** Cada classe tem uma responsabilidade
- ✅ **DRY:** Método converteDados() reutilizado
- ✅ **Testabilidade:** Fácil criar mocks
- ✅ **Manutenibilidade:** Mudanças centralizadas

**Conceitos aprendidos:**
- Service layer
- Separação de responsabilidades
- Princípio DRY (Don't Repeat Yourself)
- Baixo acoplamento, alta coesão
- Injeção de dependência em camadas

---

### 9. Buscar Série por ID
**Arquivos:** `controller/SerieController.java`, `service/SerieService.java`

**O que faz:** Retorna UMA série específica pelo ID

**Passos:**

1. **Adicionar endpoint no Controller:**
```java
@RestController
public class SerieController {
    @Autowired
    private SerieService servico;
    
    /**
     * Endpoint GET /series/{id}
     * 
     * @PathVariable: Captura variável da URL
     * - URL: /series/1 → id = 1
     * - URL: /series/42 → id = 42
     * 
     * Exemplo: http://localhost:8080/series/1
     */
    @GetMapping("/series/{id}")
    public SerieDTO obterPorId(@PathVariable Long id) {
        return servico.obterPorId(id);
    }
}
```

2. **Adicionar método no Service:**
```java
@Service
public class SerieService {
    @Autowired
    private SerieRepository repository;
    
    /**
     * Busca série por ID
     * 
     * Optional<Serie>:
     * - findById() retorna Optional (pode não existir)
     * - isPresent(): Verifica se encontrou
     * - get(): Extrai objeto do Optional
     * 
     * Retorna null se não encontrar
     * Alternativa: throw new RuntimeException("Série não encontrada")
     */
    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repository.findById(id);
        
        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(
                    s.getId(),
                    s.getTitulo(),
                    s.getTotalTemporadas(),
                    s.getAvaliacao(),
                    s.getGenero(),
                    s.getAtores(),
                    s.getPoster(),
                    s.getSinopse()
            );
        }
        return null;
    }
}
```

**Anotações:**
- `@PathVariable` - Captura variável do caminho da URL
- `{id}` - Placeholder na rota
- `Long id` - Parâmetro do método

**Fluxo de requisição:**
```
1. Cliente: GET http://localhost:8080/series/1
   ↓
2. Spring identifica @GetMapping("/series/{id}")
   ↓
3. Spring extrai id = 1 da URL
   ↓
4. Spring chama obterPorId(1L)
   ↓
5. Controller chama servico.obterPorId(1L)
   ↓
6. Service chama repository.findById(1L)
   ↓
7. Repository executa: SELECT * FROM series WHERE id = 1
   ↓
8. Service converte Serie → SerieDTO
   ↓
9. Controller retorna JSON
   ↓
10. Cliente recebe: {"id":1,"titulo":"Breaking Bad",...}
```

**Conceitos aprendidos:**
- @PathVariable para capturar ID
- Optional<T> para tratar resultado vazio
- isPresent() e get()
- Busca por chave primária
- Tratamento de null

---

### 10. Adicionar Prefixo de Rota com @RequestMapping
**Arquivo:** `controller/SerieController.java`

**O que faz:** Define prefixo comum para todas as rotas do controller

**ANTES (sem @RequestMapping):**
```java
@RestController
public class SerieController {
    @GetMapping("/series")           // http://localhost:8080/series
    @GetMapping("/series/top5")      // http://localhost:8080/series/top5
    @GetMapping("/series/lancamentos") // http://localhost:8080/series/lancamentos
    @GetMapping("/series/{id}")      // http://localhost:8080/series/{id}
}
```

**Problema:** Repetição de "/series" em todas as rotas

**AGORA (com @RequestMapping):**
```java
@RestController
@RequestMapping("/series")  // Prefixo comum
public class SerieController {
    @GetMapping                // http://localhost:8080/series
    @GetMapping("/top5")       // http://localhost:8080/series/top5
    @GetMapping("/lancamentos") // http://localhost:8080/series/lancamentos
    @GetMapping("/{id}")       // http://localhost:8080/series/{id}
}
```

**Vantagens:**
- ✅ Código mais limpo (DRY)
- ✅ Fácil mudar prefixo (um lugar só)
- ✅ Organização por recurso
- ✅ Padrão REST

**Conceitos aprendidos:**
- @RequestMapping para prefixo
- Organização de rotas
- Padrão REST (recursos)
- DRY em rotas

---

## 🧪 Testando Todos os Endpoints

### Endpoints Disponíveis

| Endpoint | Método | Retorno | Descrição |
|----------|--------|---------|-----------|
| `/series` | GET | List<SerieDTO> | Todas as séries |
| `/series/top5` | GET | List<SerieDTO> | Top 5 avaliações |
| `/series/lancamentos` | GET | List<SerieDTO> | 5 lançamentos recentes |
| `/series/{id}` | GET | SerieDTO | Série específica |

### Teste 1: Todas as séries
```
GET http://localhost:8080/series
```
**Resposta:**
```json
[
  {"id":1,"titulo":"Breaking Bad","totalTemporadas":5,...},
  {"id":2,"titulo":"The Boys","totalTemporadas":4,...}
]
```

### Teste 2: Top 5 séries
```
GET http://localhost:8080/series/top5
```
**Resposta:**
```json
[
  {"id":1,"titulo":"Breaking Bad","avaliacao":9.5,...},
  {"id":3,"titulo":"Friends","avaliacao":8.9,...}
]
```

### Teste 3: Lançamentos recentes
```
GET http://localhost:8080/series/lancamentos
```
**Resposta:**
```json
[
  {"id":2,"titulo":"The Boys","totalTemporadas":4,...}
]
```

### Teste 4: Série por ID
```
GET http://localhost:8080/series/1
```
**Resposta:**
```json
{
  "id": 1,
  "titulo": "Breaking Bad",
  "totalTemporadas": 5,
  "avaliacao": 9.5,
  "genero": "DRAMA",
  "atores": "Bryan Cranston, Aaron Paul",
  "poster": "https://...",
  "sinopse": "Um professor..."
}
```

### Teste 5: ID inexistente
```
GET http://localhost:8080/series/999
```
**Resposta:**
```
null
```
**Status:** 200 OK (poderia ser 404 Not Found com tratamento de erro)

---

## 📊 Arquitetura Final (Aula 04 Completa)

```
┌─────────────────────────────────────────────┐
│  CLIENTE (Navegador/Postman)                │
│  GET http://localhost:8080/series/1         │
└─────────────────┬───────────────────────────┘
                  │ HTTP Request
                  ↓
┌─────────────────────────────────────────────┐
│  CONTROLLER (SerieController)               │
│  @RestController                            │
│  @RequestMapping("/series")                 │
│  @GetMapping("/{id}")                       │
│  - Recebe requisições HTTP                  │
│  - Chama Service (NÃO Repository!)          │
│  - Retorna JSON                             │
└─────────────────┬───────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────┐
│  SERVICE (SerieService)                     │
│  @Service                                   │
│  - Lógica de negócio                        │
│  - Conversão Serie → SerieDTO               │
│  - Método privado converteDados() (DRY)     │
│  - Chama Repository                         │
└─────────────────┬───────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────┐
│  REPOSITORY (SerieRepository)               │
│  extends JpaRepository<Serie, Long>         │
│  - findById(id)                             │
│  - findAll()                                │
│  - findTop5ByOrderByAvaliacaoDesc()         │
└─────────────────┬───────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────┐
│  MODEL (Serie, Episodio)                    │
│  @Entity                                    │
│  - Entidades JPA                            │
└─────────────────┬───────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────┐
│  DATABASE (PostgreSQL)                      │
│  - Tabela: series                           │
│  - SELECT * FROM series WHERE id = 1        │
└─────────────────┬───────────────────────────┘
                  │
                  ↓ (conversão)
┌─────────────────────────────────────────────┐
│  DTO (SerieDTO)                             │
│  - Dados expostos na API (SEM episódios)    │
└─────────────────┬───────────────────────────┘
                  │
                  ↓ JSON
┌─────────────────────────────────────────────┐
│  CLIENTE recebe JSON                        │
│  {"id":1,"titulo":"Breaking Bad",...}       │
└─────────────────────────────────────────────┘
```

---

## 📝 Resumo da Aula 04 - Completa

### ✅ O que você aprendeu:

1. **Service Layer**
   - Camada de serviço para lógica de negócio
   - Separação Controller → Service → Repository
   - Baixo acoplamento, alta coesão
   - Injeção de dependência em camadas

2. **Princípio DRY**
   - Método privado converteDados()
   - Reutilização de código
   - Evitar duplicação

3. **Busca por ID**
   - @PathVariable para capturar ID da URL
   - Optional<T> para tratar resultado vazio
   - isPresent() e get()
   - Tratamento de null

4. **@RequestMapping**
   - Prefixo comum para rotas
   - Organização por recurso
   - Padrão REST
   - DRY em rotas

5. **Arquitetura Completa**
   - Controller: HTTP
   - Service: Lógica de negócio
   - Repository: Banco de dados
   - Model: Entidades
   - DTO: Transferência de dados

---

**Desenvolvido por:** Guilherme Falcão  
**Curso:** Alura - Formação Avançando com Java  
**Última atualização:** Aula 04 - Parte 2 (Service Layer + Busca por ID)
