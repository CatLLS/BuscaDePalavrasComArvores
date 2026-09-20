package indexacaoCaue;

import estruturaCat.ArvoreTernaria;
import estruturaCat.ListaArquivos;

public class Indice {                 // objeto de integração
    public ArvoreTernaria arvore;
    private ListaArquivos todosOsDocumentos;  // universo, necessário para o NAO
    public int qtdArquivos, qtdPalavrasDistintas, qtdTokens;
    public long tempoConstrucaoMs;

    public ListaArquivos getTodosOsDocumentos() {
        return todosOsDocumentos;
    }

    public ListaArquivos buscar(String palavra) {
        return arvore.buscar(palavra);
    }

    void definirTodosOsDocumentos(ListaArquivos documentos) {
        this.todosOsDocumentos = documentos;
    }

    public void imprimirEstatisticas() {
        System.out.println("Arquivos: " + qtdArquivos);
        System.out.println("Tokens: " + qtdTokens);
        System.out.println(arvore.estatisticas());
        System.out.println("Tempo de construcao: " + tempoConstrucaoMs + " ms");
    }
}