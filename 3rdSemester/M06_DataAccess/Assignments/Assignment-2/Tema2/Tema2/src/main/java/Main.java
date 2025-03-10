import controller.EmpleadoController;
import model.EmpleadoDAO;
import view.EmpleadoView;

public class Main {
    public static void main(String[] args) {
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();
        EmpleadoView empleadoView = new EmpleadoView();
        EmpleadoController empleadoController = new EmpleadoController(empleadoDAO, empleadoView);

        empleadoController.ejecutar();
    }
}

