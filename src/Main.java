import java.util.Scanner;

import consultaSarms.AvaliadorConsulta;
import consultaSarms.ConsultaInvalidaException;
import estruturaCat.ListaArquivos;
import indexacaoCaue.Indexador;
import indexacaoCaue.Indice;

public class Main {

    /** Pasta usada quando nenhum argumento e informado. */
    private static final String PASTA_PADRAO = "../dados";

    public static void main(String[] args) {
        String caminho = (args.length > 0 && !args[0].isBlank())
                ? args[0]
                : PASTA_PADRAO;

        imprimirCabecalho();

        // ---------------- construcao do indice (uma unica vez) ----------
        Indice indice = Indexador.construir(caminho);

        if (indice.qtdArquivos == 0) {
            System.out.println("Nenhum arquivo .txt encontrado em: " + caminho);
            System.out.println();
            System.out.println("Verifique se a pasta existe e contem arquivos .txt.");
            System.out.println("Voce pode informar outra pasta: java Main <pasta>");
            System.out.println("Programa encerrado.");
            return;
        }

        System.out.println("Arquivos encontrados: " + indice.qtdArquivos);
        System.out.println();
        System.out.println("Construindo indice...");
        System.out.println("Indice construido com sucesso.");
        System.out.println("(" + indice.qtdPalavrasDistintas + " palavras distintas em "
                + indice.tempoConstrucaoMs + " ms)");

        // ---------------- laco de consultas -----------------------------
        AvaliadorConsulta avaliador = new AvaliadorConsulta(indice);
        Scanner entrada = new Scanner(System.in);

        try {
            while (true) {
                System.out.println();
                System.out.println("Digite uma consulta:");
                System.out.print("> ");

                if (!entrada.hasNextLine()) {
                    break;                       // fim da entrada (Ctrl+D)
                }
                String linha = entrada.nextLine().trim();

                if (linha.isEmpty()) {
                    continue;                    // apenas Enter
                }

                String comando = linha.toLowerCase();

                if (comando.equals("sair")) {
                    break;
                }
                if (comando.equals("ajuda")) {
                    imprimirAjuda();
                    continue;
                }
                if (comando.equals("stats")) {
                    indice.imprimirEstatisticas();
                    continue;
                }
                if (comando.equals("palavras")) {
                    imprimirPalavras(indice);
                    continue;
                }

                executarConsulta(avaliador, linha);
            }
        } finally {
            entrada.close();                     // fechado UMA vez, fora do laco
        }

        System.out.println();
        System.out.println("Programa encerrado.");
    }

    // ------------------------------------------------------------------
    // Consulta
    // ------------------------------------------------------------------

    private static void executarConsulta(AvaliadorConsulta avaliador, String linha) {
        try {
            long t0 = System.nanoTime();
            ListaArquivos resultado = avaliador.consultar(linha);
            long ms = (System.nanoTime() - t0) / 1_000_000;

            System.out.println();
            if (resultado == null || resultado.estaVazia()) {
                System.out.println("Nenhum arquivo encontrado.");
            } else {
                System.out.println("Arquivos encontrados:");
                System.out.println(resultado);      // um por linha, ja ordenado
                System.out.println();
                System.out.println("(" + resultado.tamanho() + " arquivo(s) em " + ms + " ms)");
            }

        } catch (ConsultaInvalidaException e) {
            System.out.println();
            System.out.println("Consulta invalida: " + e.getMessage());
            System.out.println("Digite 'ajuda' para ver a sintaxe aceita.");

        } catch (RuntimeException e) {
            System.out.println();
            System.out.println("Nao foi possivel processar a consulta: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Comandos auxiliares
    // ------------------------------------------------------------------

    /** Lista todas as palavras indexadas, em ordem alfabetica. */
    private static void imprimirPalavras(Indice indice) {
        ListaArquivos palavras = indice.arvore.listarPalavras();
        System.out.println();
        if (palavras.estaVazia()) {
            System.out.println("O indice esta vazio.");
            return;
        }
        System.out.println("Palavras indexadas (" + palavras.tamanho() + "):");
        System.out.println(palavras);
    }

    // ------------------------------------------------------------------
    // Saida fixa
    // ------------------------------------------------------------------

    private static void imprimirCabecalho() {
        System.out.println("========================================");
        System.out.println("           INDICE INVERTIDO");
        System.out.println("========================================");
        System.out.println();
    }

    private static void imprimirAjuda() {
        System.out.println();
        System.out.println("SINTAXE DAS CONSULTAS");
        System.out.println();
        System.out.println("  palavra                  documentos que contem a palavra");
        System.out.println("  a E b                    contem as duas");
        System.out.println("  a OU b                   contem pelo menos uma");
        System.out.println("  NAO a                    nao contem a palavra");
        System.out.println("  a b                      equivale a 'a E b'");
        System.out.println();
        System.out.println("PRECEDENCIA (da maior para a menor): NAO, E, OU");
        System.out.println("  compra OU venda E casa   equivale a  compra OU (venda E casa)");
        System.out.println("  parenteses alteram a ordem: (casa OU apartamento) E NAO terreno");
        System.out.println();
        System.out.println("A busca ignora maiusculas, minusculas e pontuacao.");
        System.out.println();
        System.out.println("OUTROS COMANDOS");
        System.out.println("  palavras                 lista tudo que foi indexado");
        System.out.println("  stats                    estatisticas do indice");
        System.out.println("  ajuda                    mostra esta tela");
        System.out.println("  sair                     encerra o programa");
    }
}