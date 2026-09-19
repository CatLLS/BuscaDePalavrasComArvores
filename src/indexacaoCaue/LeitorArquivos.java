package indexacao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LeitorArquivos {

    /**
     * Lista e ordena alfabeticamente os nomes dos arquivos com extensão .txt
     * dentro do diretório especificado.
     */
    public static String[] listarTxt(String caminhoPasta) {
        if (caminhoPasta == null) {
            return new String[0];
        }

        File pasta = new File(caminhoPasta);
        if (!pasta.exists() || !pasta.isDirectory()) {
            return new String[0];
        }

        // Filtro case-insensitive para extensões .txt
        FilenameFilter filtroTxt = (dir, name) -> name.toLowerCase(java.util.Locale.ROOT).endsWith(".txt");

        String[] arquivos = pasta.list(filtroTxt);
        if (arquivos == null || arquivos.length == 0) {
            return new String[0];
        }

        Arrays.sort(arquivos);
        return arquivos;
    }

    /**
     * Lê o conteúdo completo de um arquivo em UTF-8 com fallback para ISO-8859-1.
     * Trata o caractere BOM (\uFEFF) e normaliza quebras de linha para LF.
     * Retorna null se houver falha de leitura (IOException).
     */
    public static String lerConteudo(File arquivo) {
        if (arquivo == null || !arquivo.exists() || !arquivo.isFile()) {
            return null;
        }

        // Tentativa de leitura primária em UTF-8
        try {
            String conteudo = lerComEncoding(arquivo, StandardCharsets.UTF_8.name());
            
            // Fallback se encontrar o caractere de substituição Unicode (sinal de encoding incompatível)
            if (conteudo.contains("\uFFFD")) {
                conteudo = lerComEncoding(arquivo, StandardCharsets.ISO_8859_1.name());
            }

            // Remove Byte Order Mark (BOM) se presente
            if (conteudo.startsWith("\uFEFF")) {
                conteudo = conteudo.substring(1);
            }

            // Normaliza quebras de linha CRLF para LF
            return conteudo.replace("\r\n", "\n");

        } catch (IOException e) {
            // Em caso de falha de leitura, pula o arquivo sem quebrar a execução
            return null;
        }
    }

    private static String lerComEncoding(File arquivo, String charset) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), charset))) {
            char[] buffer = new char[4096];
            int lidos;
            while ((lidos = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, lidos);
            }
        }
        return sb.toString();
    }
}
