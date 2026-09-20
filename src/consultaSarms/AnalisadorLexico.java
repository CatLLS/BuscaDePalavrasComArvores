package consultaSarms;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {
    public static List<TokenConsulta> tokenizar(String consulta) {
        if (consulta == null || consulta.trim().isEmpty()) {
            throw new IllegalArgumentException("Consulta vazia."); 
        }

        // Garante que parênteses colados a palavras sejam separados corretamente.
        // Ex: "(casa)" transforma-se em " ( casa ) ", permitindo o split por espaços.
        String processada = consulta.replace("(", " ( ").replace(")", " ) ");
        
        // Fatiamento da string usando espaços em branco como delimitador
        String[] partes = processada.trim().split("\\s+");
        
        List<TokenConsulta> tokens = new ArrayList<>();
        
        for (String p : partes) {
            if (p.isEmpty()) continue;
            
            String pLower = p.toLowerCase();
            
            // Classificação do token lido
            switch (pLower) {
                case "e":
                    tokens.add(new TokenConsulta(TokenConsulta.Tipo.E, p));
                    break;
                case "ou":
                    tokens.add(new TokenConsulta(TokenConsulta.Tipo.OU, p));
                    break;
                case "nao":
                case "não":
                    tokens.add(new TokenConsulta(TokenConsulta.Tipo.NAO, p));
                    break;
                case "(":
                    tokens.add(new TokenConsulta(TokenConsulta.Tipo.ABRE_PARENTESE, p));
                    break;
                case ")":
                    tokens.add(new TokenConsulta(TokenConsulta.Tipo.FECHA_PARENTESE, p));
                    break;
                default:
                    // Se não for operador nem parêntese, é uma palavra de busca.
                    String normalizada = Normalizador.normalizar(p); 
                    if (normalizada.isEmpty()) {
                        throw new IllegalArgumentException("Palavra inválida após normalização: " + p);
                    }
                    tokens.add(new TokenConsulta(TokenConsulta.Tipo.PALAVRA, normalizada));
                    break;
            }
        }
        // Marcador de fim de ficheiro/leitura para controle de paragem no analisador sintático
        tokens.add(new TokenConsulta(TokenConsulta.Tipo.EOF, ""));
        return tokens;
    }
}