# 📚 Exercícios JPA - Gerenciador de Pedidos

Exercícios práticos da Aula 02 sobre mapeamento JPA, repositórios e persistência de dados.

---

## 🎯 Objetivo

Praticar e comparar funcionalidades da JPA criando classes Produto, Categoria e Pedido com diferentes configurações de mapeamento.

---

## 📁 Estrutura do Projeto

```
exerciciosjpa/
├── model/
│   ├── Produto.java      (Exercícios 1, 2, 3)
│   ├── Categoria.java    (Exercício 4)
│   └── Pedido.java       (Exercício 5)
├── repository/
│   ├── ProdutoRepository.java
│   ├── CategoriaRepository.java
│   └── PedidoRepository.java
└── TesteExerciciosJPA.java (Exercício 8)
```

---

## ✅ Exercícios Resolvidos

### Exercício 1: Classe Produto com @Entity e @Id
**Arquivo:** `model/Produto.java`

```java
@Entity
public class Produto {
    @Id
    private Long id;
    private String nome;
    private Double preco;
}
```

**Conceitos:**
- `@Entity` - Marca como entidade JPA
- `@Id` - Define chave primária

---

### Exercício 2: @GeneratedValue com IDENTITY
**Modificação:** Adicionar geração automática de ID

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

**Conceitos:**
- `@GeneratedValue` - Valor gerado automaticamente
- `GenerationType.IDENTITY` - Usa auto-increment do banco (SERIAL no PostgreSQL)

---

### Exercício 3: Parâmetros de @Column
**Modificações:**
- Nome único e não nulo
- Preço em coluna chamada "valor"

```java
@Column(unique = true, nullable = false)
private String nome;

@Column(name = "valor")
private Double preco;
```

**Conceitos:**
- `unique = true` - Não permite duplicados
- `nullable = false` - Campo obrigatório
- `name = "valor"` - Nome da coluna no banco

---

### Exercício 4: Classe Categoria
**Arquivo:** `model/Categoria.java`

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

---

### Exercício 5: Classe Pedido com LocalDate
**Arquivo:** `model/Pedido.java`

```java
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate data;
}
```

**Conceitos:**
- `LocalDate` - Tipo Java para datas (sem hora)
- JPA converte automaticamente para DATE no PostgreSQL

---

### Exercício 6: Verificar Tabelas Criadas
**Executar aplicação e verificar no DBeaver:**

```sql
-- Ver tabelas criadas
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public';

-- Resultado esperado:
-- series
-- produtos
-- categorias
-- pedidos
```

---

### Exercício 7: Criar Repositórios
**Arquivos:** `repository/ProdutoRepository.java`, `CategoriaRepository.java`, `PedidoRepository.java`

```java
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
```

**Conceitos:**
- Interface estende `JpaRepository<Entidade, TipoId>`
- Métodos automáticos: save(), findAll(), findById(), delete()

---

### Exercício 8: Salvar Dados no Banco
**Arquivo:** `TesteExerciciosJPA.java`

**PASSO IMPORTANTE:** Injeção de dependência!

```java
@Component  // ← Marca como componente Spring
public class TesteExerciciosJPA {
    
    @Autowired  // ← Injeta repositórios
    private ProdutoRepository produtoRepository;
    
    public void executar() {
        Produto produto = new Produto("Notebook", 3500.00);
        produtoRepository.save(produto);  // Salva no banco
    }
}
```

**Por que precisa de @Component e @Autowired?**
- Repositórios só funcionam em classes gerenciadas pelo Spring
- `@Autowired` injeta automaticamente os repositórios
- Sem isso, dá erro: "Cannot instantiate interface"

---

## 🚀 Como Testar

### Opção 1: Pelo Menu da Aplicação (RECOMENDADO)

1. Execute a aplicação (Run)
2. Escolha opção **5** no menu
3. Veja os dados sendo salvos e listados

```
========================================
EXERCÍCIOS JPA - RELACIONAMENTOS
========================================

🗑️  Dados anteriores removidos

✅ Fornecedores salvos
✅ Categorias e Produtos salvos (cascade)
✅ Pedidos salvos com produtos associados

========================================
LISTANDO DADOS COM RELACIONAMENTOS
========================================

📂 CATEGORIAS COM PRODUTOS:
Categoria{id=5, nome='Eletrônicos', produtos=2}
  └─ Produto{id=6, nome='Notebook Dell Inspiron', preco=3500.0}
  └─ Produto{id=7, nome='Monitor Samsung 24"', preco=800.0}

🛒 PEDIDOS COM PRODUTOS:
Pedido{id=4, data=2026-01-27, produtos=2}
  └─ Produto{id=6, nome='Notebook Dell Inspiron'}

✅ TESTES CONCLUÍDOS COM SUCESSO!
```

---

### Opção 2: Verificar no DBeaver

#### Consultas Básicas:
```sql
-- Ver todas as categorias
SELECT * FROM categorias;

-- Ver todos os produtos com relacionamentos
SELECT p.id, p.nome, p.valor, c.nome as categoria, f.nome as fornecedor
FROM produtos p
LEFT JOIN categorias c ON p.categoria_id = c.id
LEFT JOIN fornecedores f ON p.fornecedor_id = f.id;

-- Ver todos os pedidos
SELECT * FROM pedidos;

-- Ver fornecedores
SELECT * FROM fornecedores;
```

#### Consultas de Relacionamentos:
```sql
-- Ver tabela intermediária pedido_produto (N:M)
SELECT * FROM pedido_produto;

-- Ver pedidos com seus produtos
SELECT ped.id as pedido_id, ped.data, p.nome as produto, p.valor
FROM pedidos ped
JOIN pedido_produto pp ON ped.id = pp.pedido_id
JOIN produtos p ON pp.produto_id = p.id
ORDER BY ped.id;

-- Ver produtos por categoria
SELECT c.nome as categoria, COUNT(p.id) as total_produtos
FROM categorias c
LEFT JOIN produtos p ON c.id = p.categoria_id
GROUP BY c.nome;
```

#### Verificar Estrutura das Tabelas:
```sql
-- Estrutura da tabela produtos
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'produtos'
ORDER BY ordinal_position;

-- Ver chaves estrangeiras
SELECT
    tc.table_name, 
    kcu.column_name, 
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name 
FROM information_schema.table_constraints AS tc 
JOIN information_schema.key_column_usage AS kcu
  ON tc.constraint_name = kcu.constraint_name
JOIN information_schema.constraint_column_usage AS ccu
  ON ccu.constraint_name = tc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY';
```

**Resultado esperado (produtos):**
```
column_name   | data_type | is_nullable
--------------|-----------|-------------
id            | bigint    | NO
nome          | varchar   | NO
valor         | double    | YES
categoria_id  | bigint    | YES  (FK → categorias)
fornecedor_id | bigint    | YES  (FK → fornecedores)
```

---

### Opção 3: Verificar Logs do Hibernate

No console, você verá os SQLs gerados:

```sql
Hibernate: 
    create table produtos (
        id bigint generated by default as identity,
        nome varchar(255) not null,
        valor float(53),
        primary key (id)
    )

Hibernate: 
    insert into produtos (nome, valor) values (?, ?)
```

---

## 🔬 Exercício Extra: Comparar GenerationType

### Tipos de Geração de ID:

#### 1. IDENTITY (usado nos exercícios)
```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```
- Usa auto-increment do banco (SERIAL no PostgreSQL)
- Banco gera o ID automaticamente
- Mais eficiente para PostgreSQL

#### 2. AUTO (padrão)
```java
@GeneratedValue(strategy = GenerationType.AUTO)
```
- Hibernate escolhe a melhor estratégia
- Geralmente usa SEQUENCE no PostgreSQL

#### 3. SEQUENCE
```java
@GeneratedValue(strategy = GenerationType.SEQUENCE)
```
- Usa sequência do banco
- Mais controle sobre a geração

#### 4. TABLE
```java
@GeneratedValue(strategy = GenerationType.TABLE)
```
- Usa tabela auxiliar para gerar IDs
- Menos eficiente, mas funciona em qualquer banco

### Teste Prático:

1. Mude o `GenerationType` em Produto.java
2. Execute a aplicação
3. Insira vários produtos
4. Compare no DBeaver:

```sql
-- Ver IDs gerados
SELECT id, nome FROM produtos ORDER BY id;

-- Ver sequências criadas (se usar SEQUENCE)
SELECT * FROM information_schema.sequences;
```

---

## 🔬 Exercício Extra: Comparar @Column

### Parâmetros Testados:

#### 1. unique = true
```java
@Column(unique = true)
private String nome;
```
**Teste:** Tente inserir dois produtos com mesmo nome
**Resultado:** Erro de constraint violation

#### 2. nullable = false
```java
@Column(nullable = false)
private String nome;
```
**Teste:** Tente inserir produto sem nome
**Resultado:** Erro NOT NULL constraint

#### 3. name = "valor"
```java
@Column(name = "valor")
private Double preco;
```
**Teste:** Veja no DBeaver
**Resultado:** Coluna se chama "valor" em vez de "preco"

#### 4. length = 100
```java
@Column(length = 100)
private String nome;
```
**Teste:** Veja no DBeaver
**Resultado:** VARCHAR(100) em vez de VARCHAR(255)

#### 5. precision e scale (para decimais)
```java
@Column(precision = 10, scale = 2)
private Double preco;
```
**Teste:** Insira 1234.567
**Resultado:** Salva 1234.57 (arredonda para 2 casas)

---

## 📊 Tabelas Criadas no Banco

### Tabela: produtos
| Coluna | Tipo | Restrições |
|--------|------|------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| nome | VARCHAR | UNIQUE, NOT NULL |
| valor | DOUBLE | - |

### Tabela: categorias
| Coluna | Tipo | Restrições |
|--------|------|------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| nome | VARCHAR | NOT NULL |

### Tabela: pedidos
| Coluna | Tipo | Restrições |
|--------|------|------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| data | DATE | NOT NULL |

---

## 🎓 Conceitos Aprendidos

### 1. Anotações JPA
- `@Entity` - Marca classe como entidade
- `@Table` - Define nome da tabela
- `@Id` - Chave primária
- `@GeneratedValue` - Geração automática de ID
- `@Column` - Configurações da coluna

### 2. Repositórios
- Interface `JpaRepository<T, ID>`
- Métodos CRUD automáticos
- Injeção de dependência com `@Autowired`

### 3. Persistência
- `save()` - Insere ou atualiza
- `findAll()` - Lista todos
- Hibernate gera SQL automaticamente

### 4. Boas Práticas
- Construtor padrão obrigatório
- Usar `@Component` para classes de teste
- Validações com `@Column`
- Nomes significativos para colunas

---

## ❓ Troubleshooting

### Erro: "Cannot instantiate interface"
**Causa:** Tentou instanciar repositório com `new`
**Solução:** Use `@Autowired` em classe `@Component`

### Erro: "No identifier specified"
**Causa:** Faltou `@Id` na entidade
**Solução:** Adicione `@Id` no campo id

### Erro: "duplicate key value violates unique constraint"
**Causa:** Tentou inserir nome duplicado
**Solução:** Use nomes diferentes ou remova `unique = true`

### Tabelas não são criadas
**Causa:** `ddl-auto` não está configurado
**Solução:** Verifique `application.properties`:
```properties
spring.jpa.hibernate.ddl-auto=update
```

---

## 🔗 Referências

- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Hibernate Annotations](https://docs.jboss.org/hibernate/orm/6.0/userguide/html_single/Hibernate_User_Guide.html#annotations)
- [JPA GenerationType](https://www.baeldung.com/jpa-strategies-when-set-primary-key)

---

**Desenvolvido por:** Guilherme Falcão  
**Curso:** Alura - Formação Avançando com Java  
**Aula:** 02 - Exercícios Práticos de JPA
