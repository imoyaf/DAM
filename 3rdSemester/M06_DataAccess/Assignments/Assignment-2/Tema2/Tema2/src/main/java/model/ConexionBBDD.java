package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBBDD {
    private static final String URL = "jdbc:mysql://localhost:3306/empleados_AAD";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            throw new SQLException("Error al conectar con la base de datos", e);
        }
    }
}
