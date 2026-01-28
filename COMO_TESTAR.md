# 🧪 Como Testar a Aplicação

Guia completo para testar todas as funcionalidades do projeto.

---

## 🚀 Passo 1: Iniciar a Aplicação

### Opção 1: Pelo IntelliJ IDEA
1. Abra o projeto no IntelliJ
2. Localize a classe `ScreenmatchApplication.java`
3. Clique com botão direito → **Run 'ScreenmatchApplication'**
4. Aguarde a aplicação iniciar (veja no console: "Started ScreenmatchApplication")

### Opção 2: Pelo Maven (Terminal)
```bash
cd "c:/1. Guilherme/00. Dataprev/0000. projeto conta/cursoSpringboot/3355-java-screenmatch-com-jpa"
mvn spring-boot:run
```

### Opção 3: Pelo JAR compilado
```bash
mvn clean package
java -jar target/screenmatch-0.0.1-SNAPSHOT.jar
```

---

## 📋 Menu da Aplicação

Quando a aplicação iniciar, você verá o menu:

```
1 - Buscar séries
2 - Buscar episódios
3 - Listar series buscadas
4 - Buscar série por titulo

5 - Exercícios resolvidos
6 - Testar Exercícios JPA (Produto, Categoria, Pedido)

0 - Sair
```

---

## 🧪 Testando Cada Funcionalidade

### ✅ Opção 1: Buscar séries

**O que faz:** Busca série na API OMDB e salva no banco de dados

**Como testar:**
1. Digite `1` e pressione Enter
2. Digite o nome de uma série (ex: "The Boys")
3. Aguarde a busca na API
4. A série será salva no banco automaticamente

**Resultado esperado:**
```
Digite o nome da série para busca
The Boys
DadosSerie[titulo=The Boys, totalTemporadas=4, avaliacao=8.7, ...]
```

**Verificar no banco (DBeaver):**
```sql
SELECT * FROM series WHERE titulo LIKE '%Boys%';
```

---

### ✅ Opção 2: Buscar episódios

**O que faz:** Busca episódios de uma série já salva no banco

**Pré-requisito:** Ter pelo menos uma série salva (use opção 1 primeiro)

**Como testar:**
1. Digite `2` e pressione Enter
2. Veja a lista de séries disponíveis
3. Digite o nome da série (ex: "Boys")
4. Aguarde a busca dos episódios na API
5. Confirme se deseja salvar (se já existirem episódios)

**Resultado esperado:**
```
Digite o nome da série para busca de episódios:
Boys
✅ Episódios salvos com sucesso! Total: 32
```

**Verificar no banco (DBeaver):**
```sql
-- Ver episódios da série
SELECT 
    s.titulo AS serie,
    e.temporada,
    e.numero_episodio,
    e.titulo AS episodio
FROM series s
JOIN episodios e ON s.id = e.serie_id
WHERE s.titulo LIKE '%Boys%'
ORDER BY e.temporada, e.numero_episodio;
```

---

### ✅ Opção 3: Listar series buscadas

**O que faz:** Lista todas as séries salvas no banco, ordenadas por gênero

**Como testar:**
1. Digite `3` e pressione Enter
2. Veja a lista de séries

**Resultado esperado:**
```
Serie{id=1, titulo='The Boys', totalTemporadas=4, avaliacao=8.7, genero=ACAO}
Serie{id=2, titulo='Friends', totalTemporadas=10, avaliacao=8.9, genero=COMEDIA}
```

---

### ✅ Opção 4: Buscar série por titulo (NOVO!)

**O que faz:** Busca série no banco usando Derived Query Method

**Diferença da opção 1:**
- Opção 1: Busca na API OMDB e salva no banco
- Opção 4: Busca apenas no banco local (mais rápido)

**Como testar:**
1. Digite `4` e pressione Enter
2. Digite parte do nome da série (ex: "boys")
3. Veja os dados da série

**Resultado esperado:**
```
Escolha uma serie pelo nome: 
boys
Dados da série: Serie{id=1, titulo='The Boys', totalTemporadas=4, avaliacao=8.7}
```

**Características:**
- ✅ Busca parcial (não precisa digitar o nome completo)
- ✅ Case-insensitive (ignora maiúsculas/minúsculas)
- ✅ Busca apenas no banco (não usa API)

**Exemplos de busca:**
- "boys" → Encontra "The Boys"
- "FRIENDS" → Encontra "Friends"
- "gil" → Encontra "Gilmore Girls"

---

### ✅ Opção 5: Exercícios resolvidos

**O que faz:** Executa 8 exercícios de manipulação de dados e enums

**Como testar:**
1. Digite `5` e pressione Enter
2. Veja os resultados dos exercícios no console

**Resultado esperado:**
```
========================================
EXERCÍCIO 1: Converter strings para números
========================================
Lista original: [1, 2, abc, 3, 4, def]
Números válidos: [1, 2, 3, 4]
...
```

---

### ✅ Opção 6: Testar Exercícios JPA

**O que faz:** Testa relacionamentos JPA (1:N, N:1, N:M)

**Como testar:**
1. Digite `6` e pressione Enter
2. Veja os dados sendo salvos e listados

**Resultado esperado:**
```
========================================
EXERCÍCIOS JPA - RELACIONAMENTOS
========================================

🗑️  Dados anteriores removidos

✅ Fornecedores salvos
✅ Categorias e Produtos salvos (cascade)
✅ Pedidos salvos com produtos associados

📂 CATEGORIAS COM PRODUTOS:
Categoria{id=5, nome='Eletrônicos', produtos=2}
  └─ Produto{id=6, nome='Notebook Dell Inspiron', preco=3500.0}
```

**Verificar no banco (DBeaver):**
```sql
-- Ver produtos com relacionamentos
SELECT 
    p.nome AS produto,
    p.valor,
    c.nome AS categoria,
    f.nome AS fornecedor
FROM produtos p
LEFT JOIN categorias c ON p.categoria_id = c.id
LEFT JOIN fornecedores f ON p.fornecedor_id = f.id;

-- Ver tabela intermediária (N:M)
SELECT * FROM pedido_produto;
```

---

## 🔍 Verificando Dados no DBeaver

### Conectar ao banco:
1. Abra o DBeaver
2. Conecte ao banco `alura_series`
3. Execute as queries abaixo

### Queries úteis:

```sql
-- Ver todas as tabelas
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public';

-- Ver todas as séries
SELECT * FROM series;

-- Ver episódios com série
SELECT 
    s.titulo AS serie,
    COUNT(e.id) AS total_episodios
FROM series s
LEFT JOIN episodios e ON s.id = e.serie_id
GROUP BY s.titulo;

-- Ver produtos com relacionamentos
SELECT 
    p.nome AS produto,
    c.nome AS categoria,
    f.nome AS fornecedor
FROM produtos p
LEFT JOIN categorias c ON p.categoria_id = c.id
LEFT JOIN fornecedores f ON p.fornecedor_id = f.id;
```

---

## ⚠️ Troubleshooting

### Erro: "API key não encontrada"
**Solução:** Configure a variável de ambiente no arquivo `.env`
```properties
OMDB_API_KEY=sua-chave-aqui
```

### Erro: "Série não encontrada" (Opção 4)
**Causa:** Série não está no banco
**Solução:** Use opção 1 para buscar e salvar a série primeiro

### Erro: "Connection refused"
**Causa:** PostgreSQL não está rodando
**Solução:** Inicie o PostgreSQL
```bash
# Windows
net start postgresql-x64-14

# Linux/Mac
sudo service postgresql start
```

### Erro: "duplicate key value violates unique constraint"
**Causa:** Tentando inserir série com título duplicado
**Solução:** A série já existe no banco, use opção 3 para listar

---

## 📊 Fluxo de Teste Completo

### Cenário 1: Primeira vez usando a aplicação

```
1. Opção 1 → Buscar "The Boys" (salva no banco)
2. Opção 1 → Buscar "Friends" (salva no banco)
3. Opção 3 → Listar séries (vê as 2 séries)
4. Opção 4 → Buscar "boys" (busca no banco)
5. Opção 2 → Buscar episódios de "The Boys"
6. Opção 6 → Testar exercícios JPA
```

### Cenário 2: Testando busca por título

```
1. Opção 4 → Digite "boys" → Encontra "The Boys"
2. Opção 4 → Digite "FRIENDS" → Encontra "Friends"
3. Opção 4 → Digite "xyz" → Não encontra nada
```

### Cenário 3: Testando relacionamentos JPA

```
1. Opção 6 → Executa testes JPA
2. Abrir DBeaver
3. Executar queries para ver relacionamentos
4. Verificar tabelas: produtos, categorias, fornecedores, pedidos, pedido_produto
```

---

## ✅ Checklist de Testes

- [ ] Opção 1: Buscar série na API e salvar no banco
- [ ] Opção 2: Buscar episódios de série existente
- [ ] Opção 3: Listar todas as séries do banco
- [ ] Opção 4: Buscar série por título (busca parcial)
- [ ] Opção 5: Executar exercícios resolvidos
- [ ] Opção 6: Testar relacionamentos JPA
- [ ] Verificar dados no DBeaver (séries, episódios)
- [ ] Verificar relacionamentos JPA no DBeaver
- [ ] Testar busca case-insensitive (opção 4)
- [ ] Testar busca parcial (opção 4)

---

**Pronto para testar!** 🚀

Se encontrar algum erro, verifique:
1. PostgreSQL está rodando?
2. Arquivo `.env` está configurado?
3. Dependências do Maven foram baixadas?
