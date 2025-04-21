package controller;

import dao.EmpleadoDAO;
import exception.ExcepcionAccesoBaseDeDatos;
import exception.ExcepcionServicio;
import model.Empleado;

import java.util.List;

public class EmpleadoController {
    private final EmpleadoDAO empleadoDAO;

    public EmpleadoController() {
        this.empleadoDAO = new EmpleadoDAO();
    }

    public void insertarEmpleado(Empleado empleado) throws ExcepcionServicio {
        try {
            empleadoDAO.insertarEmpleado(empleado);
        } catch(ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("EmpleadoDAO no está disponible.");
        }
    }

    public Empleado validarEmpleado(String usuario, String contrasena) throws ExcepcionServicio {
        try {
            return empleadoDAO.validarEmpleado(usuario, contrasena);
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al validar las credenciales del empleado.", e);
        }
    }

    public void modificarEmpleado(Empleado empleado) throws ExcepcionServicio {
        try {
            empleadoDAO.modificarEmpleado(empleado);
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al modificar los datos del empleado.", e);
        }
    }

    public Empleado buscarEmpleado(int id) throws ExcepcionServicio {
        try {
            return empleadoDAO.buscarEmpleado(id);
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al buscar el empleado.", e);
        }
    }

    public void cambiarContrasena(int id, String nuevaContrasena) throws ExcepcionServicio {
        try {
            empleadoDAO.cambiarContrasena(id, nuevaContrasena);
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al cambiar la contraseña del empleado.", e);
        }
    }

    public void eliminarEmpleado(int id) throws ExcepcionServicio {
        try {
            empleadoDAO.eliminarEmpleado(id);
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al eliminar el empleado.", e);
        }
    }

    public List<Empleado> obtenerEmpleados() throws ExcepcionServicio {
        try {
            return empleadoDAO.obtenerEmpleados();
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al obtener la lista de empleados.", e);
        }
    }

}
