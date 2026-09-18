class ArvoreTernaria {
    void inserir(String palavra, String nomeArquivo);
    ListaArquivos buscar(String palavra);  // NUNCA null; lista vazia se não existir
    boolean contem(String palavra);
    int totalPalavras();
    int altura();
}