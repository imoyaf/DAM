package model;

public class Empleado {
    private int id;
    private String usuario;
    private String contrasena;
    private String nombreCompleto;
    private String telefono;
    private String correoElectronico;
    private String puesto;

    public Empleado(){}

    public Empleado(int id) {
        this.id = id;
    }

    public Empleado(String usuario, String contrasena, String nombreCompleto,
                    String telefono, String correoElectronico, String puesto) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.puesto = puesto;
    }

    public Empleado(int id, String usuario, String contrasena, String nombreCompleto,
                    String telefono, String correoElectronico, String puesto) {
        this.id = id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.puesto = puesto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    @Override
    public String toString() {
        return "Empleado: \n" +
                "id: " + id +
                "\nusuario: " + usuario +
                "\ncontrasena: " + contrasena +
                "\nnombreCompleto: " + nombreCompleto +
                "\ntelefono: " + telefono +
                "\ncorreoElectronico: " + correoElectronico +
                "\npuesto: " + puesto;
    }
}
