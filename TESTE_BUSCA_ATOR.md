# 🧪 Teste: Opção 5 - Buscar Séries por Ator

## 🚀 Como Testar

### 1. Iniciar a aplicação
```bash
cd "c:/1. Guilherme/00. Dataprev/0000. projeto conta/cursoSpringboot/3355-java-screenmatch-com-jpa"
mvn spring-boot:run
```

---

## 📋 Menu Atualizado

```
1 - Buscar séries
2 - Buscar episódios e salvar no banco
3 - Listar series buscadas
4 - Buscar série por titulo
5 - Buscar series por ator  ← NOVA FUNCIONALIDADE!

6 - Exercícios resolvidos
7 - Testar Exercícios JPA (Produto, Categoria, Pedido)

0 - Sair
```

---

## 🎬 PASSO 1: Ver atores disponíveis no banco

### Opção A: Pelo DBeaver (RECOMENDADO)

```sql
-- Ver todos os atores cadastrados
SELECT titulo, atores FROM series;

-- Ver atores de forma mais legível
SELECT 
    titulo,
    atores
FROM series
ORDER BY titulo;

-- Buscar séries com ator específico
SELECT titulo, atores 
FROM series 
WHERE LOWER(atores) LIKE '%karl%';
```

**Resultado esperado:**
```
titulo          | atores
----------------|--------------------------------------------------
The Boys        | Karl Urban, Jack Quaid, Antony Starr
Friends         | Jennifer Aniston, Courteney Cox, Lisa Kudrow
Gilmore Girls   | Lauren Graham, Alexis Bledel, Melissa McCarthy
```

### Opção B: Pela aplicação (Opção 3)

```
Digite: 3

Serie{id=1, titulo='The Boys', ..., atores='Karl Urban, Jack Quaid, Antony Starr'}
Serie{id=2, titulo='Friends', ..., atores='Jennifer Aniston, Courteney Cox, Lisa Kudrow'}
```

---

## 🧪 PASSO 2: Testar busca por ator

### Teste 1: Buscar por "Karl"

```
Digite: 5
Qual o nome do ator/atriz para busca: 
Karl
```

**Resultado esperado:**
```
✅ Séries encontradas com Karl:
- The Boys (ACAO) - Atores: Karl Urban, Jack Quaid, Antony Starr
```

---

### Teste 2: Buscar por "Jennifer"

```
Digite: 5
Qual o nome do ator/atriz para busca: 
Jennifer
```

**Resultado esperado:**
```
✅ Séries encontradas com Jennifer:
- Friends (COMEDIA) - Atores: Jennifer Aniston, Courteney Cox, Lisa Kudrow
```

---

### Teste 3: Buscar por "jack" (minúsculas)

```
Digite: 5
Qual o nome do ator/atriz para busca: 
jack
```

**Resultado esperado:**
```
✅ Séries encontradas com jack:
- The Boys (ACAO) - Atores: Karl Urban, Jack Quaid, Antony Starr
```

---

### Teste 4: Buscar por ator inexistente

```
Digite: 5
Qual o nome do ator/atriz para busca: 
Tom Cruise
```

**Resultado esperado:**
```
❌ Nenhuma série encontrada com o ator: Tom Cruise
```

---

## 🔍 Como funciona internamente

### 1. Derived Query Method
```java
List<Serie> findByAtoresContainingIgnoreCase(String nomeAtor);
```

### 2. SQL gerado automaticamente
```sql
SELECT * FROM series 
WHERE LOWER(atores) LIKE LOWER('%nomeAtor%')
```

### 3. Fluxo de execução
```
Usuário digita: "Karl"
       ↓
Spring Data JPA gera SQL:
SELECT * FROM series WHERE LOWER(atores) LIKE LOWER('%karl%')
       ↓
PostgreSQL busca no campo 'atores'
       ↓
Retorna: List<Serie> com todas as séries que contêm "Karl"
       ↓
Aplicação formata e exibe resultado
```

---

## 📊 Estrutura do campo 'atores' no banco

### Tabela: series

| Coluna | Tipo | Exemplo |
|--------|------|---------|
| atores | VARCHAR | "Karl Urban, Jack Quaid, Antony Starr" |

**Nota:** O campo 'atores' é uma string com vários nomes separados por vírgula.

---

## 🎯 Exemplos de busca

| Digite | Encontra |
|--------|----------|
| Karl | The Boys (Karl Urban) |
| karl | The Boys (case-insensitive) |
| KARL | The Boys (case-insensitive) |
| Jack | The Boys (Jack Quaid) |
| Jennifer | Friends (Jennifer Aniston) |
| Aniston | Friends (Jennifer Aniston) |
| Lauren | Gilmore Girls (Lauren Graham) |
| xyz | ❌ Nenhuma série encontrada |

---

## 🔍 Queries úteis no DBeaver

### Ver todos os atores
```sql
SELECT titulo, atores FROM series;
```

### Buscar séries com ator específico
```sql
SELECT titulo, atores 
FROM series 
WHERE LOWER(atores) LIKE '%karl%';
```

### Contar séries por ator
```sql
SELECT 
    UNNEST(STRING_TO_ARRAY(atores, ', ')) AS ator,
    COUNT(*) AS total_series
FROM series
GROUP BY ator
ORDER BY total_series DESC;
```

### Ver séries com múltiplos atores
```sql
SELECT 
    titulo,
    ARRAY_LENGTH(STRING_TO_ARRAY(atores, ', '), 1) AS num_atores,
    atores
FROM series
ORDER BY num_atores DESC;
```

---

## 📝 Comparação com outras opções

| Opção | Busca por | Retorna | Tipo |
|-------|-----------|---------|------|
| 4 | Título | Optional<Serie> | Uma série |
| 5 | Ator | List<Serie> | Várias séries |

**Diferença:**
- Opção 4: Retorna **uma** série (Optional)
- Opção 5: Retorna **várias** séries (List)

---

## ✅ Checklist de Testes

- [ ] Buscar por ator existente (ex: "Karl")
- [ ] Buscar com maiúsculas (ex: "KARL")
- [ ] Buscar com minúsculas (ex: "karl")
- [ ] Buscar por sobrenome (ex: "Urban")
- [ ] Buscar por nome parcial (ex: "Jen" para Jennifer)
- [ ] Buscar por ator inexistente (ex: "Tom Cruise")
- [ ] Verificar no DBeaver os atores disponíveis
- [ ] Testar com diferentes séries

---

## 🎬 Fluxo Completo de Teste

```
1. Iniciar aplicação
   ↓
2. Opção 1: Buscar "The Boys" (salva no banco)
   ↓
3. Opção 1: Buscar "Friends" (salva no banco)
   ↓
4. DBeaver: SELECT titulo, atores FROM series
   → Ver atores disponíveis
   ↓
5. Opção 5: Buscar "Karl"
   → Encontra "The Boys"
   ↓
6. Opção 5: Buscar "Jennifer"
   → Encontra "Friends"
   ↓
7. Opção 5: Buscar "xyz"
   → Não encontra nada
```

---

## ⚠️ Troubleshooting

### Erro: "Nenhuma série encontrada"
**Causa:** Ator não está no banco ou nome incorreto  
**Solução:** 
1. Use opção 3 para ver séries disponíveis
2. Ou consulte no DBeaver: `SELECT titulo, atores FROM series`

### Erro: "Cannot invoke findByAtoresContainingIgnoreCase"
**Causa:** Método não foi adicionado no SerieRepository  
**Solução:** Verifique se o método existe em SerieRepository.java

---

## 🚀 Comandos Git para subir alterações

```bash
cd "c:/1. Guilherme/00. Dataprev/0000. projeto conta/cursoSpringboot/3355-java-screenmatch-com-jpa"

git add .

git commit -m "feat: Adicionar busca de séries por ator (Derived Query Method)

- Implementado findByAtoresContainingIgnoreCase no SerieRepository
- Adicionada opção 5 no menu: Buscar series por ator
- Reorganizado menu: opções 6 (Exercícios) e 7 (Testes JPA)
- Busca parcial e case-insensitive no campo atores
- Retorna lista de séries com formatação melhorada
- Criado guia de teste TESTE_BUSCA_ATOR.md"

git push origin desenvolvimento
```

---

**Pronto para testar!** 🎬

Execute `mvn spring-boot:run` e teste a opção 5!
