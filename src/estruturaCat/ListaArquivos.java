class ListaArquivos {//auxiliar para o nó
    
    public String[] dados;
    int tamanho;
    
    
    void adicionar(String nomeArquivo);   // sem duplicatas, mantém ordenada
    boolean contem(String nomeArquivo);
    int tamanho();
    String obter(int indice);
    ListaArquivos uniao(ListaArquivos outra);
    ListaArquivos intersecao(ListaArquivos outra);
    ListaArquivos diferenca(ListaArquivos outra); // this - outra
}