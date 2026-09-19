# Gabarito de Validação de Consultas (Índice Invertido)

Este documento serve como referência e gabarito oficial para validação automatizada e apresentação dos testes de recuperação de informação sobre o corpus de documentos de teste (`doc1.txt` a `doc9.txt`).

---

## 1. Resumo dos Documentos da Base

| Arquivo | Descrição / Propósito do Teste |
| :--- | :--- |
| **doc1.txt** | Texto padrão contendo: `contrato`, `compra`, `venda`, `casa`, `multa`, `responsabilidade`. |
| **doc2.txt** | Texto padrão contendo: `contrato`, `aluguel`, `multa`, `responsabilidade`, `apartamento`. |
| **doc3.txt** | Texto padrão contendo: `compra`, `terreno`, `aluguel`, `venda`, `casa`, `contrato`. |
| **doc4.txt** | **Teste 7.1 (Case Insensitivity):** Frase com `Venda`, `VENDA` e `venda`. |
| **doc5.txt** | **Teste 7.2 (Tratamento de Pontuação):** Ocorrências de `venda,`, `venda.` e `venda;`. |
| **doc6.txt** | **Acentuação / Diacríticos:** Contém `locação`, `inadimplência` e `rescisão`. |
| **doc7.txt** | **Robustez (Arquivo Vazio):** 0 bytes / nenhum token gerado. |
| **doc8.txt** | **Robustez (Apenas pontuação e números):** Não deve indexar palavras do vocabulário léxico. |
| **doc9.txt** | **Deduplicação de Posting Lists:** A palavra `casa` repetida 10 vezes (deve registrar apenas uma entrada para o arquivo). |

---

## 2. Tabela de Mapeamento: Palavra → Arquivos Esperados

> **Premissas de pré-processamento padrão consideradas:**
> - Remoção de pontuação.
> - *Case folding* (conversão para minúsculas).
> - Deduplicação de IDs de documentos nas listas invertidas (cada documento aparece no máximo uma vez por termo).

| Palavra-Chave | Arquivos Esperados | Observações |
| :--- | :--- | :--- |
| **contrato** | `doc1.txt`, `doc2.txt`, `doc3.txt` | Vocabulário central |
| **compra** | `doc1.txt`, `doc3.txt` | Interseção parcial |
| **venda** | `doc1.txt`, `doc3.txt`, `doc4.txt`, `doc5.txt` | Cobre `doc4` (7.1) e `doc5` (7.2) |
| **casa** | `doc1.txt`, `doc3.txt`, `doc9.txt` | `doc9` deve aparecer uma única vez |
| **apartamento** | `doc2.txt` | Ocorrência única |
| **terreno** | `doc3.txt` | Ocorrência única |
| **multa** | `doc1.txt`, `doc2.txt` | Interseção comum |
| **responsabilidade** | `doc1.txt`, `doc2.txt` | Interseção comum |
| **aluguel** | `doc2.txt`, `doc3.txt` | Interseção parcial |
| **locação** | `doc6.txt` | Teste de caractere especial (`ç`/`ã`) |
| **inadimplência** | `doc6.txt` | Teste de caractere especial (`ê`) |
| **rescisão** | `doc6.txt` | Teste de caractere especial (`ã`) |

---

## 3. Consultas Booleanas Não-Triviais para Demonstração

Utilize estas consultas compostas para demonstrar operações de interseção (AND), união (OR) e negação (NOT) durante a apresentação e validação:

| Tipo | Consulta (Exemplo) | Expressão / Conjuntos | Resultado Esperado |
| :--- | :--- | :--- | :--- |
| **AND** | `contrato AND aluguel` | `{doc1, doc2, doc3} ∩ {doc2, doc3}` | `doc2.txt`, `doc3.txt` |
| **AND** | `compra AND casa` | `{doc1, doc3} ∩ {doc1, doc3, doc9}` | `doc1.txt`, `doc3.txt` |
| **AND** | `multa AND terreno` | `{doc1, doc2} ∩ {doc3}` | `∅` (Vazio) |
| **OR** | `apartamento OR terreno` | `{doc2} ∪ {doc3}` | `doc2.txt`, `doc3.txt` |
| **AND NOT** | `venda AND NOT casa` | `{doc1, doc3, doc4, doc5} \ {doc1, doc3, doc9}` | `doc4.txt`, `doc5.txt` |
| **Três termos** | `(contrato AND venda) AND NOT compra` | `({doc1, doc2, doc3} ∩ {doc1, doc3, doc4, doc5}) \ {doc1, doc3}` | `∅` (Vazio) |
| **Três termos** | `(contrato AND venda) AND responsabilidade` | `{doc1, doc3} ∩ {doc1, doc2}` | `doc1.txt` |

---

## 4. Casos de Borda e Verificações Obrigatórias

1. **Testes de Normalização de Caixa (7.1):**
   - A busca por `venda`, `Venda` ou `VENDA` deve retornar exatamente a mesma lista, contendo explicitamente `doc4.txt`.
2. **Testes de Pontuação (7.2):**
   - Pontuações coladas aos tokens em `doc5.txt` (`venda,`, `venda.`, `venda;`) não devem gerar termos espúrios como `venda,`.
3. **Deduplicação de Posting:**
   - Para a palavra `casa`, o tamanho da lista invertida deve contar `doc9.txt` exatamente uma vez, ignorando as 9 repetições subsequentes.
4. **Arquivos Neutros:**
   - Consultas válidas nunca devem apontar para `doc7.txt` ou `doc8.txt`.
