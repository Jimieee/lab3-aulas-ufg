package edu.ufg.aulas.dao;

import java.util.List;
import edu.ufg.aulas.entidades.Aula;
import edu.ufg.aulas.entidades.EstadoAula;
import edu.ufg.aulas.entidades.Planta;
import edu.ufg.aulas.entidades.TipoAula;
import edu.ufg.aulas.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AulaDAO {

    public List<Planta> listarPlantas() {
        try (Session sesion = HibernateUtil.getSessionFactory().openSession()) {
            return sesion.createQuery("FROM Planta p ORDER BY p.numero", Planta.class).list();
        }
    }

    public List<TipoAula> listarTipos() {
        try (Session sesion = HibernateUtil.getSessionFactory().openSession()) {
            return sesion.createQuery("FROM TipoAula t ORDER BY t.id", TipoAula.class).list();
        }
    }

    public List<EstadoAula> listarEstados() {
        try (Session sesion = HibernateUtil.getSessionFactory().openSession()) {
            return sesion.createQuery("FROM EstadoAula e ORDER BY e.id", EstadoAula.class).list();
        }
    }

    public boolean existeAula(String nombre, String edificio) {
        String hql = "SELECT COUNT(a) FROM Aula a WHERE a.nombre = :nombre AND a.edificio = :edificio";
        try (Session sesion = HibernateUtil.getSessionFactory().openSession()) {
            Long total = sesion.createQuery(hql, Long.class)
                    .setParameter("nombre", nombre)
                    .setParameter("edificio", edificio)
                    .getSingleResult();
            return total > 0;
        }
    }

    public void guardar(Aula aula) {
        try (Session sesion = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = sesion.beginTransaction();
            try {
                sesion.persist(aula);
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        }
    }

}