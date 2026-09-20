package consultaSarms;

import java.util.List;

// Codifica a precedência matemática (NAO > E > OU) através das chamadas de método.
public class AnalisadorSintatico {
    private final List<TokenConsulta> tokens;
    private int pos = 0; // Ponteiro de estado da leitura dos tokens

    public AnalisadorSintatico(List<TokenConsulta> tokens) {
        this.tokens = tokens;
    }

    // Retorna o token atual sem avançar o ponteiro
    private TokenConsulta atual() {
        return tokens.get(pos);
    }

    // Verifica se o token atual corresponde à gramática esperada e avança. 
    // Caso contrário, lança erro de sintaxe.
    private void consumir(TokenConsulta.Tipo esperado) {
        if (atual().tipo == esperado) {
            pos++;
        } else {
            throw new IllegalArgumentException("Erro de sintaxe. Esperado " + esperado + " mas encontrou " + atual().tipo);
        }
    }

    public NoConsulta analisar() {
        NoConsulta raiz = analisarExpressao();
        // Se a expressão foi resolvida e sobram tokens, há parênteses abertos a mais.
        if (atual().tipo != TokenConsulta.Tipo.EOF) {
            throw new IllegalArgumentException("Erro de sintaxe: parênteses desbalanceados ou operadores sobrando.");
        }
        return raiz;
    }

    // Lida com o operador de menor precedência (OU).
    private NoConsulta analisarExpressao() {
        NoConsulta esq = analisarTermo();
        
        while (atual().tipo == TokenConsulta.Tipo.OU) {
            consumir(TokenConsulta.Tipo.OU);
            NoConsulta dir = analisarTermo();
            //agrupa os nós lidos como ramos de um operador raiz OU.
            esq = new NoConsulta(TokenConsulta.Tipo.OU, null, esq, dir);
        }
        return esq;
    }

    // Lida com o operador intermediário (E) e injeta o E-Implicito.
    private NoConsulta analisarTermo() {
        NoConsulta esq = analisarFator();
        
        // Laço acionado por um 'E' explícito ou pela justaposição de termos (palavras, NAO, parênteses)
        while (atual().tipo == TokenConsulta.Tipo.E || atual().tipo == TokenConsulta.Tipo.PALAVRA 
               || atual().tipo == TokenConsulta.Tipo.NAO || atual().tipo == TokenConsulta.Tipo.ABRE_PARENTESE) {
            
            if (atual().tipo == TokenConsulta.Tipo.E) {
                consumir(TokenConsulta.Tipo.E);
                
                // Impede falhas de segmentação caso a consulta termine do nada (ex: "casa E")
                if (atual().tipo == TokenConsulta.Tipo.EOF || atual().tipo == TokenConsulta.Tipo.OU || atual().tipo == TokenConsulta.Tipo.FECHA_PARENTESE) {
                    throw new IllegalArgumentException("Operador 'E' sem operando à direita.");
                }
            }
            
            NoConsulta dir = analisarFator();
            esq = new NoConsulta(TokenConsulta.Tipo.E, null, esq, dir);
        }
        return esq;
    }

    // Lida com a maior precedência: Operador Unário (NAO), Parênteses (sub-árvores) e Folhas (Palavras).
    private NoConsulta analisarFator() {
        if (atual().tipo == TokenConsulta.Tipo.NAO) {
            consumir(TokenConsulta.Tipo.NAO);
            
            if (atual().tipo == TokenConsulta.Tipo.EOF || atual().tipo == TokenConsulta.Tipo.OU || atual().tipo == TokenConsulta.Tipo.E) {
                throw new IllegalArgumentException("Operador 'NAO' sem operando.");
            }
            // Operadores unários geram nós com apenas um ramo (filho direito).
            return new NoConsulta(TokenConsulta.Tipo.NAO, null, null, analisarFator());
            
        } else if (atual().tipo == TokenConsulta.Tipo.ABRE_PARENTESE) {
            consumir(TokenConsulta.Tipo.ABRE_PARENTESE);
            NoConsulta no = analisarExpressao(); // Avalia o escopo isolado antes de prosseguir
            consumir(TokenConsulta.Tipo.FECHA_PARENTESE);
            return no;
            
        } else if (atual().tipo == TokenConsulta.Tipo.PALAVRA) {
            String palavra = atual().valor;
            consumir(TokenConsulta.Tipo.PALAVRA);
            // Retorna um nó folha contendo apenas a string da pesquisa
            return new NoConsulta(TokenConsulta.Tipo.PALAVRA, palavra, null, null);
            
        } else {
            throw new IllegalArgumentException("Sintaxe inválida: operador inesperado (" + atual().valor + ")");
        }
    }
}