package controller;

import model.Incidencia;
import model.Empleado;
import org.hibernate.Hibernate;
import util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class IncidenciaController {

    private Session iniciarSesion() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    private void cerrarSesion(Session session) {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    public List<Incidencia> obtenerTodasLasIncidencias() {
        try (Session session = iniciarSesion()) {
            String hql = "FROM Incidencia i " +
                    "JOIN FETCH i.empleadoOrigen " +
                    "JOIN FETCH i.empleadoDestino";
            Query<Incidencia> query = session.createQuery(hql, Incidencia.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Incidencia obtenerIncidenciaPorId(int id) {
        try (Session session = iniciarSesion()) {
            Incidencia incidencia = session.get(Incidencia.class, id);
            if (incidencia != null) {
                Hibernate.initialize(incidencia.getEmpleadoOrigen());
                Hibernate.initialize(incidencia.getEmpleadoDestino());
            }
            return incidencia;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertarIncidencia(Incidencia incidencia) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = iniciarSesion();
            transaction = session.beginTransaction();
            session.save(incidencia);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            cerrarSesion(session);
        }
    }

    public List<Incidencia> obtenerIncidenciasPorEmpleadoOrigen(Empleado empleado) {
        try (Session session = iniciarSesion()) {
            String hql = "FROM Incidencia i WHERE i.empleadoOrigen = :empleado";
            Query<Incidencia> query = session.createQuery(hql, Incidencia.class);
            query.setParameter("empleado", empleado);
            List<Incidencia> incidencias = query.list();

            for (Incidencia incidencia : incidencias) {
                if (incidencia != null) {
                    Hibernate.initialize(incidencia.getEmpleadoOrigen());
                    Hibernate.initialize(incidencia.getEmpleadoDestino());
                }
            }
            return incidencias;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Incidencia> obtenerIncidenciasParaEmpleadoDestino(Empleado empleado) {
        try (Session session = iniciarSesion()) {
            String hql = "FROM Incidencia i WHERE i.empleadoDestino = :empleado";
            Query<Incidencia> query = session.createQuery(hql, Incidencia.class);
            query.setParameter("empleado", empleado);
            List<Incidencia> incidencias = query.list();

            for (Incidencia incidencia : incidencias) {
                if (incidencia != null) {
                    Hibernate.initialize(incidencia.getEmpleadoOrigen());
                    Hibernate.initialize(incidencia.getEmpleadoDestino());
                }
            }
            return incidencias;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

