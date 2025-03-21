package controller;

import exception.BuscarEnArchivoException;
import exception.EliminarEnArchivoException;
import exception.GuardarEnArchivoException;
import model.Empleado;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class EmpleadoController {
    private static EmpleadoController instance;
    private final ObjectContainer db;
    private static final Logger logger = Logger.getLogger(EmpleadoController.class.getName());

    private EmpleadoController() {
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "empleados");
    }

    public static EmpleadoController getInstance() {
        if (instance == null) {
            instance = new EmpleadoController();
        }
        return instance;
    }

    public void insertarEmpleado(Empleado empleado) throws GuardarEnArchivoException {
        try{
            db.store(empleado);
        } catch(Exception e) {
            logger.log(Level.SEVERE,"Excepción al intentar guardar el empleado "
                    + empleado.getNombreCompleto(), e);
            throw new GuardarEnArchivoException("Error al guardar el empleado: "
                    + e.getMessage(), e);
        }
    }

    public Empleado validarEmpleado(String usuario, String contrasena) throws BuscarEnArchivoException {
        Empleado empleado = new Empleado(usuario, contrasena);
        Empleado empleadoValidado;
        try {
            ObjectSet<Empleado> result = db.queryByExample(empleado);
            if(result.hasNext()){
                empleadoValidado = result.next();
            } else {
                empleadoValidado = null;
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Excepción al intentar validar el empleado. ", e);
            throw new BuscarEnArchivoException("Error al intentar validar el empleado. "
                    + e.getMessage(), e);
        }
        return empleadoValidado;
    }

    public void modificarEmpleado(Empleado empleado)
            throws BuscarEnArchivoException, GuardarEnArchivoException {
        int idEmpleado = empleado.getId();
        Empleado empleadoModificar;
        try {
            ObjectSet<Empleado> result = db.queryByExample(new Empleado(idEmpleado));
            empleadoModificar = result.next();
        } catch(Exception e) {
            logger.log(Level.SEVERE,"Excepción al buscar el empleado con ID: "
                    + empleado.getId(), e);
            throw new BuscarEnArchivoException("Error al buscar el empleado con ID: "
                    + empleado.getId() + e.getMessage(), e);
        }

        try {
            empleadoModificar.setNombreCompleto(empleado.getNombreCompleto());
            empleadoModificar.setCorreoElectronico(empleado.getCorreoElectronico());
            empleadoModificar.setTelefono(empleado.getTelefono());
            empleadoModificar.setPuesto(empleado.getPuesto());
            db.store(empleadoModificar);
        } catch(Exception e) {
            logger.log(Level.SEVERE,"Excepción al intentar modificar el empleado con ID: "
                    + empleado.getId(), e);
            throw new GuardarEnArchivoException("Error al modificar el empleado con ID: "
                    + empleado.getId() + e.getMessage(), e);
        }

    }

    public Empleado buscarEmpleado(int id) throws BuscarEnArchivoException {
        Empleado empleadoEncontrado;
        try {
            ObjectSet<Empleado> result = db.queryByExample(new Empleado(id));
            if(result.hasNext()){
                empleadoEncontrado = result.next();
            } else {
                empleadoEncontrado =  null;
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE,"Excepción al intentar modificar el empleado con ID: " + id, e);
            throw new BuscarEnArchivoException("Error al buscar el empleado. " + e.getMessage(), e);
        }
        return empleadoEncontrado;
    }

    public void cambiarContrasena(int id, String nuevaContrasena)
            throws BuscarEnArchivoException, GuardarEnArchivoException {
        Empleado empleadoEncontrado;
        try {
            ObjectSet<Empleado> result = db.queryByExample(new Empleado(id));
            empleadoEncontrado = result.next();
            empleadoEncontrado.setContrasena(nuevaContrasena);
        } catch(Exception e) {
            logger.log(Level.SEVERE,"Excepción al intentar buscar el empleado con ID: "
                    + id, e);
            throw new BuscarEnArchivoException("Error al buscar el empleado con ID: " + id
                    + e.getMessage(), e);
        }

        try {
            db.store(empleadoEncontrado);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Excepción al intentar cambiar la contraseña del empleado con ID: "
                    + id, e);
            throw new GuardarEnArchivoException("Error al cambiar la contraseña del empleado con ID: "
                    + id + e.getMessage(), e);
        }
    }

    public void eliminarEmpleado(int id) throws BuscarEnArchivoException, EliminarEnArchivoException {
        Empleado empleadoEliminar;
        try {
            ObjectSet<Empleado> result = db.queryByExample(new Empleado(id));
            empleadoEliminar = result.next();
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Excepción al intentar buscar el usuario con ID: "
                    + id, e);
            throw new BuscarEnArchivoException("Error al intentar buscar el usuario con ID: "
                    + id + e.getMessage(), e);
        }

        try {
            db.delete(empleadoEliminar);
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Excepción al intentar eliminar el usuario con ID: "
                    + id, e);
            throw new EliminarEnArchivoException("Error al intentar eliminar el usuario con ID: "
                    + id + e.getMessage(), e);
        }
    }

    public List<Empleado> obtenerEmpleados() throws BuscarEnArchivoException {
        List<Empleado> empleados = new ArrayList<>();
        try {
            ObjectSet<Empleado> result = db.queryByExample(Empleado.class);
            while (result.hasNext()) {
                empleados.add(result.next());
            }
            return empleados;
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error al intentar obtener la lista de empleados. ", e);
            throw new BuscarEnArchivoException("Error al intentar obtener la lista de empleados. "
                    + e.getMessage(), e);
        }
    }

    public void cerrarDB() {
        if (!db.ext().isClosed()) {
            db.close();
        }
    }
}
