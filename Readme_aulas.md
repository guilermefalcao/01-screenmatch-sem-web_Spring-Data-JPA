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
