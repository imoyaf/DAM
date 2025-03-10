package view;

import controller.IncidenciaController;
import controller.EmpleadoController;
import model.Incidencia;
import model.Empleado;
import util.LeerDatos;

import java.util.List;
import java.util.Scanner;

public class IncidenciaView {
    private final Scanner scanner;
    private final IncidenciaController incidenciaController;

    public IncidenciaView() {
        this.scanner = new Scanner(System.in);
        this.incidenciaController = new IncidenciaController();
    }

    public void mostrarMenuIncidencias() {
        String menuIncidencias = """
        --- Menú de Incidencias ---
        1. Obtener incidencia por ID
        2. Obtener todas las incidencias
        3. Crear nueva incidencia
        4. Ver incidencias creadas por ti
        5. Ver incidencias destinadas a ti
        6. Volver al menú principal
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
                    return;
                default:
                    System.out.println("Error, por favor introduce una opción válida.");
            }
        } while (opcion != 6);
    }

    private void obtenerIncidenciaPorId() {
        System.out.print("Introduce el ID de la incidencia: ");
        int id = LeerDatos.leerInt();
        Incidencia incidencia = incidenciaController.obtenerIncidenciaPorId(id);
        if (incidencia != null) {
            System.out.println("Incidencia encontrada: " + incidencia);
        } else {
            System.out.println("No se encontró la incidencia con ID: " + id);
        }
    }

    private void mostrarTodasLasIncidencias() {
        List<Incidencia> incidencias = incidenciaController.obtenerTodasLasIncidencias();
        if (incidencias != null && !incidencias.isEmpty()) {
            for (Incidencia incidencia : incidencias) {
                System.out.println(incidencia);
            }
        } else {
            System.out.println("No hay incidencias registradas.");
        }
    }

    private void crearNuevaIncidencia(Empleado empleado) {
        System.out.print("Introduce el ID del empleado destino: ");
        int empleadoDestinoId = LeerDatos.leerInt();
        System.out.print("Introduce el detalle de la incidencia: ");
        String detalle = scanner.nextLine();
        System.out.print("Introduce el tipo de incidencia (U para Urgente, N para Normal): ");
        String tipo = LeerDatos.leerTipoIncidencia();

        Empleado empleadoDestino = new EmpleadoController().buscarEmpleado(empleadoDestinoId);

        if (empleadoDestino != null) {
            Incidencia incidencia = new Incidencia();
            incidencia.setEmpleadoOrigen(empleado);
            incidencia.setEmpleadoDestino(empleadoDestino);
            incidencia.setDetalle(detalle);
            incidencia.setTipo(tipo);
            incidencia.setFechaHora(new java.sql.Timestamp(System.currentTimeMillis()));

            boolean resultado = incidenciaController.insertarIncidencia(incidencia);
            if (resultado) {
                System.out.println("Incidencia creada con éxito.");
            } else {
                System.out.println("Error al crear la incidencia.");
            }
        } else {
            System.out.println("Empleado destino no encontrado.");
        }
    }

    private void obtenerIncidenciasCreadasPorEmpleado(Empleado empleado) {
        List<Incidencia> incidencias = incidenciaController.obtenerIncidenciasPorEmpleadoOrigen(empleado);
        if (incidencias != null && !incidencias.isEmpty()) {
            for (Incidencia incidencia : incidencias) {
                System.out.println(incidencia);
            }
        } else {
            System.out.println("No has creado incidencias.");
        }
    }

    private void obtenerIncidenciasDestinadasAEmpleado(Empleado empleado) {
        List<Incidencia> incidencias = incidenciaController.obtenerIncidenciasParaEmpleadoDestino(empleado);
        if (incidencias != null && !incidencias.isEmpty()) {
            for (Incidencia incidencia : incidencias) {
                System.out.println(incidencia);
            }
        } else {
            System.out.println("No tienes incidencias destinadas.");
        }
    }
}
