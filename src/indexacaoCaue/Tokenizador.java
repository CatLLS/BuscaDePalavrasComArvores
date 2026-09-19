package indexacao;

public class Tokenizador {

    /**
     * Quebra o texto bruto em tokens com base em sequências de caracteres não alfanuméricos.
     * Tokens brutos são retornados; a normalização e descarte de vazios
     * devem ser feitos por quem chama.
     */
    public static String[] tokenizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return new String[0];
        }

        // Quebra em qualquer sequência que não seja letra ou número
        return texto.split("[^\\p{L}\\p{N}]+");
    }
}
