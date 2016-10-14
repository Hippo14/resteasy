package dao;

import config.ErrorConfig;
import model.Users;
import model.Users_;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.WebApplicationException;

/**
 * Created by MSI on 2016-10-09.
 */
@Stateful
public class UsersDAO {

    @PersistenceContext(name = "NewPersistenceUnit", type = PersistenceContextType.EXTENDED)
    EntityManager em;

    public Users getByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> q = cb.createQuery(Users.class);
        Root<Users> from = q.from(Users.class);
        Predicate predicate = cb.equal(from.get(Users_.name), name);

        q.select(from).where(predicate);

        return em.createQuery(q).getSingleResult();
    }

    public Users getByEmail(String email, String password) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> q = cb.createQuery(Users.class);
        Root<Users> from = q.from(Users.class);
        Predicate predicate = cb.equal(from.get(Users_.email), email);

        q.select(from).where(predicate);

        Users user = em.createQuery(q).getSingleResult();

        if (password.equals(user.getPassword()))
            return user;
        else
            throw new WebApplicationException(ErrorConfig.BAD_PASSWORD);
    }
}
