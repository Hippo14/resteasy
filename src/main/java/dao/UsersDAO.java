package dao;

import auth.Token;
import config.ErrorConfig;
import model.Key;
import model.Users;
import model.Users_;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtil;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.WebApplicationException;
import java.security.NoSuchAlgorithmException;

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

    public String createNewUser(Users newUser) {
        try {
            // Add user to database
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            session.save(newUser);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        // Generate key
        byte[] key = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // for example
            SecretKey secretKey = keyGen.generateKey();
            key = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Get token for user
        String token = null;
        try {
            token = Token.getTokenToJson(newUser, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //TODO ADD TOKEN TO DATABASE!!!!!!
        // Add key to database
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            session.save(new Key(newUser, key));
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return token;

    }
}
