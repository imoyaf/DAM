package exception;

public class BuscarEnArchivoException extends Exception{
    public BuscarEnArchivoException(String message) {
        super(message);
    }

    public BuscarEnArchivoException(String message, Throwable cause) {
        super(message, cause);
    }
}
