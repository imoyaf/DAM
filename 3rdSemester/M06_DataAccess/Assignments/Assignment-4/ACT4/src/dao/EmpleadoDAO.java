package dao;

import exception.ExcepcionAccesoBaseDeDatos;
import model.Empleado;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import util.ConexionExistDB;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XQueryService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    private static final String RUTA_COLECCION = "/db/incidenciasApp/empleados";
    private static final String DOCUMENTO = "empleados.xml";

    public void insertarEmpleado(Empleado empleado) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            update insert
                <empleado id="%d">
                    <usuario>%s</usuario>
                    <contrasena>%s</contrasena>
                    <nombre_completo>%s</nombre_completo>
                    <telefono>%s</telefono>
                    <correo_electronico>%s</correo_electronico>
                    <puesto>%s</puesto>
                </empleado>
            into doc("%s")/empleados
            """,
                empleado.getId(),
                empleado.getUsuario(),
                empleado.getContrasena(),
                empleado.getNombreCompleto(),
                empleado.getTelefono(),
                empleado.getCorreoElectronico(),
                empleado.getPuesto(),
                DOCUMENTO
        );

        ejecutarXQuerySinResultado(xquery, "Error al insertar empleado");
    }

    public Empleado validarEmpleado(String usuario, String contrasena) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            for $e in doc("%s")/empleados/empleado
            where $e/usuario = '%s' and $e/contrasena = '%s'
            return
                <empleado id="{$e/@id}">
                    <usuario>{$e/usuario/text()}</usuario>
                    <contrasena>{$e/contrasena/text()}</contrasena>
                    <nombre_completo>{$e/nombre_completo/text()}</nombre_completo>
                    <telefono>{$e/telefono/text()}</telefono>
                    <correo_electronico>{$e/correo_electronico/text()}</correo_electronico>
                    <puesto>{$e/puesto/text()}</puesto>
                </empleado>
            """, DOCUMENTO, usuario, contrasena);

        try {
            return ejecutarConsultaEmpleadoUnico(xquery);
        } catch (Exception e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al validar empleado", e);
        }
    }

    public void modificarEmpleado(Empleado empleado) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            let $empleado := doc("%s")/empleados/empleado[@id = %d]
            return (
                update replace $empleado/nombre_completo with <nombre_completo>%s</nombre_completo>,
                update replace $empleado/telefono with <telefono>%s</telefono>,
                update replace $empleado/correo_electronico with <correo_electronico>%s</correo_electronico>,
                update replace $empleado/puesto with <puesto>%s</puesto>
            )
            """,
                DOCUMENTO,
                empleado.getId(),
                empleado.getNombreCompleto(),
                empleado.getTelefono(),
                empleado.getCorreoElectronico(),
                empleado.getPuesto()
        );

        ejecutarXQuerySinResultado(xquery, "Error al modificar empleado");
    }

    public Empleado buscarEmpleado(int id) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            for $e in doc("%s")/empleados/empleado[@id = %d]
            return
                <empleado id="{$e/@id}">
                    <usuario>{$e/usuario/text()}</usuario>
                    <contrasena>{$e/contrasena/text()}</contrasena>
                    <nombre_completo>{$e/nombre_completo/text()}</nombre_completo>
                    <telefono>{$e/telefono/text()}</telefono>
                    <correo_electronico>{$e/correo_electronico/text()}</correo_electronico>
                    <puesto>{$e/puesto/text()}</puesto>
                </empleado>
            """, DOCUMENTO, id);

        try {
            return ejecutarConsultaEmpleadoUnico(xquery);
        } catch (Exception e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al buscar empleado", e);
        }
    }

    public void cambiarContrasena(int id, String nuevaContrasena) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            let $empleado := doc("%s")/empleados/empleado[@id = %d]
            return update replace $empleado/contrasena with <contrasena>%s</contrasena>
            """, DOCUMENTO, id, nuevaContrasena);

        ejecutarXQuerySinResultado(xquery, "Error al cambiar la contraseña del empleado");
    }

    public void eliminarEmpleado(int id) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            update delete doc("%s")/empleados/empleado[@id = %d]
            """, DOCUMENTO, id);

        ejecutarXQuerySinResultado(xquery, "Error al eliminar empleado");
    }

    public List<Empleado> obtenerEmpleados() throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            for $e in doc("%s")/empleados/empleado
            return
                <empleado id="{$e/@id}">
                    <usuario>{$e/usuario/text()}</usuario>
                    <contrasena>{$e/contrasena/text()}</contrasena>
                    <nombre_completo>{$e/nombre_completo/text()}</nombre_completo>
                    <telefono>{$e/telefono/text()}</telefono>
                    <correo_electronico>{$e/correo_electronico/text()}</correo_electronico>
                    <puesto>{$e/puesto/text()}</puesto>
                </empleado>
            """, DOCUMENTO);

        try {
            return ejecutarConsultaEmpleados(xquery);
        } catch (Exception e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al obtener empleados", e);
        }
    }

    private XQueryService obtenerServicio(Collection collection) throws XMLDBException {
        XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
        service.setProperty(OutputKeys.INDENT, "yes");
        service.setProperty(OutputKeys.ENCODING, "UTF-8");
        return service;
    }

    private String getText(Element padre, String etiqueta) {
        NodeList nodos = padre.getElementsByTagName(etiqueta);
        if (nodos.getLength() > 0) {
            return nodos.item(0).getTextContent();
        }
        return "";
    }

    private Empleado parsearEmpleadoDesdeXML(String contenido) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(contenido)));
        Element root = doc.getDocumentElement();

        Empleado empleado = new Empleado();
        empleado.setId(Integer.parseInt(root.getAttribute("id")));
        empleado.setUsuario(getText(root, "usuario"));
        empleado.setContrasena(getText(root, "contrasena"));
        empleado.setNombreCompleto(getText(root, "nombre_completo"));
        empleado.setTelefono(getText(root, "telefono"));
        empleado.setCorreoElectronico(getText(root, "correo_electronico"));
        empleado.setPuesto(getText(root, "puesto"));

        return empleado;
    }

    private List<Empleado> ejecutarConsultaEmpleados(String xquery) throws Exception {
        Collection collection = null;
        List<Empleado> empleados = new ArrayList<>();

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);
            XQueryService service = obtenerServicio(collection);

            var result = service.execute(service.compile(xquery));
            var iterator = result.getIterator();

            while (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                empleados.add(parsearEmpleadoDesdeXML(res.getContent().toString()));
            }

        } finally {
            if (collection != null) collection.close();
        }

        return empleados;
    }

    private Empleado ejecutarConsultaEmpleadoUnico(String xquery) throws Exception {
        Collection collection = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);
            XQueryService service = obtenerServicio(collection);

            var result = service.execute(service.compile(xquery));
            var iterator = result.getIterator();

            if (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                return parsearEmpleadoDesdeXML(res.getContent().toString());
            }

            return null;

        } finally {
            if (collection != null) collection.close();
        }
    }

    private void ejecutarXQuerySinResultado(String xquery, String mensajeError) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);
            XQueryService service = obtenerServicio(collection);
            service.execute(service.compile(xquery));

        } catch (Exception e) {
            throw new ExcepcionAccesoBaseDeDatos(mensajeError + ": " + e.getMessage(), e);

        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }
}
