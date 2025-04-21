package dao;

import exception.ExcepcionAccesoBaseDeDatos;
import model.Empleado;
import model.Incidencia;
import util.ConexionExistDB;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XQueryService;

import javax.xml.transform.OutputKeys;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDAO {

    private final String RUTA_COLECCION = "/db/incidenciasApp/incidencias";
    private final String DOCUMENTO = "incidencias.xml";

    public void insertarIncidencia(Incidencia incidencia) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            String xquery = String.format("""
                update insert
                    <incidencia id="%d">
                        <fechaHora>%s</fechaHora>
                        <empleadoOrigen>%d</empleadoOrigen>
                        <empleadoDestino>%d</empleadoDestino>
                        <detalle>%s</detalle>
                        <tipo>%s</tipo>
                    </incidencia>
                into doc("%s")/incidencias
                """,
                    incidencia.getId(),
                    sdf.format(incidencia.getFechaHora()),
                    incidencia.getEmpleadoOrigen().getId(),
                    incidencia.getEmpleadoDestino().getId(),
                    incidencia.getDetalle(),
                    incidencia.getTipo(),
                    DOCUMENTO
            );

            service.execute(service.compile(xquery));
            System.out.println("✅ Incidencia insertada correctamente.");

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al insertar la incidencia: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }

    public Incidencia obtenerIncidenciaPorId(int id) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;
        Incidencia incidencia = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

            String xquery = String.format("""
            for $i in doc("%s")/incidencias/incidencia[@id = %d]
            return $i
            """, DOCUMENTO, id);

            var result = service.query(xquery);
            var iterator = result.getIterator();

            if (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                String contenido = res.getContent().toString();

                incidencia = new Incidencia();
                incidencia.setId(id);

                if (contenido.contains("<fechaHora>")) {
                    String fechaStr = contenido.replaceAll(".*<fechaHora>(.+)</fechaHora>.*", "$1");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    LocalDateTime ldt = LocalDateTime.parse(fechaStr, formatter);
                    incidencia.setFechaHora(ldt);
                }
                if (contenido.contains("<empleadoOrigen>")) {
                    int idOrigen = Integer.parseInt(contenido.replaceAll(".*<empleadoOrigen>(\\d+)</empleadoOrigen>.*", "$1"));
                    incidencia.setEmpleadoOrigen(new Empleado(idOrigen));
                }
                if (contenido.contains("<empleadoDestino>")) {
                    int idDestino = Integer.parseInt(contenido.replaceAll(".*<empleadoDestino>(\\d+)</empleadoDestino>.*", "$1"));
                    incidencia.setEmpleadoDestino(new Empleado(idDestino));
                }
                if (contenido.contains("<detalle>")) {
                    incidencia.setDetalle(contenido.replaceAll(".*<detalle>(.+)</detalle>.*", "$1"));
                }
                if (contenido.contains("<tipo>")) {
                    incidencia.setTipo(contenido.replaceAll(".*<tipo>(.+)</tipo>.*", "$1"));
                }
            }

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al buscar la incidencia: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }

        return incidencia;
    }

    public List<Incidencia> obtenerTodasLasIncidencias() throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;
        List<Incidencia> incidencias = new ArrayList<>();

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

            String xquery = String.format("for $i in doc(\"%s\")/incidencias/incidencia return $i", DOCUMENTO);

            var result = service.query(xquery);
            var iterator = result.getIterator();

            while (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                String contenido = res.getContent().toString();

                Incidencia incidencia = new Incidencia();

                if (contenido.contains("id=\"")) {
                    int id = Integer.parseInt(contenido.replaceAll(".*id=\"(\\d+)\".*", "$1"));
                    incidencia.setId(id);
                }
                if (contenido.contains("<fechaHora>")) {
                    String fechaStr = contenido.replaceAll(".*<fechaHora>(.+)</fechaHora>.*", "$1");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    LocalDateTime ldt = LocalDateTime.parse(fechaStr, formatter);
                    incidencia.setFechaHora(ldt);
                }
                if (contenido.contains("<empleadoOrigen>")) {
                    int idOrigen = Integer.parseInt(contenido.replaceAll(".*<empleadoOrigen>(\\d+)</empleadoOrigen>.*", "$1"));
                    incidencia.setEmpleadoOrigen(new Empleado(idOrigen));
                }
                if (contenido.contains("<empleadoDestino>")) {
                    int idDestino = Integer.parseInt(contenido.replaceAll(".*<empleadoDestino>(\\d+)</empleadoDestino>.*", "$1"));
                    incidencia.setEmpleadoDestino(new Empleado(idDestino));
                }
                if (contenido.contains("<detalle>")) {
                    incidencia.setDetalle(contenido.replaceAll(".*<detalle>(.+)</detalle>.*", "$1"));
                }
                if (contenido.contains("<tipo>")) {
                    incidencia.setTipo(contenido.replaceAll(".*<tipo>(.+)</tipo>.*", "$1"));
                }

                incidencias.add(incidencia);
            }

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al obtener todas las incidencias: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }

        return incidencias;
    }

    public List<Incidencia> obtenerIncidenciasPorEmpleadoOrigen(Empleado empleado) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;
        List<Incidencia> incidencias = new ArrayList<>();

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

            String xquery = String.format("""
            for $i in doc("%s")/incidencias/incidencia[empleadoOrigen = %d]
            return $i
            """, DOCUMENTO, empleado.getId());

            var result = service.query(xquery);
            var iterator = result.getIterator();

            while (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                String contenido = res.getContent().toString();

                Incidencia incidencia = new Incidencia();

                if (contenido.contains("id=\"")) {
                    int id = Integer.parseInt(contenido.replaceAll(".*id=\"(\\d+)\".*", "$1"));
                    incidencia.setId(id);
                }
                if (contenido.contains("<fechaHora>")) {
                    String fechaStr = contenido.replaceAll(".*<fechaHora>(.+)</fechaHora>.*", "$1");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    LocalDateTime ldt = LocalDateTime.parse(fechaStr, formatter);
                    incidencia.setFechaHora(ldt);
                }
                if (contenido.contains("<empleadoOrigen>")) {
                    int idOrigen = Integer.parseInt(contenido.replaceAll(".*<empleadoOrigen>(\\d+)</empleadoOrigen>.*", "$1"));
                    incidencia.setEmpleadoOrigen(new Empleado(idOrigen));
                }
                if (contenido.contains("<empleadoDestino>")) {
                    int idDestino = Integer.parseInt(contenido.replaceAll(".*<empleadoDestino>(\\d+)</empleadoDestino>.*", "$1"));
                    incidencia.setEmpleadoDestino(new Empleado(idDestino));
                }
                if (contenido.contains("<detalle>")) {
                    incidencia.setDetalle(contenido.replaceAll(".*<detalle>(.+)</detalle>.*", "$1"));
                }
                if (contenido.contains("<tipo>")) {
                    incidencia.setTipo(contenido.replaceAll(".*<tipo>(.+)</tipo>.*", "$1"));
                }

                incidencias.add(incidencia);
            }

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al obtener incidencias por empleado origen: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }

        return incidencias;
    }

    public List<Incidencia> obtenerIncidenciasPorEmpleadoDestino(Empleado empleado) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;
        List<Incidencia> incidencias = new ArrayList<>();

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

            String xquery = String.format("""
            for $i in doc("%s")/incidencias/incidencia[empleadoDestino = %d]
            return $i
            """, DOCUMENTO, empleado.getId());

            var result = service.query(xquery);
            var iterator = result.getIterator();

            while (iterator.hasMoreResources()) {
                var res = iterator.nextResource();
                String contenido = res.getContent().toString();

                Incidencia incidencia = new Incidencia();

                if (contenido.contains("id=\"")) {
                    int id = Integer.parseInt(contenido.replaceAll(".*id=\"(\\d+)\".*", "$1"));
                    incidencia.setId(id);
                }
                if (contenido.contains("<fechaHora>")) {
                    String fechaStr = contenido.replaceAll(".*<fechaHora>(.+)</fechaHora>.*", "$1");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    LocalDateTime ldt = LocalDateTime.parse(fechaStr, formatter);
                    incidencia.setFechaHora(ldt);
                }
                if (contenido.contains("<empleadoOrigen>")) {
                    int idOrigen = Integer.parseInt(contenido.replaceAll(".*<empleadoOrigen>(\\d+)</empleadoOrigen>.*", "$1"));
                    incidencia.setEmpleadoOrigen(new Empleado(idOrigen));
                }
                if (contenido.contains("<empleadoDestino>")) {
                    int idDestino = Integer.parseInt(contenido.replaceAll(".*<empleadoDestino>(\\d+)</empleadoDestino>.*", "$1"));
                    incidencia.setEmpleadoDestino(new Empleado(idDestino));
                }
                if (contenido.contains("<detalle>")) {
                    incidencia.setDetalle(contenido.replaceAll(".*<detalle>(.+)</detalle>.*", "$1"));
                }
                if (contenido.contains("<tipo>")) {
                    incidencia.setTipo(contenido.replaceAll(".*<tipo>(.+)</tipo>.*", "$1"));
                }

                incidencias.add(incidencia);
            }

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al obtener incidencias por empleado destino: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }

        return incidencias;
    }

    public void eliminarIncidencia(Incidencia incidencia) throws ExcepcionAccesoBaseDeDatos {
        Collection collection = null;

        try {
            collection = ConexionExistDB.getInstancia().obtenerColeccion(RUTA_COLECCION);

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

            String xquery = String.format("""
            update delete doc("%s")/incidencias/incidencia[@id = %d]
            """, DOCUMENTO, incidencia.getId());

            service.execute(service.compile(xquery));
            System.out.println("Incidencia eliminada correctamente.");

        } catch (XMLDBException e) {
            throw new ExcepcionAccesoBaseDeDatos("Error al eliminar la incidencia: " + e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }
}
