class ArvoreTernaria {
    private No raiz;
    private int qtdPalavras;
    private int qtdNos;
 
    /** Cria uma arvore vazia. Custo: O(1). */
    public ArvoreTernaria() {
        this.raiz = null;
        this.qtdPalavras = 0;
        this.qtdNos = 0;
    }
 
    // ------------------------------------------------------------------
    // Insercao
    // ------------------------------------------------------------------
    public void inserir(String palavra, String nomeArquivo) {
        if (palavra == null || palavra.isEmpty()) {
            return;
        }
        raiz = inserirRec(raiz, palavra, 0, nomeArquivo);
    }
    private No inserirRec(No atual, String p, int i, String arq) {
        char c = p.charAt(i);
 
        if (atual == null) {
            atual = new No(c);
            qtdNos++;
        }
 
        if (c < atual.caractere) {
            atual.esquerda = inserirRec(atual.esquerda, p, i, arq);
 
        } else if (c > atual.caractere) {
            atual.direita = inserirRec(atual.direita, p, i, arq);
 
        } else {
            if (i + 1 < p.length()) {
                atual.meio = inserirRec(atual.meio, p, i + 1, arq);
            } else {
                if (!atual.fimDaPalavra) {
                    atual.fimDaPalavra = true;
                    qtdPalavras++;
                }
                if (atual.arquivos == null) {
                    atual.arquivos = new ListaArquivos();
                }
                atual.arquivos.adicionar(arq);
            }
        }
        return atual;
    }
 
    // ------------------------------------------------------------------
    // Busca
    // ------------------------------------------------------------------
 
    /**
     * Devolve os arquivos em que a palavra aparece.
     *
     * <p>Implementacao iterativa. Palavra inexistente, palavra que e
     * apenas prefixo de outra, palavra nula ou vazia: todos devolvem
     * lista vazia, nunca {@code null}.</p>
     *
     * <p>O resultado e uma copia: alterar a lista devolvida nao afeta o
     * indice.</p>
     *
     * Custo: O(|palavra| + c), igual ao da insercao.
     */
    public ListaArquivos buscar(String palavra) {
        if (palavra == null || palavra.isEmpty()) {
            return new ListaArquivos();
        }
 
        No atual = raiz;
        int i = 0;
 
        while (atual != null) {
            char c = palavra.charAt(i);
 
            if (c < atual.caractere) {
                atual = atual.esquerda;
 
            } else if (c > atual.caractere) {
                atual = atual.direita;
 
            } else {
                if (i == palavra.length() - 1) {
                    if (atual.fimDaPalavra && atual.arquivos != null) {
                        return atual.arquivos.copiar();
                    }
                    return new ListaArquivos();   // era apenas prefixo
                }
                atual = atual.meio;
                i++;
            }
        }
        return new ListaArquivos();               // caiu em ponteiro nulo
    }
 
    /** Verdadeiro se a palavra esta indexada. Custo: O(|palavra| + c). */
    public boolean contem(String palavra) {
        return !buscar(palavra).estaVazia();
    }
 
    // ------------------------------------------------------------------
    // Metricas
    // ------------------------------------------------------------------
 
    /** Palavras distintas indexadas. Custo: O(1) (contador incremental). */
    public int totalPalavras() {
        return qtdPalavras;
    }
 
    /** Nos alocados. Custo: O(1) (contador incremental). */
    public int totalNos() {
        return qtdNos;
    }
 
    /** Verdadeiro se nada foi inserido. Custo: O(1). */
    public boolean estaVazia() {
        return raiz == null;
    }
 
    /**
     * Altura da arvore. Util no relatorio para evidenciar a degeneracao
     * quando as insercoes ocorrem em ordem alfabetica.
     * Custo: O(N) nos.
     */
    public int altura() {
        return alturaRec(raiz);
    }
 
    private int alturaRec(No no) {
        if (no == null) {
            return 0;
        }
        int e = alturaRec(no.esquerda);
        int m = alturaRec(no.meio);
        int d = alturaRec(no.direita);
        return 1 + Math.max(e, Math.max(m, d));
    }
 
    // ------------------------------------------------------------------
    // Depuracao / apresentacao
    // ------------------------------------------------------------------
 
    /**
     * Devolve todas as palavras indexadas, em ordem alfabetica.
     *
     * <p>Percurso em ordem: esquerda, depois o proprio caractere seguido
     * do meio, depois direita. O caractere e removido do prefixo
     * (backtracking) <b>depois</b> de visitar o meio e <b>antes</b> de
     * visitar a direita — inverter essa ordem gera palavras coladas.</p>
     *
     * <p>Alimenta o comando de depuracao "palavras" da interface e serve
     * como prova visual de que a arvore foi construida corretamente.</p>
     *
     * Custo: O(N) nos, mais o custo de montar as strings.
     */
    public ListaArquivos listarPalavras() {
        ListaArquivos saida = new ListaArquivos();
        coletar(raiz, new StringBuilder(), saida);
        return saida;
    }
 
    private void coletar(No no, StringBuilder prefixo, ListaArquivos saida) {
        if (no == null) {
            return;
        }
        coletar(no.esquerda, prefixo, saida);
 
        prefixo.append(no.caractere);
        if (no.fimDaPalavra) {
            saida.adicionar(prefixo.toString());
        }
        coletar(no.meio, prefixo, saida);
        prefixo.deleteCharAt(prefixo.length() - 1);
 
        coletar(no.direita, prefixo, saida);
    }
 
    /** Resumo textual das metricas, para o comando "stats" e o relatorio. */
    public String estatisticas() {
        return "Palavras distintas: " + qtdPalavras
                + " | Nos alocados: " + qtdNos
                + " | Altura: " + altura();
    }
}