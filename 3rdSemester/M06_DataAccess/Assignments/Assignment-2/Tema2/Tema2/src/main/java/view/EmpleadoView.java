package view;

import model.Empleado;

import java.util.Scanner;

import static util.LeerDatos.leerInt;

public class EmpleadoView {

    private final Scanner scanner;

    public EmpleadoView() {
        scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        String menu = """
                --- Menú ---
                1. Ver listado de empleados
                2. Insertar un nuevo empleado
                3. Modificar perfil de un empleado
                4. Cambiar contraseña de un empleado
                5. Eliminar un empleado
                6. Salir
                """;
        System.out.println(menu);
    }

    public int obtenerOpcion() {
        System.out.print("Seleccione una opción: ");
        return leerInt();
    }

    public Empleado obtenerDatosEmpleado() {
        Empleado empleado = new Empleado();

        System.out.print("Usuario: ");
        empleado.setUsuario(scanner.nextLine());

        System.out.print("Contraseña: ");
        empleado.setContrasena(scanner.nextLine());

        System.out.print("Nombre Completo: ");
        empleado.setNombreCompleto(scanner.nextLine());

        System.out.print("Teléfono: ");
        empleado.setTelefono(scanner.nextLine());

        System.out.print("Correo electrónico: ");
        empleado.setCorreoElectronico(scanner.nextLine());

        System.out.print("Puesto: ");
        empleado.setPuesto(scanner.nextLine());

        return empleado;
    }

    public int obtenerIdEmpleado() {
        System.out.print("ID del empleado: ");
        return leerInt();
    }

    public String obtenerNuevaContrasena() {
        System.out.print("Nueva Contraseña: ");
        return scanner.nextLine();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}
