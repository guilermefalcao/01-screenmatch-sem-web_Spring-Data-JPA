# 🧪 Guia Rápido de Teste - Opções 2 e 4

## 🚀 Passo 1: Iniciar a Aplicação

```bash
cd "c:/1. Guilherme/00. Dataprev/0000. projeto conta/cursoSpringboot/3355-java-screenmatch-com-jpa"
mvn spring-boot:run
```

Aguarde até ver: `Started ScreenmatchApplication`

---

## 📋 Menu da Aplicação

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

## 🧪 TESTE 1: Opção 4 - Buscar série por título

### O que faz:
Busca série **no banco de dados** usando Derived Query Method

### Como testar:

```
Digite: 4
Escolha uma serie pelo nome: boys
```

### O que será exibido:

**Se a série existir no banco:**
```
Dados da série: Serie{id=1, titulo='The Boys', totalTemporadas=4, avaliacao=8.7, genero=ACAO, atores='Karl Urban, Jack Quaid, Antony Starr', poster='https://...', sinopse='...'}
```

**Se a série NÃO existir:**
```
❌ Série não encontrada!
```

### Características da busca:
- ✅ Busca **parcial**: "boys" encontra "The Boys"
- ✅ **Case-insensitive**: "BOYS", "boys", "Boys" funcionam igual
- ✅ Busca **apenas no banco** (não usa API)
- ✅ **Rápido**: SQL otimizado

### SQL gerado automaticamente:
```sql
SELECT * FROM series WHERE LOWER(titulo) LIKE LOWER('%boys%')
```

---

## 🧪 TESTE 2: Opção 2 - Buscar episódios

### O que faz:
1. Lista séries do banco
2. Busca série no banco (usando mesmo método da opção 4)
3. Busca episódios na API OMDB
4. Salva episódios no banco com relacionamento

### Como testar:

```
Digite: 2
```

### O que será exibido:

**Passo 1: Lista de séries**
```
Serie{id=1, titulo='The Boys', totalTemporadas=4, avaliacao=8.7, genero=ACAO}
Serie{id=2, titulo='Friends', totalTemporadas=10, avaliacao=8.9, genero=COMEDIA}
```

**Passo 2: Digite o nome**
```
Digite o nome da série para busca de episódios:
boys
```

**Passo 3: Busca episódios na API**
```
DadosTemporada[numero=1, episodios=[...]]
DadosTemporada[numero=2, episodios=[...]]
DadosTemporada[numero=3, episodios=[...]]
DadosTemporada[numero=4, episodios=[...]]
```

**Passo 4: Salva no banco**
```
✅ Episódios salvos com sucesso! Total: 32
```

### Se a série já tiver episódios:
```
⚠️  Esta série já possui 32 episódios salvos.
Deseja buscar novamente? Isso irá substituir os episódios existentes. (S/N)
```

---

## 🔍 DIFERENÇA ENTRE OPÇÃO 2 E 4

| Aspecto | Opção 2 | Opção 4 |
|---------|---------|---------|
| **Busca série** | No banco (Derived Query) | No banco (Derived Query) |
| **Busca episódios** | ✅ Sim (API OMDB) | ❌ Não |
| **Salva no banco** | ✅ Sim (episódios) | ❌ Não |
| **Velocidade** | Lento (chama API) | Rápido (só banco) |
| **Uso** | Primeira vez | Consulta rápida |

---

## 📊 EVOLUÇÃO DO CÓDIGO

### ANTES (Aula 02):
```java
// Buscava na lista em memória
Optional<Serie> serie = series.stream()
    .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
    .findFirst();
```

**Problemas:**
- ❌ Dependia da lista `series` em memória
- ❌ Lista podia estar desatualizada
- ❌ Menos eficiente

### AGORA (Aula 03 - Derived Query Methods):
```java
// Busca direto no banco
Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
```

**Vantagens:**
- ✅ Busca direto no banco (sempre atualizado)
- ✅ SQL otimizado pelo Spring Data JPA
- ✅ Não depende de lista em memória
- ✅ Mais eficiente

---

## 🔍 Verificar no DBeaver

### Após testar opção 2:

```sql
-- Ver episódios salvos
SELECT 
    s.titulo AS serie,
    e.temporada,
    e.numero_episodio,
    e.titulo AS episodio,
    e.avaliacao
FROM series s
JOIN episodios e ON s.id = e.serie_id
WHERE s.titulo LIKE '%Boys%'
ORDER BY e.temporada, e.numero_episodio;

-- Contar episódios por série
SELECT 
    s.titulo,
    COUNT(e.id) AS total_episodios
FROM series s
LEFT JOIN episodios e ON s.id = e.serie_id
GROUP BY s.titulo;
```

---

## ✅ Checklist de Testes

### Opção 4:
- [ ] Buscar série existente (ex: "boys")
- [ ] Buscar com maiúsculas (ex: "BOYS")
- [ ] Buscar com minúsculas (ex: "boys")
- [ ] Buscar série inexistente (ex: "xyz")
- [ ] Buscar com parte do nome (ex: "gil" para "Gilmore Girls")

### Opção 2:
- [ ] Buscar episódios de série sem episódios salvos
- [ ] Buscar episódios de série que já tem episódios
- [ ] Confirmar substituição de episódios (S)
- [ ] Cancelar substituição de episódios (N)
- [ ] Verificar episódios no DBeaver

---

## 🎯 FLUXO COMPLETO DE TESTE

```
1. Iniciar aplicação (mvn spring-boot:run)
   ↓
2. Opção 1: Buscar "The Boys" (salva no banco)
   ↓
3. Opção 4: Buscar "boys" (consulta rápida)
   → Exibe: Serie{id=1, titulo='The Boys', ...}
   ↓
4. Opção 2: Buscar episódios de "boys"
   → Lista séries
   → Digite: boys
   → Busca episódios na API
   → Salva 32 episódios no banco
   ↓
5. Opção 4: Buscar "boys" novamente (consulta rápida)
   → Exibe mesma série (agora com episódios)
   ↓
6. DBeaver: SELECT * FROM episodios WHERE serie_id = 1
   → Verifica 32 episódios salvos
```

---

## ⚠️ Troubleshooting

### Erro: "Série não encontrada" (Opção 4)
**Causa:** Série não está no banco  
**Solução:** Use opção 1 para buscar e salvar primeiro

### Erro: "Não foi possível buscar episódios da API" (Opção 2)
**Causa:** API key inválida ou limite de requisições  
**Solução:** Verifique `.env` e API key no site OMDB

### Erro: "Esta série já possui X episódios salvos" (Opção 2)
**Causa:** Episódios já foram buscados antes  
**Solução:** Digite S para substituir ou N para cancelar

---

**Pronto para testar!** 🚀

Execute `mvn spring-boot:run` e teste as opções 2 e 4!
