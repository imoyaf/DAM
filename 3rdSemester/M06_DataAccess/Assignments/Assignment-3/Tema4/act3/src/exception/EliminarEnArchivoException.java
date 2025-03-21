package exception;

public class EliminarEnArchivoException extends Exception {
    public EliminarEnArchivoException(String message) {
        super(message);
    }

    public EliminarEnArchivoException(String message, Throwable cause) {
        super(message, cause);
    }
}
