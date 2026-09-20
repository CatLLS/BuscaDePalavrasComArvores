package indexacaoCaue;

import java.text.Normalizer;
import java.util.Locale;

public class Normalizador {

    /**
     * Normaliza uma string bruta:
     * 1. Converte para minúsculas usando Locale.ROOT.
     * 2. Decompõe caracteres acentuados (NFD) e remove marcas diacríticas.
     * 3. Remove pontuações e caracteres que não sejam letras ou números.
     * Retorna "" caso nada reste.
     */
    public static String normalizar(String bruta) {
        if (bruta == null || bruta.isEmpty()) {
            return "";
        }

        // 1. Minúsculas independente de locale do sistema operacional
        String s = bruta.toLowerCase(Locale.ROOT);

        // 2. Separação de acentos/cedilhas em marcas diacríticas
        s = Normalizer.normalize(s, Normalizer.Form.NFD);

        // 3. Remoção de acentos (marcas diacríticas)
        s = s.replaceAll("\\p{M}", "");

        // 4. Remoção de tudo que não for letra ou número
        s = s.replaceAll("[^\\p{L}\\p{N}]", "");

        return s;
    }
}
