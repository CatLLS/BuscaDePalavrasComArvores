

class No{
    public char caractere; //professor não pediu getters setters e não especificou se precisava ser privado, portanto é público por questões de tempo.
    public No esquerda,meio,direita;
    public boolean fimDaPalavra;
    public ListaArquivos arquivos;//só tem nos nós de fim de palavra, fizemos com uma classe auxiliar criada aqui

    public No(char c){
        this.caractere=c;
        fimDaPalavra = false;
    }
}
