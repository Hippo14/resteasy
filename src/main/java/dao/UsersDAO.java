package dao;

import auth.Token;
import auth.parts.Header;
import auth.parts.Payload;
import config.ErrorConfig;
import model.UsersKeys;
import model.Users;
import model.Users_;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtil;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.WebApplicationException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by MSI on 2016-10-09.
 */
@Stateful
public class UsersDAO {

    @PersistenceContext(name = "NewPersistenceUnit", type = PersistenceContextType.EXTENDED)
    EntityManager em;

    @EJB
    TokensDAO tokensDAO;

    public Users getByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> q = cb.createQuery(Users.class);
        Root<Users> from = q.from(Users.class);
        Predicate predicate = cb.equal(from.get(Users_.name), name);

        q.select(from).where(predicate);

        return em.createQuery(q).getSingleResult();
    }

    public String getByEmail(String email, String password) {
        // Get user by credentials
        Users user = getByCredentials(email, password);

        if (password.equals(user.getPassword()))
            return tokensDAO.generateToken(user);
        else
            throw new WebApplicationException(ErrorConfig.BAD_PASSWORD);
    }

    private Users getByCredentials(String email, String password) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> q = cb.createQuery(Users.class);
        Root<Users> from = q.from(Users.class);
        Predicate predicate = cb.equal(from.get(Users_.email), email);

        q.select(from).where(predicate);

        return em.createQuery(q).getSingleResult();
    }

    public String createNewUser(Users newUser) {
        // Check if user exists
        if (userExists(newUser))
            throw new HibernateException("User exists!");

        // Add user to database
        em.persist(newUser);

        // Generate token
        return tokensDAO.generateToken(newUser);
    }

    private boolean userExists(Users newUser) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> q = cb.createQuery(Users.class);
        Root<Users> from = q.from(Users.class);
        Predicate predicate = cb.equal(from.get(Users_.email), newUser.getEmail());
        q.select(from).where(predicate);
        TypedQuery<Users> tq = em.createQuery(q);
        tq.setMaxResults(1);

        Users user = null;

        try {
            user = tq.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (user != null);

    }
}
