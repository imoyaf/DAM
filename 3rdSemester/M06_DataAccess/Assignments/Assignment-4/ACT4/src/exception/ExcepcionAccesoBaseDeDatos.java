package exception;

public class ExcepcionAccesoBaseDeDatos extends Exception {
  public ExcepcionAccesoBaseDeDatos() {
    super();
  }

  public ExcepcionAccesoBaseDeDatos(String mensaje) {
    super(mensaje);
  }

  public ExcepcionAccesoBaseDeDatos(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }

  public ExcepcionAccesoBaseDeDatos(Throwable causa) {
    super(causa);
  }
}
