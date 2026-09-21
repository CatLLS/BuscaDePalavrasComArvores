# Relatório do projeto

## Objetivo

- O projeto implementa um índice invertido para buscar palavras em vários arquivos `.txt`;
- Os arquivos são lidos, normalizados e inseridos em uma árvore ternária;
- Depois, o usuário pode fazer consultas com `E`, `OU`, `NAO` e parênteses.

## Funcionamento

- O `Indexador` percorre os arquivos da pasta `dados`, separa as palavras e remove diferenças de maiúsculas, acentos e pontuação, de forma que cada palavra fica associada aos arquivos onde aparece;
-  A árvore ternária organiza as palavras e permite buscá-las;
- As listas de arquivos fazem as operações de união, interseção e diferença, usadas respectivamente por `OU`, `E` e `NAO`;
- O programa também trata arquivos vazios, palavras repetidas e consultas com parênteses;
- A execução começa compilando `Main.java` dentro da pasta `src` e depois iniciando o menu de consultas.

## Análise de complexidade

- As ordens abaixo são baseadas nos laços e nas chamadas dos arquivos do projeto. Considere:
    - `t`: total de caracteres dos arquivos;
    - `F`: quantidade de arquivos `.txt`;
    - `p`: tamanho de uma palavra;
    - `N`: quantidade de nós da árvore ternária;
    - `k`: quantidade de nomes em uma `ListaArquivos`;
    - `q`: quantidade de tokens de uma consulta.

### Leitura e preparação

- **Listagem dos arquivos `O(F log F)`**:
    - `File.list` precisa examinar os arquivos da pasta e `Arrays.sort` ordena os nomes;
    - A ordenação domina quando `F` é grande.
- **Leitura do conteúdo: `O(t)`**:
    - `lerComEncoding` percorre o arquivo inteiro em blocos e copia cada caractere uma vez;
    O fallback para ISO-8859-1 também é linear no arquivo, mas só ocorre quando aparece o caractere de substituição.
- **Tokenização: `O(t)`**:
    - `Tokenizador.tokenizar` aplica um `split` sobre o texto;
    - O tamanho processado é proporcional ao número de caracteres.
- **Normalização: `O(p)` por token e `O(t)` no total**:
    - `Normalizador` faz conversão de caixa, normalização Unicode e duas substituições que percorrem o texto da palavra.

### Árvore ternária

- **Inserção de uma palavra:**
    - `O(N)` no pior caso;
    - `inserirRec` compara o caractere atual e pode seguir para a esquerda ou direita por uma cadeia de nós, além de avançar pelo ponteiro `meio` para cada caractere;
    - Como a árvore não é balanceada, uma sequência de inserções desfavorável pode fazer a busca lateral atravessar muitos nós;
    - Em uma árvore lateral razoavelmente equilibrada, o custo costuma ser próximo de `O(p log N)` ou, de forma simplificada para palavras curtas, próximo de `O(p)`.
- **Busca de uma palavra:**
    - `O(N)` no pior caso e `O(p)` quando os caminhos laterais são curtos;
    - O método `buscar` visita no máximo um caminho por comparação, mas a ausência de balanceamento permite uma cadeia lateral com muitos nós;
    - Quando encontra o fim da palavra, ainda copia a lista de arquivos.
- **Cópia da lista encontrada:**
    - `O(k)`, porque `copiar` usa `System.arraycopy` para os `k` nomes;
    Portanto, uma busca completa custa `O(N + k)` no pior caso, ou `O(p + k)` quando o caminho da árvore é favorável.
- **Listagem das palavras:**
    - `O(N + s)`, onde `s` é o total de caracteres das palavras produzidas;
    - `coletar` visita cada nó uma vez; além disso, cria as strings no percurso.

### Listas de arquivos

- **Inserção com nome maior que o último:**
    - `O(1)` amortizado;
    - O código apenas compara com o último elemento e chama `anexar`;
    - A expansão do vetor copia os elementos, mas a capacidade dobra, então esse custo é `O(1)` amortizado por inserção e `O(k)` em uma expansão isolada.
- **Inserção em outra posição:**
    - `O(k)`;
    - `buscaBinaria` encontra a posição em `O(log k)`, mas `System.arraycopy` pode deslocar quase todos os elementos, dominando o custo.
- **Inserção repetida no mesmo arquivo:**
    - `O(1)`, pois a comparação com o último elemento encerra o método antes da busca binária;
    - Isso elimina duplicatas dentro da lista de uma palavra.
- **Busca de um arquivo:**
    - `O(log k)`;
    - `buscaBinaria` corta pela metade o intervalo a cada comparação.
- **União, interseção e diferença:**
    - `O(k1 + k2)`;
    - Cada método usa dois índices e avança pelo menos um deles a cada iteração;
    - Portanto, cada entrada das duas listas é examinada no máximo uma vez.
- **Cópia e conversão para texto:**
    - `O(k)`, desconsiderando o tamanho dos nomes;
    - `copiar` copia o vetor e `toString` percorre a lista para montar a saída.

### Consultas e construção do índice

- **Análise léxica:**
    - `O(c)`, onde `c` é o tamanho em caracteres da consulta;
    O texto é separado, cada token é classificado e cada palavra é normalizada uma vez.
- **Análise sintática:**
    - `O(q)`;
    - Os métodos recursivos consomem cada token uma vez, respeitando a precedência `NAO`, `E`, `OU` e os parênteses.
- **Avaliação de uma consulta:**
    - Para cada folha, há uma busca na árvore e uma cópia da lista encontrada;
    - Para cada operador binário, a união ou interseção custa `O(k1 + k2)`;
    - Para `NAO`, a diferença percorre a lista de todos os documentos e a lista do operando;
    - Em uma consulta completa, esses custos se somam para os nós da árvore sintática.
- **Construção do índice:**
    - A leitura e a preparação custam `O(t + F log F)`;
    - Para cada token, o indexador chama a inserção na árvore e adiciona o arquivo à lista da palavra;
    Assim, o pior caso é `O(t + F log F + nN + custo das listas)`, onde `n` é o número de tokens;
    - No uso normal, como os arquivos são processados em ordem e as listas geralmente recebem o próximo nome no final, o custo das listas é próximo de `O(n)` amortizado;
    - O custo da árvore continua dependendo do formato dos caminhos laterais.

## Conclusão

- A implementação é eficiente nas listas ordenadas e nas árvores ternárias com caminhos laterais curtos, mas não oferece a garantia de `O(p)` de uma árvore ternária balanceada;
- Como a árvore deste projeto não possui balanceamento, o pior caso deve ser apresentado como linear no número de nós.

```sh
javac -sourcepath src -cp src src/Main.java
cd src/ && java Main
```
