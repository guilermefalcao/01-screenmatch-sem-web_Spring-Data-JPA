
![Programação-Formação Java](https://github.com/iasminaraujoc/3355-java-screenmatch-com-jpa/assets/84939115/3c51e000-962d-4dc9-97fc-1d384e2511a2)

# Java: persistência de dados e consultas com Spring Data JPA

Projeto desenvolvido no segundo curso da formação Avançando com Java da Alura

## 🔨 Objetivos do projeto

- Evoluir no projeto Screenmatch, iniciado no primeiro curso da formação, criando um menu com várias opções
- Modelar as abstrações da aplicação através de classes, enums, atributos e métodos
- Consumir a API do ChatGPT (alternativa: MyMemory API para tradução)
- Utilizar o Spring Data JPA para persistir dados no banco
- Conhecer vários tipos de banco de dados e utilizar o PostgreSQL
- Trabalhar com vários tipos de consultas ao banco de dados
- Aprofundar na interface JPARepository

## ✨ Funcionalidades Implementadas

### Aula 04 - Desenvolvimento Web com Spring Boot

- ✅ Configuração do Spring Boot Web (spring-boot-starter-web)
- ✅ Servidor Tomcat embutido na porta 8080
- ✅ Criação de Controllers REST (@RestController)
- ✅ Primeiro endpoint GET com @GetMapping
- ✅ Captura de parâmetros com @RequestParam
- ✅ Arquitetura MVC (Model-View-Controller)
- ✅ Separação de camadas (Controller, Service, Repository)
- ✅ Backup da versão console (ScreenmatchApplicationSemWeb)
- ✅ Configuração de classe principal no pom.xml
- ✅ Testes de endpoints via navegador

### Aula 01 - Modelando a aplicação

- ✅ Menu interativo com loop para buscar múltiplas séries
- ✅ Classe `Serie` para modelar dados de séries de TV
- ✅ Enum `Categoria` para representar gêneros de forma tipada
- ✅ Conversão de dados da API OMDB para objetos Java
- ✅ Uso de `OptionalDouble` para tratamento seguro de conversões
- ✅ Integração com API MyMemory para tradução automática de sinopses
- ✅ Encapsulamento com métodos privados
- ✅ Mapeamento de atributos com `@JsonAlias`
- ✅ 8 exercícios resolvidos sobre manipulação de dados e enums

### Aula 02 - Persistência de Dados com JPA

- ✅ Configuração do PostgreSQL e conexão com banco de dados
- ✅ Mapeamento de entidades JPA com anotações (@Entity, @Id, @Column)
- ✅ Criação de repositórios com JpaRepository
- ✅ Injeção de dependência com @Autowired
- ✅ Persistência automática de dados no banco
- ✅ Variáveis de ambiente para proteção de credenciais (.env)
- ✅ Exercícios práticos JPA (Produto, Categoria, Pedido)
- ✅ Relacionamentos JPA (@OneToMany e @ManyToOne)
- ✅ Persistência em cascata (cascade = CascadeType.ALL)
- ✅ Busca e salvamento de episódios com relacionamento bidirecional
- ✅ Relacionamento @OneToMany bidirecional (Categoria-Produto)
- ✅ Relacionamento @ManyToOne unidirecional (Produto-Fornecedor)
- ✅ Relacionamento @ManyToMany com tabela intermediária (Produto-Pedido)
- ✅ Fetch types (EAGER vs LAZY) e LazyInitializationException
- ✅ Limpeza de dados com deleteAll() para evitar duplicação

### Aula 03 - Consultas com Spring Data JPA

- ✅ Derived Query Methods (métodos derivados)
- ✅ Busca por título com findByTituloContainingIgnoreCase
- ✅ Busca composta com AND (ator + avaliação mínima)
- ✅ Top N queries com findTop5ByOrderByAvaliacaoDesc
- ✅ Busca por categoria com enum e tratamento de variações
- ✅ Filtrar séries por temporadas e avaliação
- ✅ JPQL com JOIN para buscar episódios por trecho
- ✅ JPQL com WHERE usando objeto (s = :serie)
- ✅ Top 5 episódios por série com ORDER BY e LIMIT
- ✅ Buscar episódios a partir de uma data com função YEAR()
- ✅ 11 exercícios JPQL avançados (AVG, MAX, COUNT, GROUP BY, SIZE, nativeQuery)
- ✅ Reutilização de variáveis de instância (serieBusca)
- ✅ Otimização de buscas (banco vs memória)
- ✅ Tratamento de dados nulos da API OMDB
- ✅ Comparação: Streams vs Derived Queries vs JPQL vs SQL Nativo

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.1.1
- **Spring Boot Web** (API REST)
- **Apache Tomcat** (servidor embutido)
- Spring Data JPA (persistência)
- PostgreSQL (banco de dados)
- Hibernate (ORM)
- Jackson (processamento JSON)
- Maven (gerenciamento de dependências)
- API OMDB (busca de séries)
- API MyMemory (tradução gratuita)

## 📦 Estrutura do Projeto

```
src/main/java/br/com/alura/screenmatch/
├── controller/                          ⭐ NOVO - Camada Web
│   └── SerieController.java            (endpoints REST)
├── model/
│   ├── Categoria.java (enum de gêneros)
│   ├── DadosSerie.java (record para API)
│   ├── DadosTemporada.java
│   ├── Episodio.java
│   └── Serie.java (entidade JPA)
├── repository/
│   └── SerieRepository.java (JpaRepository)
├── service/
│   ├── ConsumoApi.java
│   ├── ConverteDados.java
│   └── traducao/
│       ├── ConsultaMyMemory.java
│       ├── DadosTraducao.java
│       └── DadosResposta.java
├── exerciciosjpa/
│   ├── model/
│   ├── repository/
│   └── TesteExerciciosJPA.java
├── principal/
│   └── Principal.java (menu console - backup)
├── ScreenmatchApplication.java          ⭐ ATUALIZADO - Versão Web
└── ScreenmatchApplicationSemWeb.java    (backup versão console)
```

## 🚀 Como Executar

### 1. Configurar variáveis de ambiente

Copie o arquivo `.env.example` para `.env` e preencha com suas credenciais:

```bash
cp .env.example .env
```

Edite o `.env`:
```properties
OMDB_API_KEY=sua-chave-omdb
DB_URL=jdbc:postgresql://localhost:5433/alura_series
DB_USERNAME=postgres
DB_PASSWORD=sua-senha
```

### 2. Executar a aplicação WEB

```bash
mvn spring-boot:run
```

Ou execute a classe `ScreenmatchApplication` pela IDE.

**Servidor iniciará em:** http://localhost:8080

### 3. Testar endpoints REST

**Navegador ou Postman:**
- http://localhost:8080/series → "Nenhuma série informada"
- http://localhost:8080/series?nomedaserie=Lost → "Série informada: Lost"

### 4. Versão Console (Backup)

Para usar a versão console com menu interativo:
1. Descomente `@SpringBootApplication` em `ScreenmatchApplicationSemWeb`
2. Comente a classe `ScreenmatchApplication`
3. Ou altere `<mainClass>` no `pom.xml`

### 5. Menu Console (versão antiga)

- **1** - Buscar séries na API OMDB
- **2** - Buscar episódios de uma série
- **3** - Listar séries buscadas (do banco de dados)
- **4** - Buscar série por título (Derived Query)
- **5** - Buscar séries por ator e avaliação mínima
- **6** - Top 5 séries com melhor avaliação
- **7** - Buscar séries por categoria
- **8** - Filtrar séries por temporadas e avaliação
- **9** - Buscar episódio por trecho (JPQL com JOIN)
- **10** - Top 5 episódios por série (JPQL com ORDER BY)
- **11** - Buscar episódios a partir de uma data (JPQL com YEAR)
- **12** - Ver exercícios resolvidos (Aula 01)
- **13** - Testar exercícios JPQL avançados (11 exercícios)
- **0** - Sair

## 📝 Conceitos Aprendidos

### Aula 01:
- Modelagem de classes e enums
- Conversão de tipos com Optional
- Consumo de APIs REST
- Processamento de JSON com Jackson
- Encapsulamento e boas práticas
- Streams e manipulação de coleções
- Tratamento de erros e exceções

### Aula 02:
- Configuração de banco de dados PostgreSQL
- Mapeamento objeto-relacional (ORM) com Hibernate
- Anotações JPA (@Entity, @Id, @GeneratedValue, @Column, @Transient)
- Repositórios com Spring Data JPA
- Injeção de dependência (@Autowired)
- Variáveis de ambiente para segurança
- Persistência automática de dados
- **Relacionamentos JPA (@OneToMany e @ManyToOne)**
- **Chaves estrangeiras (Foreign Keys)**
- **Persistência em cascata (cascade = CascadeType.ALL)**
- **Fetch types (EAGER vs LAZY)**
- **Relacionamentos bidirecionais (mappedBy)**
- **Relacionamento N:M com @ManyToMany e @JoinTable**

### Aula 04:
- Configuração de aplicação web com Spring Boot
- Dependência spring-boot-starter-web
- Servidor Tomcat embutido
- Arquitetura MVC (Model-View-Controller)
- Controllers REST com @RestController
- Mapeamento de rotas com @GetMapping
- Captura de parâmetros com @RequestParam
- Diferença entre aplicação console e web
- Configuração de classe principal no Maven
- Separação de camadas (Controller, Service, Repository)

### Aula 03:
- **Derived Query Methods** (Spring Data JPA gera SQL automaticamente)
- **Nomenclatura de métodos** (findBy, Containing, IgnoreCase, And, GreaterThanEqual)
- **Busca parcial e case-insensitive** (LIKE %texto%)
- **Queries compostas** com múltiplos critérios (AND, OR)
- **Top N queries** (findTop5, findFirst10)
- **Ordenação** (OrderBy...Desc/Asc)
- **JPQL com JOIN** para buscar episódios
- **JPQL com WHERE usando objeto** (s = :serie)
- **ORDER BY + LIMIT** para top N episódios
- **Função YEAR()** para filtrar por ano
- **Funções agregadas** (AVG, MAX, COUNT)
- **GROUP BY e HAVING** para agrupamentos
- **Função SIZE()** para contar coleções
- **SQL Nativo** com nativeQuery = true
- **Reutilização de variáveis** entre métodos (serieBusca)
- **Otimização**: Busca direta no banco vs lista em memória
- **Tratamento de nulls** da API externa
- **Comparação**: Derived Queries vs JPQL vs SQL Nativo

## 🔗 Links Úteis

- [API OMDB](http://www.omdbapi.com/)
- [API MyMemory](https://mymemory.translated.net/)
- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL](https://www.postgresql.org/)

---

## 📚 Documentação Adicional

- **Readme_aulas.md** - Guia passo a passo de todas as aulas
- **TESTE_TOP_EPISODIOS.md** - Guia de teste da opção 10 (Top 5 episódios)
- **TESTE_EPISODIOS_POR_ANO.md** - Guia de teste da opção 11 (Episódios por ano)
- **exerciciosjpa/README_EXERCICIOS_JPA.md** - Exercícios práticos de JPA
- **exerciciosjpa/COMO_TESTAR.md** - Guia rápido de testes

---

**Desenvolvido por:** Guilherme Falcão  
**Curso:** Alura - Formação Avançando com Java  
**Última atualização:** Aula 04 - Desenvolvimento Web (API REST com Spring Boot)
