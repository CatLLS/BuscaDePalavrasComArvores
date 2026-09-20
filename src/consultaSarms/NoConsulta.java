package consultaSarms;


public class NoConsulta {
    public final TokenConsulta.Tipo tipo; // Define se esse nó é uma operação (E, OU, NAO) ou um dado (PALAVRA)
    public final String palavra;
    public final NoConsulta esquerdo; // Ramo esquerdo da expressão
    public final NoConsulta direito;  // Ramo direito da expressão

    public NoConsulta(TokenConsulta.Tipo tipo, String palavra, NoConsulta esquerdo, NoConsulta direito) {
        this.tipo = tipo;
        this.palavra = palavra;
        this.esquerdo = esquerdo;
        this.direito = direito;
    }
}