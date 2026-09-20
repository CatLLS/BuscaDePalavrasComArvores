package consultaSarms;

public class ConsultaInvalidaException extends Exception {
    public ConsultaInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
