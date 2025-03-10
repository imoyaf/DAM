package util;

import java.util.Scanner;

public class LeerDatos {

    public static int leerInt() {
        Scanner scanner = new Scanner(System.in);
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
}
