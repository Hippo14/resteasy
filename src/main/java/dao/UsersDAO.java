package dao;

import auth.parts.Payload;
import config.ErrorConfig;
import model.Users;
import model.UsersLogo;
import model.Users_;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

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

/**
 * Created by MSI on 2016-10-09.
 */
@Stateful
public class UsersDAO {

    @PersistenceContext(name = "NewPersistenceUnit", type = PersistenceContextType.EXTENDED)
    EntityManager em;

    @EJB
    TokensDAO tokensDAO;
    @EJB
    LogoDAO logoDAO;

    final static Logger LOG = Logger.getLogger(UsersDAO.class);

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
        Users user = getByCredentials(email);

        // Generate token
        if (password.equals(user.getPassword()))
            return tokensDAO.generateToken(user);
        else
            throw new WebApplicationException(ErrorConfig.BAD_PASSWORD);
    }

    private Users getByCredentials(String email) {
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
            throw new WebApplicationException(ErrorConfig.USER_EXISTS);

        // Add user to database
        em.persist(newUser);

        // Generate token
        return getByEmail(newUser.getEmail(), newUser.getPassword());
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
            // User not exists!!
            LOG.error(e);
        }

        return (user != null);
    }

    public String getUserLogo(String token) throws UnsupportedEncodingException {
        String[] subString = token.split("\\.");

        Payload payload = new Payload(new String(Base64.decodeBase64(subString[1].getBytes("UTF-8"))));
        String username = payload.getName();

        UsersLogo usersLogo = logoDAO.getLogoForUser(username);
        byte[] imageB64 = Base64.encodeBase64(usersLogo.getImage());

        return new String(imageB64);
    }
}
