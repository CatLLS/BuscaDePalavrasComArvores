package consultaSarms;

public class TokenConsulta {
    // Categorias possíveis para cada fragmento de texto lido
    public enum Tipo {
        E, OU, NAO, ABRE_PARENTESE, FECHA_PARENTESE, PALAVRA, EOF
    }

    public final Tipo tipo;
    public final String valor; // Guarda o texto original

    public TokenConsulta(Tipo tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }
    
    @Override
    public String toString() {
        return tipo + (valor != null ? "(" + valor + ")" : "");
    }
}