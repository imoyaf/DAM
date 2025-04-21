package util;

import exception.ExcepcionAccesoBaseDeDatos;
import org.exist.xmldb.DatabaseImpl;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import java.lang.reflect.InvocationTargetException;

public class ConexionExistDB {

    private static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
    private static final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private static final String USUARIO = "admin";
    private static final String CONTRASENYA = "";

    private static ConexionExistDB instancia;

    private ConexionExistDB() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            XMLDBException, NoSuchMethodException, InvocationTargetException {
        Class<?> cl = Class.forName(DRIVER);
        DatabaseImpl database = (DatabaseImpl) cl.getDeclaredConstructor().newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);
    }

    public static ConexionExistDB getInstancia() throws ExcepcionAccesoBaseDeDatos {
        if (instancia == null) {
            try {
                instancia = new ConexionExistDB();
            } catch (Exception e) {
                throw new ExcepcionAccesoBaseDeDatos("Error al inicializar la conexión con eXist-db", e);
            }
        }
        return instancia;
    }

    public Collection obtenerColeccion(String ruta) throws XMLDBException {
        return DatabaseManager.getCollection(URI + ruta, USUARIO, CONTRASENYA);
    }
}
