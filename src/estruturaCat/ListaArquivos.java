class ListaArquivos {//auxiliar para o nó
    private static final int CAPACIDADE_INICIAL = 4;
 
    private String[] dados;
    private int tamanho;
    public ListaArquivos() {
        this(CAPACIDADE_INICIAL);
    }
    private ListaArquivos(int capacidade) {
        this.dados = new String[Math.max(capacidade, 1)];
        this.tamanho = 0;
    }
 
    private int buscaBinaria(String alvo) {
        int ini = 0;
        int fim = tamanho - 1;
        while (ini <= fim) {
            int meio = (ini + fim) >>> 1;
            int cmp = dados[meio].compareTo(alvo);
            if (cmp == 0) {
                return meio;
            } else if (cmp < 0) {
                ini = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return -(ini) - 1;
    }
 
    /** Dobra a capacidade quando o vetor enche. Amortizado O(1) por insercao. */
    private void garantirCapacidade() {
        if (tamanho == dados.length) {
            String[] novo = new String[dados.length * 2];
            System.arraycopy(dados, 0, novo, 0, tamanho);
            dados = novo;
        }
    }
    private void anexar(String nomeArquivo) {
        garantirCapacidade();
        dados[tamanho++] = nomeArquivo;
    }
    // ------------------------------------------------------------------
    // Operacoes basicas
    // ------------------------------------------------------------------
 
    /**
     * Insere mantendo a ordem alfabetica e sem gerar duplicatas.
     *
     * como o indexador processa os arquivos em
     * ordem alfabetica, toda ocorrencia repetida de uma palavra dentro do
     * mesmo arquivo cai no atalho do ultimo elemento e e resolvida em O(1)
     * com uma unica comparacao. A busca binaria so roda na primeira
     * ocorrencia da palavra naquele arquivo.
     *
     * Custo: O(1) no caso dominante; O(log k) para localizar mais O(k) de
     * deslocamento no pior caso.
     */
    public void adicionar(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.isEmpty()) {
            return;
        }
 
        if (tamanho > 0) {
            int cmpUltimo = dados[tamanho - 1].compareTo(nomeArquivo);
            if (cmpUltimo == 0) {
                return;           // repeticao dentro do mesmo arquivo
            }
            if (cmpUltimo < 0) {
                anexar(nomeArquivo);  // maior que todos: entra no fim
                return;
            }
        }
 
        int pos = buscaBinaria(nomeArquivo);
        if (pos >= 0) {
            return;               // ja existe
        }
        pos = -pos - 1;
 
        garantirCapacidade();
        System.arraycopy(dados, pos, dados, pos + 1, tamanho - pos);
        dados[pos] = nomeArquivo;
        tamanho++;
    }
 
    /** Verifica presenca por busca binaria. Custo: O(log k). */
    public boolean contem(String nomeArquivo) {
        return nomeArquivo != null && buscaBinaria(nomeArquivo) >= 0;
    }
 
    public int tamanho() {
        return tamanho;
    }
 
    public boolean estaVazia() {
        return tamanho == 0;
    }
 
    public String obter(int indice) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException(
                    "Indice " + indice + " fora do intervalo [0, " + tamanho + ")");
        }
        return dados[indice];
    }
 
    public ListaArquivos copiar() {
        ListaArquivos c = new ListaArquivos(Math.max(tamanho, 1));
        System.arraycopy(dados, 0, c.dados, 0, tamanho);
        c.tamanho = tamanho;
        return c;
    }
 
    // ------------------------------------------------------------------
    // Operacoes de conjunto (base dos operadores E, OU e NAO)
    // ------------------------------------------------------------------
 
    /**
     * Uniao — operador <b>OU</b>. Intercalacao de duas listas ordenadas.
     * Custo: O(a + b).
     */
    public ListaArquivos uniao(ListaArquivos outra) {
        if (outra == null) {
            return this.copiar();
        }
        ListaArquivos r = new ListaArquivos(this.tamanho + outra.tamanho + 1);
        int i = 0;
        int j = 0;
        while (i < this.tamanho && j < outra.tamanho) {
            int cmp = this.dados[i].compareTo(outra.dados[j]);
            if (cmp < 0) {
                r.anexar(this.dados[i++]);
            } else if (cmp > 0) {
                r.anexar(outra.dados[j++]);
            } else {
                r.anexar(this.dados[i++]);
                j++;                       // comum: entra uma vez so
            }
        }
        while (i < this.tamanho) {
            r.anexar(this.dados[i++]);
        }
        while (j < outra.tamanho) {
            r.anexar(outra.dados[j++]);
        }
        return r;
    }
 
    /**
     * Intersecao — operador <b>E</b>. Anexa somente os elementos comuns.
     * Custo: O(a + b).
     */
    public ListaArquivos intersecao(ListaArquivos outra) {
        if (outra == null) {
            return new ListaArquivos();
        }
        ListaArquivos r = new ListaArquivos(Math.min(this.tamanho, outra.tamanho) + 1);
        int i = 0;
        int j = 0;
        while (i < this.tamanho && j < outra.tamanho) {
            int cmp = this.dados[i].compareTo(outra.dados[j]);
            if (cmp < 0) {
                i++;
            } else if (cmp > 0) {
                j++;
            } else {
                r.anexar(this.dados[i++]);
                j++;
            }
        }
        return r;
    }
 
    public ListaArquivos diferenca(ListaArquivos outra) {
        if (outra == null) {
            return this.copiar();
        }
        ListaArquivos r = new ListaArquivos(this.tamanho + 1);
        int i = 0;
        int j = 0;
        while (i < this.tamanho && j < outra.tamanho) {
            int cmp = this.dados[i].compareTo(outra.dados[j]);
            if (cmp < 0) {
                r.anexar(this.dados[i++]);
            } else if (cmp > 0) {
                j++;
            } else {
                i++;                       // presente nos dois: descarta
                j++;
            }
        }
        while (i < this.tamanho) {
            r.anexar(this.dados[i++]);
        }
        return r;
    }
 
    // ------------------------------------------------------------------
    // Saida
    // ------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            if (i > 0) {
                sb.append(System.lineSeparator());
            }
            sb.append(dados[i]);
        }
        return sb.toString();
    }
}