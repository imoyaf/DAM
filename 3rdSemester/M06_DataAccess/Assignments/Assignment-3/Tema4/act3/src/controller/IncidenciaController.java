package controller;

import exception.BuscarEnArchivoException;
import exception.GuardarEnArchivoException;
import model.Empleado;
import model.Incidencia;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncidenciaController {
    private final ObjectContainer db;
    private static final Logger logger = Logger.getLogger(IncidenciaController.class.getName());

    public IncidenciaController() {
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "incidencias");
    }

    public Incidencia obtenerIncidenciaPorId(int id) throws BuscarEnArchivoException {
        Incidencia incidencia = new Incidencia(id);
        Incidencia incidenciaObtenida;
        try {
            ObjectSet<Incidencia> result = db.queryByExample(incidencia);
            incidenciaObtenida = result.next();
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error al intentar obtener la incidencia", e);
            throw new BuscarEnArchivoException("Error al intentar obtener la incidencia"
                    + e.getMessage(), e);
        }
        return incidenciaObtenida;
    }

    public List<Incidencia> obtenerTodasLasIncidencias() throws BuscarEnArchivoException {
        List<Incidencia> incidencias = new ArrayList<>();
        try {
            ObjectSet<Incidencia> result = db.queryByExample(Incidencia.class);
            while(result.hasNext()) {
                incidencias.add(result.next());
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error al intentar obtener la lista de incidencias", e);
            throw new BuscarEnArchivoException("Error al intentar obtener la lista de incidencias"
                    + e.getMessage(), e);
        }
        return incidencias;
    }

    public void insertarIncidencia(Incidencia incidencia) throws GuardarEnArchivoException {
        try {
            db.store(incidencia);
        } catch(Exception e) {
            logger.log(Level.SEVERE,"Error al guardar la nueva incidencia. " + e.getMessage(), e);
            throw new GuardarEnArchivoException("Error al guardar la nueva incidencia"
                    + e.getMessage(), e);
        }
    }

    public List<Incidencia> obtenerIncidenciasPorEmpleadoOrigen(Empleado empleado)
            throws BuscarEnArchivoException {
        List<Incidencia> incidencias = new ArrayList<>();
        Incidencia incidencia = new Incidencia();
        incidencia.setEmpleadoOrigen(empleado);
        try {
            ObjectSet<Incidencia> result = db.queryByExample(incidencia);
            while (result.hasNext()) {
                incidencias.add(result.next());
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error al buscar las incidencias. ", e);
            throw new BuscarEnArchivoException("Error al buscar las incidencias"
                    + e.getMessage(), e);
        }
        return incidencias;
    }

    public List<Incidencia> obtenerIncidenciasPorEmpleadoDestino(Empleado empleado)
            throws BuscarEnArchivoException {
        List<Incidencia> incidencias = new ArrayList<>();
        Incidencia incidencia = new Incidencia();
        incidencia.setEmpleadoDestino(empleado);
        try {
            ObjectSet<Incidencia> result = db.queryByExample(incidencia);
            while (result.hasNext()) {
                incidencias.add(result.next());
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error al buscar las incidencias. ", e);
            throw new BuscarEnArchivoException("Error al buscar las incidencias"
                    + e.getMessage(), e);
        }
        return incidencias;
    }
}
