package view;

import controller.EmpleadoController;
import controller.IncidenciaController;
import exception.ExcepcionServicio;
import model.Empleado;
import model.Incidencia;
import util.LeerDatos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class IncidenciaView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final IncidenciaController incidenciaController = new IncidenciaController();
    private static final EmpleadoController empleadoController = new EmpleadoController();

    public void mostrarMenuIncidencias() {
        String menuIncidencias = """
        --- Menú de Incidencias ---
        1. Obtener incidencia por ID
        2. Obtener todas las incidencias
        3. Crear nueva incidencia
        4. Ver incidencias creadas por ti
        5. Ver incidencias destinadas a ti
        6. Eliminar incidencia
        7. Volver al menú principal
        """;
        System.out.println(menuIncidencias);
    }

    public int obtenerOpcion() {
        System.out.print("Seleccione una opción: ");
        return LeerDatos.leerInt();
    }

    public void ejecutar(Empleado empleado) {
        int opcion;

        do {
            mostrarMenuIncidencias();
            opcion = obtenerOpcion();

            switch (opcion) {
                case 1:
                    obtenerIncidenciaPorId();
                    break;
                case 2:
                    mostrarTodasLasIncidencias();
                    break;
                case 3:
                    crearNuevaIncidencia(empleado);
                    break;
                case 4:
                    obtenerIncidenciasCreadasPorEmpleado(empleado);
                    break;
                case 5:
                    obtenerIncidenciasDestinadasAEmpleado(empleado);
                    break;
                case 6:
                    eliminarIncidencia();
                    break;
                default:
                    System.out.println("Por favor introduce una opción válida.");
            }
        } while (opcion != 7);
    }

    private void crearNuevaIncidencia(Empleado empleado) {
        Incidencia incidencia = new Incidencia();

        System.out.print("Introduce el ID de la nueva incidencia: ");
        int id = LeerDatos.leerInt();

        System.out.print("Introduce el ID del empleado destino: ");
        int empleadoDestinoId = LeerDatos.leerInt();

        System.out.print("Introduce el detalle de la incidencia: ");
        String detalle = scanner.nextLine();

        System.out.print("Introduce el tipo de incidencia (U para Urgente, N para Normal): ");
        String tipo = LeerDatos.leerTipoIncidencia();

        Empleado empleadoDestino = null;

        try {
            empleadoDestino = empleadoController.buscarEmpleado(empleadoDestinoId);
        } catch (ExcepcionServicio e) {
            System.out.println("No se ha podido obtener el empleado destino. Motivo: " + e.getMessage());
        }

        if (empleadoDestino != null) {
            incidencia.setId(id);
            incidencia.setEmpleadoOrigen(empleado);
            incidencia.setEmpleadoDestino(empleadoDestino);
            incidencia.setDetalle(detalle);
            incidencia.setTipo(tipo);
            incidencia.setFechaHora(LocalDateTime.now());

            try {
                incidenciaController.insertarIncidencia(incidencia);
                System.out.println("Incidencia creada con éxito.");
            } catch (ExcepcionServicio e) {
                System.out.println("No se ha podido insertar la incidencia. Motivo: " + e.getMessage());
            }
        } else {
            System.out.println("Empleado destino no encontrado. La incidencia no se ha creado.");
        }
    }

    private void obtenerIncidenciaPorId() {
        System.out.print("Introduce el ID de la incidencia: ");
        int id = LeerDatos.leerInt();
        try {
            Incidencia incidencia = incidenciaController.obtenerIncidenciaPorId(id);
            if (incidencia != null) {
                System.out.println("Incidencia encontrada: " + incidencia);
            } else {
                System.out.println("No se encontró la incidencia con ID: " + id);
            }
        } catch (ExcepcionServicio e) {
            System.out.println("No se ha podido obtener la incidencia. Motivo: " + e.getMessage());
        }
    }

    private void mostrarTodasLasIncidencias() {
        try {
            List<Incidencia> incidencias = incidenciaController.obtenerTodasLasIncidencias();
            if (incidencias != null && !incidencias.isEmpty()) {
                for (Incidencia incidencia : incidencias) {
                    System.out.println(incidencia);
                }
            } else {
                System.out.println("No hay incidencias registradas.");
            }
        } catch (ExcepcionServicio e) {
            System.out.println("No se ha podido obtener la lista de incidencias. Motivo: " + e.getMessage());
        }
    }

    private void obtenerIncidenciasCreadasPorEmpleado(Empleado empleado) {
        try {
            List<Incidencia> incidencias = incidenciaController.obtenerIncidenciasPorEmpleadoOrigen(empleado);
            if (incidencias != null && !incidencias.isEmpty()) {
                for (Incidencia incidencia : incidencias) {
                    System.out.println(incidencia);
                }
            } else {
                System.out.println("No has creado incidencias.");
            }
        } catch (ExcepcionServicio e) {
            System.out.println("No se ha podido obtener las incidencias. Motivo: " + e.getMessage());
        }
    }

    private void obtenerIncidenciasDestinadasAEmpleado(Empleado empleado) {
        try {
            List<Incidencia> incidencias = incidenciaController.obtenerIncidenciasPorEmpleadoDestino(empleado);
            if (incidencias != null && !incidencias.isEmpty()) {
                for (Incidencia incidencia : incidencias) {
                    System.out.println(incidencia);
                }
            } else {
                System.out.println("No tienes incidencias destinadas.");
            }
        } catch (ExcepcionServicio e) {
            System.out.println("No se ha podido obtener la lista de incidencias. Motivo: " + e.getMessage());
        }
    }

    private void eliminarIncidencia() {
        System.out.print("Introduce el ID de la incidencia: ");
        int id = LeerDatos.leerInt();

        try {
            Incidencia incidencia = incidenciaController.obtenerIncidenciaPorId(id);
            if (incidencia != null) {
                incidenciaController.eliminarIncidencia(incidencia);
                System.out.println("Incidencia con ID " + id + " eliminada.");
            } else {
                System.out.println("No se encontró la incidencia con ID: " + id);
            }
        } catch (ExcepcionServicio e) {
            System.out.println("No se ha podido eliminar la incidencia. Motivo: " + e.getMessage());
        }
    }
}
