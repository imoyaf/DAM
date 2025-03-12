package imf.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.util.Date;

@Entity
@Table(name = "incidencias")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "fecha_hora")
    private Date fechaHora;

    @ManyToOne
    @JoinColumn(name = "empleado_origen_id", referencedColumnName = "id", nullable = false)
    private Empleado empleadoOrigen;

    @ManyToOne
    @JoinColumn(name = "empleado_destino_id", referencedColumnName = "id", nullable = false)
    private Empleado empleadoDestino;

    @Column(name = "detalle", nullable = false)
    private String detalle;

    @Column(name = "tipo", nullable = false, length = 1)
    private String tipo;  // 'N' or 'U'

    public Incidencia() {}

    public Incidencia(int id) {
        this.id = id;
    }

    public Incidencia(Date fechaHora, Empleado empleadoOrigen, Empleado empleadoDestino,
                      String detalle, String tipo) {
        this.fechaHora = fechaHora;
        this.empleadoOrigen = empleadoOrigen;
        this.empleadoDestino = empleadoDestino;
        this.detalle = detalle;
        this.tipo = tipo;
    }

    public Incidencia(int id, Date fechaHora, Empleado empleadoOrigen, Empleado empleadoDestino,
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

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
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
