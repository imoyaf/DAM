package controller;

import dao.IncidenciaDAO;
import exception.ExcepcionAccesoBaseDeDatos;
import exception.ExcepcionServicio;
import model.Empleado;
import model.Incidencia;

import java.util.List;

public class IncidenciaController {

    private final IncidenciaDAO incidenciaDAO = new IncidenciaDAO();

    public void insertarIncidencia(Incidencia incidencia) throws ExcepcionServicio {
        try {
            incidenciaDAO.insertarIncidencia(incidencia);
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al guardar la incidencia.", e);
        }
    }

    public Incidencia obtenerIncidenciaPorId(int id) throws ExcepcionServicio {
        try {
            return incidenciaDAO.obtenerIncidenciaPorId(id);
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al obtener la incidencia.", e);
        }
    }

    public List<Incidencia> obtenerTodasLasIncidencias() throws ExcepcionServicio {
        try {
            return incidenciaDAO.obtenerTodasLasIncidencias();
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al obtener todas las incidencias.", e);
        }
    }

    public List<Incidencia> obtenerIncidenciasPorEmpleadoOrigen(Empleado empleado) throws ExcepcionServicio {
        try {
            return incidenciaDAO.obtenerIncidenciasPorEmpleadoOrigen(empleado);
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al obtener las incidencias creadas por el empleado.", e);
        }
    }

    public List<Incidencia> obtenerIncidenciasPorEmpleadoDestino(Empleado empleado) throws ExcepcionServicio {
        try {
            return incidenciaDAO.obtenerIncidenciasPorEmpleadoDestino(empleado);
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al obtener las incidencias destinadas al empleado.", e);
        }
    }

    public void eliminarIncidencia(Incidencia incidencia) throws ExcepcionServicio {
        try {
            incidenciaDAO.eliminarIncidencia(incidencia);
        } catch (ExcepcionAccesoBaseDeDatos e) {
            throw new ExcepcionServicio("Error al eliminar la incidencia.", e);
        }
    }
}
