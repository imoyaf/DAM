package model;

import java.time.LocalDateTime;

public class Incidencia {
    private int id;
    private LocalDateTime fechaHora;
    private Empleado empleadoOrigen;
    private Empleado empleadoDestino;
    private String detalle;
    private String tipo;

    public Incidencia() {}

    public Incidencia(int id) {
        this.id = id;
    }

    public Incidencia(int id, LocalDateTime fechaHora, Empleado empleadoOrigen, Empleado empleadoDestino,
                      String detalle, String tipo) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.empleadoOrigen = empleadoOrigen;
        this.empleadoDestino = empleadoDestino;
        this.detalle = detalle;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Empleado getEmpleadoOrigen() {
        return empleadoOrigen;
    }

    public void setEmpleadoOrigen(Empleado empleadoOrigen) {
        this.empleadoOrigen = empleadoOrigen;
    }

    public Empleado getEmpleadoDestino() {
        return empleadoDestino;
    }

    public void setEmpleadoDestino(Empleado empleadoDestino) {
        this.empleadoDestino = empleadoDestino;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Incidencia: \n" +
                "id: " + id +
                "\nfechaHora: " + fechaHora +
                "\nempleadoOrigen: " + empleadoOrigen.getNombreCompleto() +
                "\nempleadoDestino: " + empleadoDestino.getNombreCompleto() +
                "\ndetalle: " + detalle +
                "\ntipo: " + tipo;
    }
}
