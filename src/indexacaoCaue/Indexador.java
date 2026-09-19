package indexacao;

import estrutura.ArvoreTernaria;
import estrutura.ListaArquivos;
import java.io.File;

public class Indexador {

    /**
     * Constrói e retorna a instância de Indice preenchida a partir dos arquivos .txt
     * presentes no caminho fornecido.
     */
    public static Indice construir(String caminhoPasta) {
        long inicio = System.currentTimeMillis();

        Indice indice = new Indice();
        indice.arvore = new ArvoreTernaria();
        indice.todosOsDocumentos = new ListaArquivos();

        String[] arquivos = LeitorArquivos.listarTxt(caminhoPasta);
        if (arquivos.length == 0) {
            indice.qtdArquivos = 0;
            indice.qtdPalavrasDistintas = 0;
            indice.qtdTokens = 0;
            indice.tempoConstrucaoMs = System.currentTimeMillis() - inicio;
            return indice;
        }

        File pasta = new File(caminhoPasta);

        for (String nomeArquivo : arquivos) {
            // Garante que o arquivo existe no universo para operações de negação (NAO)
            indice.todosOsDocumentos.adicionar(nomeArquivo);
            indice.qtdArquivos++;

            File arq = new File(pasta, nomeArquivo);
            String conteudo = LeitorArquivos.lerConteudo(arq);

            if (conteudo == null || conteudo.trim().isEmpty()) {
                continue;
            }

            String[] tokensBrutos = Tokenizador.tokenizar(conteudo);
            for (String tokenBruto : tokensBrutos) {
                String palavraNormalizada = Normalizador.normalizar(tokenBruto);

                if (!palavraNormalizada.isEmpty()) {
                    indice.arvore.inserir(palavraNormalizada, nomeArquivo);
                    indice.qtdTokens++;
                }
            }
        }

        indice.qtdPalavrasDistintas = indice.arvore.totalPalavras();
        indice.tempoConstrucaoMs = System.currentTimeMillis() - inicio;

        return indice;
    }
}
