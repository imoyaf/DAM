package exception;

public class ExcepcionServicio extends Exception {
    public ExcepcionServicio() {
        super("No se pudo realizar la gestión");
    }

    public ExcepcionServicio(String mensaje) {
        super(mensaje);
    }

    public ExcepcionServicio(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public ExcepcionServicio(Throwable causa) {
        super(causa);
    }
}
