# \ud83e\uddea Guia de Teste - Op\u00e7\u00e3o 11: Buscar Epis\u00f3dios a Partir de Uma Data

## \ud83c\udfaf O que faz:

Busca epis\u00f3dios de uma s\u00e9rie lan\u00e7ados **a partir de um ano espec\u00edfico** usando JPQL com fun\u00e7\u00e3o YEAR().

---

## \ud83d\udccb Pr\u00e9-requisitos:

1. \u2705 S\u00e9rie salva no banco (op\u00e7\u00e3o 1)
2. \u2705 Epis\u00f3dios salvos no banco (op\u00e7\u00e3o 2)

---

## \ud83e\uddea Como testar:

### Cen\u00e1rio 1: Buscar s\u00e9rie primeiro (op\u00e7\u00e3o 4) e depois filtrar por ano (op\u00e7\u00e3o 11)

```
1. Execute a aplica\u00e7\u00e3o
2. Digite: 4 (Buscar s\u00e9rie por t\u00edtulo)
3. Digite: The Boys
4. Digite: 11 (Buscar epis\u00f3dios a partir de uma data)
5. Digite: 2022
```

**Sa\u00edda esperada:**
```
\ud83d\udcc5 Epis\u00f3dios de The Boys a partir de 2022:
Temporada: 3 | Epis\u00f3dio: 1 - Payback | Data: 2022-06-03 | Avalia\u00e7\u00e3o: 8.2
Temporada: 3 | Epis\u00f3dio: 2 - The Only Man in the Sky | Data: 2022-06-03 | Avalia\u00e7\u00e3o: 8.6
Temporada: 3 | Epis\u00f3dio: 3 - Barbary Coast | Data: 2022-06-03 | Avalia\u00e7\u00e3o: 7.9
...
Total de epis\u00f3dios encontrados: 16
```

---

### Cen\u00e1rio 2: Ir direto para op\u00e7\u00e3o 11 (sem buscar s\u00e9rie antes)

```
1. Execute a aplica\u00e7\u00e3o
2. Digite: 11 (Buscar epis\u00f3dios a partir de uma data)
   \u2192 Sistema solicita: \"Escolha uma serie pelo nome:\"
3. Digite: Friends
4. Digite: 2000
```

**Sa\u00edda esperada:**
```
=== DADOS DA S\u00c9RIE ===
T\u00edtulo: Friends
...

\ud83d\udcc5 Epis\u00f3dios de Friends a partir de 2000:
Temporada: 7 | Epis\u00f3dio: 1 - The One with Monica's Thunder | Data: 2000-10-12 | Avalia\u00e7\u00e3o: 8.1
Temporada: 7 | Epis\u00f3dio: 2 - The One with Rachel's Book | Data: 2000-10-12 | Avalia\u00e7\u00e3o: 8.0
...
Total de epis\u00f3dios encontrados: 142
```

---

## \u26a0\ufe0f Poss\u00edveis erros:

### Erro 1: \"Nenhum epis\u00f3dio encontrado\"
```
\u274c Nenhum epis\u00f3dio encontrado para a s\u00e9rie The Boys a partir de 2025
```

**Causa:** Ano muito recente, n\u00e3o h\u00e1 epis\u00f3dios lan\u00e7ados nesse ano  
**Solu\u00e7\u00e3o:** Use um ano mais antigo (ex: 2020, 2022)

---

### Erro 2: \"S\u00e9rie n\u00e3o encontrada\"
```
\u274c S\u00e9rie n\u00e3o encontrada!
```

**Causa:** S\u00e9rie n\u00e3o existe no banco  
**Solu\u00e7\u00e3o:** Use op\u00e7\u00e3o 1 para buscar e salvar a s\u00e9rie

---

## \ud83d\udd0d Verificar no DBeaver:

### Query 1: Ver epis\u00f3dios de uma s\u00e9rie a partir de um ano
```sql
SELECT 
    s.titulo AS serie,
    e.temporada,
    e.numero_episodio,
    e.titulo AS episodio,
    e.data_lancamento,
    e.avaliacao
FROM series s
JOIN episodios e ON s.id = e.serie_id
WHERE s.titulo = 'The Boys'
AND EXTRACT(YEAR FROM e.data_lancamento) >= 2022
ORDER BY e.data_lancamento;
```

### Query 2: Contar epis\u00f3dios por ano de uma s\u00e9rie
```sql
SELECT 
    EXTRACT(YEAR FROM e.data_lancamento) AS ano,
    COUNT(*) AS total_episodios
FROM episodios e
JOIN series s ON e.serie_id = s.id
WHERE s.titulo = 'The Boys'
GROUP BY EXTRACT(YEAR FROM e.data_lancamento)
ORDER BY ano;
```

### Query 3: Ver distribui\u00e7\u00e3o de epis\u00f3dios por ano (todas as s\u00e9ries)
```sql
SELECT 
    s.titulo,
    EXTRACT(YEAR FROM e.data_lancamento) AS ano,
    COUNT(*) AS total_episodios
FROM series s
JOIN episodios e ON s.id = e.serie_id
GROUP BY s.titulo, EXTRACT(YEAR FROM e.data_lancamento)
ORDER BY s.titulo, ano;
```

---

## \ud83c\udfaf Fluxo completo de teste:

```
1. Op\u00e7\u00e3o 1: Buscar \"The Boys\" (salva s\u00e9rie no banco)
   \u2193
2. Op\u00e7\u00e3o 2: Buscar epis\u00f3dios de \"The Boys\" (salva 32 epis\u00f3dios)
   \u2193
3. Op\u00e7\u00e3o 4: Buscar s\u00e9rie \"The Boys\" (armazena em serieBusca)
   \u2193
4. Op\u00e7\u00e3o 11: Buscar epis\u00f3dios a partir de 2022
   \u2192 Exibe epis\u00f3dios das temporadas 3 e 4
   \u2193
5. DBeaver: SELECT * FROM episodios WHERE serie_id = 1 AND EXTRACT(YEAR FROM data_lancamento) >= 2022
   \u2192 Confirma os mesmos epis\u00f3dios
```

---

## \ud83d\udcca Compara\u00e7\u00e3o: Op\u00e7\u00e3o 10 vs Op\u00e7\u00e3o 11

| Aspecto | Op\u00e7\u00e3o 10 (Top 5 Epis\u00f3dios) | Op\u00e7\u00e3o 11 (Epis\u00f3dios por Ano) |
|---------|------------------------|----------------------------|
| **O que busca** | 5 melhores epis\u00f3dios | Epis\u00f3dios a partir de um ano |
| **Crit\u00e9rio** | Avalia\u00e7\u00e3o (DESC) | Ano de lan\u00e7amento (>=) |
| **Ordena\u00e7\u00e3o** | Por avalia\u00e7\u00e3o | Por data de lan\u00e7amento |
| **LIMIT** | 5 epis\u00f3dios | Todos os epis\u00f3dios do per\u00edodo |
| **Fun\u00e7\u00e3o JPQL** | ORDER BY + LIMIT | YEAR() + WHERE |
| **Uso** | Ranking de melhores | Filtro temporal |

---

## \ud83d\udca1 Conceitos aprendidos:

1. **Fun\u00e7\u00e3o YEAR() em JPQL** - Extrai ano de uma data
2. **Filtro por ano** - Mais f\u00e1cil que data completa
3. **Compara\u00e7\u00e3o de datas** - >= para \"a partir de\"
4. **ORDER BY com datas** - Ordena\u00e7\u00e3o cronol\u00f3gica
5. **Reutiliza\u00e7\u00e3o de serieBusca** - Evita busca duplicada

---

## \ud83d\udee0\ufe0f Fun\u00e7\u00f5es JPQL para datas:

| Fun\u00e7\u00e3o | Descri\u00e7\u00e3o | Exemplo |
|---------|-------------|---------|
| `YEAR(data)` | Extrai o ano | `YEAR(e.dataLancamento) = 2022` |
| `MONTH(data)` | Extrai o m\u00eas | `MONTH(e.dataLancamento) = 6` |
| `DAY(data)` | Extrai o dia | `DAY(e.dataLancamento) = 15` |
| `CURRENT_DATE` | Data atual | `e.dataLancamento >= CURRENT_DATE` |
| `CURRENT_TIMESTAMP` | Data e hora atual | `e.dataLancamento <= CURRENT_TIMESTAMP` |

---

**Pronto para testar!** \ud83d\ude80

Execute `mvn spring-boot:run` e teste a op\u00e7\u00e3o 11!
