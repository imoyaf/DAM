package model;

import java.sql.*;

public class EmpleadoDAO {

    public void verListadoEmpleados() {
        String query = "SELECT * FROM empleados";
        Empleado empleado;

        try (
                Connection conn = ConexionBBDD.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)
        ) {
            while (rs.next()) {
                empleado = new Empleado(
                        rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getString("nombre_completo"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico"),
                        rs.getString("puesto")
                );

                System.out.println(empleado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insertarEmpleado(Empleado empleado) {
        String query = "INSERT INTO empleados (usuario, contrasena, nombre_completo, telefono, correo_electronico, puesto) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, empleado.getUsuario());
            ps.setString(2, empleado.getContrasena());
            ps.setString(3, empleado.getNombreCompleto());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getCorreoElectronico());
            ps.setString(6, empleado.getPuesto());

            ps.executeUpdate();
            System.out.println("Empleado insertado con éxito");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificarEmpleado(Empleado empleado) {
        String query = "UPDATE empleados SET usuario = ?, contrasena = ?, nombre_completo = ?, telefono = ?," +
                "correo_electronico = ?, puesto = ? WHERE id = ?";

        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, empleado.getUsuario());
            ps.setString(2, empleado.getContrasena());
            ps.setString(3, empleado.getNombreCompleto());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getCorreoElectronico());
            ps.setString(6, empleado.getPuesto());
            ps.setInt(7, empleado.getId());

            int filasModificadas = ps.executeUpdate();

            if (filasModificadas > 0) {
                System.out.println("Empleado modificado con éxito.");
            } else {
                System.out.println("No se encontró un empleado con ese ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cambiarContrasena(int id, String nuevaContrasena) {
        String query = "UPDATE empleados SET contrasena = ? WHERE id = ?";

        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nuevaContrasena);
            ps.setInt(2, id);

            int filasModificadas = ps.executeUpdate();

            if (filasModificadas > 0) {
                System.out.println("Contraseña cambiada con éxito.");
            } else {
                System.out.println("No se encontró un empleado con ese ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarEmpleado(int id) {
        String query = "DELETE FROM empleados WHERE id = ?";

        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);

            int filasModificadas = ps.executeUpdate();

            if (filasModificadas > 0) {
                System.out.println("Empleado eliminado con éxito.");
            } else {
                System.out.println("No se encontró un empleado con ese ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
