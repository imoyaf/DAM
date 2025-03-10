package view;

import util.LeerDatos;
import controller.EmpleadoController;
import model.Empleado;

import java.util.List;
import java.util.Scanner;

public class EmpleadoView {

    private static final Scanner scanner = new Scanner(System.in);
    private static final EmpleadoController empleadoController = new EmpleadoController();
    private static final IncidenciaView incidenciaView = new IncidenciaView();

    public void mostrarMenu() {
        String menu = """
        --- Menú principal---
        1. Añadir nuevo empleado
        2. Validar empleado (login)
        3. Modificar empleado
        4. Cambiar contraseña
        5. Eliminar empleado
        6. Ver todos los empleados
        7. Salir
        """;
        System.out.println(menu);
    }

    public int obtenerOpcion() {
        System.out.print("Seleccione una opción: ");
        return LeerDatos.leerInt();
    }

    public void ejecutar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = obtenerOpcion();
            switch (opcion) {
                case 1:
                    insertarEmpleado();
                    break;
                case 2:
                    validarEmpleado();
                    break;
                case 3:
                    modificarEmpleado();
                    break;
                case 4:
                    cambiarContrasena();
                    break;
                case 5:
                    eliminarEmpleado();
                    break;
                case 6:
                    verTodosEmpleados();
                    break;
                case 7:
                    System.out.println("Gracias por usar la aplicación");
                    break;
                default:
                    System.out.println("Error, por favor introduce una opción válida.");
            }
        } while (opcion != 7);
    }

    private static void insertarEmpleado() {
        System.out.println("\n--- Insertar Nuevo Empleado ---");
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();
        System.out.print("Nombre completo: ");
        String nombreCompleto = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();
        System.out.print("Puesto: ");
        String puesto = scanner.nextLine();

        Empleado empleado = new Empleado(usuario, contrasena, nombreCompleto, telefono, correo, puesto);
        empleadoController.insertarEmpleado(empleado);
        System.out.println("Empleado insertado correctamente.");
    }

    private static void validarEmpleado() {
        System.out.println("\n--- Validar Empleado (Login) ---");
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        Empleado empleado = empleadoController.validarEmpleado(usuario, contrasena);
        if (empleado != null) {
            System.out.println("Login realizado con éxito, " + empleado.getNombreCompleto());
            incidenciaView.ejecutar(empleado);
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    private static void modificarEmpleado() {
        System.out.println("\n--- Modificar Empleado ---");
        System.out.print("Ingrese el ID del empleado a modificar: ");
        int id = LeerDatos.leerInt();
        Empleado empleadoExistente = empleadoController.buscarEmpleado(id);
        if (empleadoExistente == null) {
            System.out.println("Error: No existe un empleado con el ID proporcionado.");
            return;
        }
        System.out.print("Nuevo nombre completo: ");
        String nombreCompleto = scanner.nextLine();
        System.out.print("Nuevo teléfono: ");
        String telefono = scanner.nextLine();
        System.out.print("Nuevo correo electrónico: ");
        String correo = scanner.nextLine();
        System.out.print("Nuevo puesto: ");
        String puesto = scanner.nextLine();
        String usuario = empleadoController.buscarEmpleado(id).getUsuario();
        String contrasena = empleadoController.buscarEmpleado(id).getContrasena();

        Empleado empleado = new Empleado(id, usuario, contrasena, nombreCompleto, telefono, correo, puesto);
        empleadoController.modificarEmpleado(empleado);
        System.out.println("Empleado modificado correctamente.");
    }

    private static void cambiarContrasena() {
        System.out.println("\n--- Cambiar Contraseña ---");
        System.out.print("Ingrese el ID del empleado: ");
        int id = LeerDatos.leerInt();
        Empleado empleadoExistente = empleadoController.buscarEmpleado(id);
        if (empleadoExistente == null) {
            System.out.println("Error: No existe un empleado con el ID proporcionado.");
            return;
        }
        System.out.print("Nueva contraseña: ");
        String nuevaContrasena = scanner.nextLine();
        empleadoController.cambiarContrasena(id, nuevaContrasena);
        System.out.println("Contraseña cambiada correctamente.");
    }

    private static void eliminarEmpleado() {
        System.out.println("\n--- Eliminar Empleado ---");
        System.out.print("Ingrese el ID del empleado a eliminar: ");
        int id = LeerDatos.leerInt();
        Empleado empleadoExistente = empleadoController.buscarEmpleado(id);
        if (empleadoExistente == null) {
            System.out.println("Error: No existe un empleado con el ID proporcionado.");
            return;
        }
        empleadoController.eliminarEmpleado(id);
        System.out.println("Empleado eliminado correctamente.");
    }

    private static void verTodosEmpleados() {
        System.out.println("\n--- Ver Todos los Empleados ---");
        List<Empleado> empleados = empleadoController.obtenerEmpleados();
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
        } else {
            for (Empleado empleado : empleados) {
                System.out.println(empleado);
            }
        }
    }
}