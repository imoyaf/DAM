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
        Collection collection = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

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

            service.execute(service.compile(xquery));

            System.out.println("Empleado insertado correctamente en empleados.xml");

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al insertar empleado: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }

    public Empleado validarEmpleado(String usuario, String contrasena) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;
        Empleado empleado = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

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

            var result = service.query(xquery);
            var iterator = result.getIterator();

            if (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                String contenido = res.getContent().toString();

                // Muy básico: lo ideal sería parsear con DOM o JAXB, pero lo hacemos por extracción directa
                empleado = new Empleado();
                empleado.setUsuario(usuario);
                empleado.setContrasena(contrasena);

                if (contenido.contains("id=\"")) {
                    int id = Integer.parseInt(contenido.replaceAll(".*id=\"(\\d+)\".*", "$1"));
                    empleado.setId(id);
                }
                if (contenido.contains("<nombre_completo>")) {
                    empleado.setNombreCompleto(contenido.replaceAll(".*<nombre_completo>(.+)</nombre_completo>.*", "$1"));
                }
                if (contenido.contains("<telefono>")) {
                    empleado.setTelefono(contenido.replaceAll(".*<telefono>(.+)</telefono>.*", "$1"));
                }
                if (contenido.contains("<correo_electronico>")) {
                    empleado.setCorreoElectronico(contenido.replaceAll(".*<correo_electronico>(.+)</correo_electronico>.*", "$1"));
                }
                if (contenido.contains("<puesto>")) {
                    empleado.setPuesto(contenido.replaceAll(".*<puesto>(.+)</puesto>.*", "$1"));
                }
            }

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al validar empleado: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }

        return empleado;
    }

    public void modificarEmpleado(Empleado empleado) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

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

            service.execute(service.compile(xquery));
            System.out.println("Empleado modificado correctamente.");

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al modificar empleado: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }

    public Empleado buscarEmpleado(int id) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;
        Empleado empleado = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

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

            var result = service.query(xquery);
            var iterator = result.getIterator();

            if (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                String contenido = res.getContent().toString();

                empleado = new Empleado();
                empleado.setId(id);

                if (contenido.contains("<usuario>")) {
                    empleado.setUsuario(contenido.replaceAll(".*<usuario>(.+)</usuario>.*", "$1"));
                }
                if (contenido.contains("<contrasena>")) {
                    empleado.setContrasena(contenido.replaceAll(".*<contrasena>(.+)</contrasena>.*", "$1"));
                }
                if (contenido.contains("<nombre_completo>")) {
                    empleado.setNombreCompleto(contenido.replaceAll(".*<nombre_completo>(.+)</nombre_completo>.*", "$1"));
                }
                if (contenido.contains("<telefono>")) {
                    empleado.setTelefono(contenido.replaceAll(".*<telefono>(.+)</telefono>.*", "$1"));
                }
                if (contenido.contains("<correo_electronico>")) {
                    empleado.setCorreoElectronico(contenido.replaceAll(".*<correo_electronico>(.+)</correo_electronico>.*", "$1"));
                }
                if (contenido.contains("<puesto>")) {
                    empleado.setPuesto(contenido.replaceAll(".*<puesto>(.+)</puesto>.*", "$1"));
                }
            }

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al buscar empleado por ID: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }

        return empleado;
    }

    public void cambiarContrasena(int id, String nuevaContrasena) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

            String xquery = String.format("""
            let $empleado := doc("%s")/empleados/empleado[@id = %d]
            return update replace $empleado/contrasena with <contrasena>%s</contrasena>
            """, DOCUMENTO, id, nuevaContrasena);

            service.execute(service.compile(xquery));
            System.out.println("Contraseña actualizada correctamente para el empleado con ID " + id);

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al cambiar la contraseña del empleado: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }

    public void eliminarEmpleado(int id) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

            String xquery = String.format("""
            update delete doc("%s")/empleados/empleado[@id = %d]
            """, DOCUMENTO, id);

            service.execute(service.compile(xquery));
            System.out.println("Empleado eliminado correctamente.");

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al eliminar empleado: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Empleado> obtenerEmpleados() throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;
        List<Empleado> empleados = new ArrayList<>();

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

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

            var result = service.execute(service.compile(xquery));
            var iterator = result.getIterator();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            while (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                String contenido = res.getContent().toString();

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

                empleados.add(empleado);
            }

        } catch (Exception e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al obtener los empleados: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }

        return empleados;
    }

    private String getText(Element padre, String etiqueta) {
        NodeList nodos = padre.getElementsByTagName(etiqueta);
        if (nodos.getLength() > 0) {
            return nodos.item(0).getTextContent();
        }
        return "";
    }
}
