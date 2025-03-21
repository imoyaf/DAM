package exception;

public class GuardarEnArchivoException extends Exception {
    public GuardarEnArchivoException(String message) {
        super(message);
    }

    public GuardarEnArchivoException(String message, Throwable cause) {
        super(message, cause);
    }
}
