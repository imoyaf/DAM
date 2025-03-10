package controller;

import model.Empleado;
import model.EmpleadoDAO;
import view.EmpleadoView;

public class EmpleadoController {

    private final EmpleadoDAO empleadoDAO;
    private final EmpleadoView empleadoView;

    public EmpleadoController(EmpleadoDAO empleadoDAO, EmpleadoView empleadoView) {
        this.empleadoDAO = empleadoDAO;
        this.empleadoView = empleadoView;
    }

    public void ejecutar() {
        int opcion;
        do {
            empleadoView.mostrarMenu();
            opcion = empleadoView.obtenerOpcion();

            switch (opcion) {
                case 1:
                    empleadoDAO.verListadoEmpleados();
                    break;
                case 2:
                    Empleado nuevoEmpleado = empleadoView.obtenerDatosEmpleado();
                    empleadoDAO.insertarEmpleado(nuevoEmpleado);
                    empleadoView.mostrarMensaje("Empleado añadido con éxito.");
                    break;

                case 3:
                    int idModificar = empleadoView.obtenerIdEmpleado();
                    Empleado empleadoModificado = empleadoView.obtenerDatosEmpleado();
                    empleadoModificado.setId(idModificar);
                    empleadoDAO.modificarEmpleado(empleadoModificado);
                    empleadoView.mostrarMensaje("Empleado modificado con éxito.");
                    break;

                case 4:
                    int idContrasena = empleadoView.obtenerIdEmpleado();
                    String nuevaContrasena = empleadoView.obtenerNuevaContrasena();
                    empleadoDAO.cambiarContrasena(idContrasena, nuevaContrasena);
                    empleadoView.mostrarMensaje("Contraseña cambiada con éxito.");
                    break;

                case 5:
                    int idEliminar = empleadoView.obtenerIdEmpleado();
                    empleadoDAO.eliminarEmpleado(idEliminar);
                    empleadoView.mostrarMensaje("Empleado eliminado con éxito.");
                    break;

                case 6:
                    empleadoView.mostrarMensaje("¡Gracias por usar la aplicación");
                    break;

                default:
                    empleadoView.mostrarMensaje("Opción no válida, por favor entra una opción válida.");
            }
        } while (opcion != 6);
    }
}
