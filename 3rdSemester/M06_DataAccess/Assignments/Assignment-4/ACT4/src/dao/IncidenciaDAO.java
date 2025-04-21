package dao;

import exception.ExcepcionAccesoBaseDeDatos;
import model.Empleado;
import model.Incidencia;
import util.ConexionExistDB;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XQueryService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDAO {

    private static final String RUTA_COLECCION = "/db/incidenciasApp/incidencias";
    private static final String DOCUMENTO = "incidencias.xml";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public void insertarIncidencia(Incidencia incidencia) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            update insert
                <incidencia id="%d">
                    <fechaHora>%s</fechaHora>
                    <empleadoOrigen ref="%d"/>
                    <empleadoDestino ref="%d"/>
                    <detalle>%s</detalle>
                    <tipo>%s</tipo>
                </incidencia>
            into doc("%s")/incidencias
            """,
                incidencia.getId(),
                incidencia.getFechaHora().format(FORMATO_FECHA),
                incidencia.getEmpleadoOrigen().getId(),
                incidencia.getEmpleadoDestino().getId(),
                incidencia.getDetalle(),
                incidencia.getTipo(),
                DOCUMENTO
        );

        ejecutarXQuerySinResultado(xquery, "Error al insertar la incidencia");
    }

    public Incidencia obtenerIncidenciaPorId(int id) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            for $i in doc("%s")/incidencias/incidencia[@id = %d]
            return $i
            """, DOCUMENTO, id);

        try {
            return ejecutarConsultaIncidenciaUnica(xquery);
        } catch (Exception e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al obtener la incidencia por ID", e);
        }
    }

    public List<Incidencia> obtenerTodasLasIncidencias() throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            for $i in doc("%s")/incidencias/incidencia
            return $i
            """, DOCUMENTO);

        try {
            return ejecutarConsultaIncidencias(xquery);
        } catch (Exception e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al obtener todas las incidencias", e);
        }
    }

    public List<Incidencia> obtenerIncidenciasPorEmpleadoOrigen(Empleado empleado) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
        for $i in doc("%s")/incidencias/incidencia[empleadoOrigen/@ref = %d]
        return $i
        """, DOCUMENTO, empleado.getId());

        try {
            return ejecutarConsultaIncidencias(xquery);
        } catch (Exception e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al obtener incidencias por empleado origen", e);
        }
    }

    public List<Incidencia> obtenerIncidenciasPorEmpleadoDestino(Empleado empleado) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
        for $i in doc("%s")/incidencias/incidencia[empleadoDestino/@ref = %d]
        return $i
        """, DOCUMENTO, empleado.getId());

        try {
            return ejecutarConsultaIncidencias(xquery);
        } catch (Exception e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al obtener incidencias por empleado destino", e);
        }
    }

    public void eliminarIncidencia(Incidencia incidencia) throws ExcepcionAccesoBaseDeDatos {
        String xquery = String.format("""
            update delete doc("%s")/incidencias/incidencia[@id = %d]
            """, DOCUMENTO, incidencia.getId());

        ejecutarXQuerySinResultado(xquery, "Error al eliminar la incidencia");
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

    private Incidencia parsearIncidenciaDesdeXML(String contenido) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(contenido)));
        Element root = doc.getDocumentElement();

        Incidencia incidencia = new Incidencia();

        EmpleadoDAO empleadoDAO = new EmpleadoDAO();

        incidencia.setId(Integer.parseInt(root.getAttribute("id")));

        String fechaStr = getText(root, "fechaHora");
        if (!fechaStr.isBlank()) {
            incidencia.setFechaHora(LocalDateTime.parse(fechaStr, FORMATO_FECHA));
        }

        Element origenElem = (Element) root.getElementsByTagName("empleadoOrigen").item(0);
        if (origenElem != null && origenElem.hasAttribute("ref")) {
            int idOrigen = Integer.parseInt(origenElem.getAttribute("ref"));
            Empleado empleadoOrigen = empleadoDAO.buscarEmpleado(idOrigen);
            incidencia.setEmpleadoOrigen(empleadoOrigen);
        }

        Element destinoElem = (Element) root.getElementsByTagName("empleadoDestino").item(0);
        if (destinoElem != null && destinoElem.hasAttribute("ref")) {
            int idDestino = Integer.parseInt(destinoElem.getAttribute("ref"));
            Empleado empleadoDestino = empleadoDAO.buscarEmpleado(idDestino);
            incidencia.setEmpleadoDestino(empleadoDestino);
        }

        incidencia.setDetalle(getText(root, "detalle"));
        incidencia.setTipo(getText(root, "tipo"));

        return incidencia;
    }


//    private Incidencia parsearIncidenciaDesdeXML(String contenido) throws Exception {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(new InputSource(new StringReader(contenido)));
//        Element root = doc.getDocumentElement();
//
//        Incidencia incidencia = new Incidencia();
//        incidencia.setId(Integer.parseInt(root.getAttribute("id")));
//
//        String fechaStr = getText(root, "fechaHora");
//        if (!fechaStr.isBlank()) {
//            incidencia.setFechaHora(LocalDateTime.parse(fechaStr, FORMATO_FECHA));
//        }
//
//        Element origenElem = (Element) root.getElementsByTagName("empleadoOrigen").item(0);
//        if (origenElem != null && origenElem.hasAttribute("ref")) {
//            int idOrigen = Integer.parseInt(origenElem.getAttribute("ref"));
//            incidencia.setEmpleadoOrigen(new Empleado(idOrigen));
//        }
//
//        Element destinoElem = (Element) root.getElementsByTagName("empleadoDestino").item(0);
//        if (destinoElem != null && destinoElem.hasAttribute("ref")) {
//            int idDestino = Integer.parseInt(destinoElem.getAttribute("ref"));
//            incidencia.setEmpleadoDestino(new Empleado(idDestino));
//        }
//
//        incidencia.setDetalle(getText(root, "detalle"));
//        incidencia.setTipo(getText(root, "tipo"));
//
//        return incidencia;
//    }


    private List<Incidencia> ejecutarConsultaIncidencias(String xquery) throws Exception {
        List<Incidencia> incidencias = new ArrayList<>();
        Collection collection = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);
            XQueryService service = obtenerServicio(collection);

            var result = service.execute(service.compile(xquery));
            var iterator = result.getIterator();

            while (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                incidencias.add(parsearIncidenciaDesdeXML(res.getContent().toString()));
            }
        } finally {
            if (collection != null) collection.close();
        }

        return incidencias;
    }

    private Incidencia ejecutarConsultaIncidenciaUnica(String xquery) throws Exception {
        Collection collection = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);
            XQueryService service = obtenerServicio(collection);

            var result = service.execute(service.compile(xquery));
            var iterator = result.getIterator();

            if (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                return parsearIncidenciaDesdeXML(res.getContent().toString());
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
