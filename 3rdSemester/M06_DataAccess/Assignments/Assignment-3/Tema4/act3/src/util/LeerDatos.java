package util;

import java.util.Scanner;

public class LeerDatos {
    private static final Scanner scanner = new Scanner(System.in);

    private LeerDatos() {}

    public static int leerInt() {
        int valor = -1;
        boolean entradaValida = false;

        while (!entradaValida) {
            try {
                valor = Integer.parseInt(scanner.nextLine());
                entradaValida = true;
            } catch (NumberFormatException e) {
                System.out.println("¡Error! Debes introducir un número entero válido.");
            }
        }

        return valor;
    }

    public static String leerTipoIncidencia() {
        String tipoIncidencia = null;
        boolean exit = false;
        do {
            String input = scanner.nextLine().toUpperCase();
            if(input.charAt(0) == 'N') {
                tipoIncidencia = "N";
                exit = true;
            } else if(input.charAt(0) == 'U') {
                tipoIncidencia = "U";
                exit = true;
            } else {
                System.out.println("Introduce solo la letra N para Normal o la letra U para Urgente");
            }
        } while (!exit);

        return tipoIncidencia;
    }
}
