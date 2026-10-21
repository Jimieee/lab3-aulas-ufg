#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dao;

import ${package}.entidades.Usuario;
import ${package}.util.HibernateUtil;
import org.hibernate.Session;

public class UsuarioDAO {
    // Recibe el hash del controlador; los parámetros evitan concatenar credenciales.
    public Usuario login(String correo, String claveMD5) {
        String hql = "FROM Usuario u WHERE u.correo = :correo "
                + "AND u.clave = :clave AND u.estado = 'ACTIVO'";

        try (Session sesion = HibernateUtil.getSessionFactory().openSession()) {
            return sesion.createQuery(hql, Usuario.class)
                    .setParameter("correo", correo)
                    .setParameter("clave", claveMD5)
                    .uniqueResultOptional()
                    .orElse(null);
        }
    }

}
