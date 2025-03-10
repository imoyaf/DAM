package controller;

import org.hibernate.query.Query;
import util.HibernateUtil;
import model.Empleado;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class EmpleadoController {

    private Session iniciarSesionConTransaccion() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        return session;
    }

    private void manejarTransaccion(Session session, Transaction transaction) {
        try {
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void insertarEmpleado(Empleado empleado) {
        Session session;
        Transaction transaction;
        try {
            session = iniciarSesionConTransaccion();
            session.save(empleado);
            transaction = session.getTransaction();
            manejarTransaccion(session, transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarEmpleado(Empleado empleado) {
        Session session;
        Transaction transaction;
        try {
            session = iniciarSesionConTransaccion();
            session.update(empleado);
            transaction = session.getTransaction();
            manejarTransaccion(session, transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Empleado buscarEmpleado(int id) {
        Session session = null;
        Empleado empleado = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            empleado = session.get(Empleado.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return empleado;
    }

    public void cambiarContrasena(int id, String nuevaContrasena) {
        Session session;
        Transaction transaction;
        try {
            session = iniciarSesionConTransaccion();
            Empleado empleado = session.get(Empleado.class, id);
            if (empleado != null) {
                empleado.setContrasena(nuevaContrasena);
                session.update(empleado);
            }
            transaction = session.getTransaction();
            manejarTransaccion(session, transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarEmpleado(int id) {
        Session session;
        Transaction transaction;
        try {
            session = iniciarSesionConTransaccion();
            Empleado empleado = session.get(Empleado.class, id);
            if (empleado != null) {
                session.delete(empleado);
            }
            transaction = session.getTransaction();
            manejarTransaccion(session, transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Empleado> obtenerEmpleados() {
        Session session = null;
        List<Empleado> empleados = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            empleados = session.createQuery("FROM Empleado", Empleado.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return empleados;
    }

    public Empleado validarEmpleado(String usuario, String contrasena) {
        Session session = null;
        Empleado empleado = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Empleado WHERE usuario = :usuario AND contrasena = :contrasena";
            Query<Empleado> query = session.createQuery(hql, Empleado.class);
            query.setParameter("usuario", usuario);
            query.setParameter("contrasena", contrasena);
            empleado = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return empleado;
    }

}
