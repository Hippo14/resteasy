package dao;

import model.Users;
import model.UsersLogo;
import model.UsersLogo_;
import org.apache.log4j.Logger;
import org.jboss.resteasy.util.Base64;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;

/**
 * Created by MSI on 2017-01-15.
 */
@Stateless
public class LogoDAO {

    @PersistenceContext(name = "NewPersistenceUnit", type = PersistenceContextType.EXTENDED)
    EntityManager em;

    @EJB
    UsersDAO usersDAO;

    final static Logger LOG = Logger.getLogger(UsersDAO.class);

    public void setLogoForUser(String username, String image) {
        Users users = usersDAO.getByName(username);
        UsersLogo usersLogo = getLogoForUser(username);
        //UsersLogo usersLogo = new UsersLogo();
        usersLogo.setUser(users);

        byte[] imageFromB64 = null;
        try {
            imageFromB64 = Base64.decode(image.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        usersLogo.setImage(imageFromB64);

        em.merge(usersLogo);
    }

    public void setLogoForUser(Users users, String image) {
        UsersLogo usersLogo = new UsersLogo();
        usersLogo.setUser(users);

        byte[] imageFromB64 = null;
        try {
            imageFromB64 = Base64.decode(image.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        usersLogo.setImage(imageFromB64);

        em.merge(usersLogo);
    }

    public UsersLogo getLogoForUser(String username) {
        Users users = usersDAO.getByName(username);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UsersLogo> q = cb.createQuery(UsersLogo.class);
        Root<UsersLogo> from = q.from(UsersLogo.class);
        Predicate predicate = cb.equal(from.get(UsersLogo_.user), users);

        q.select(from).where(predicate);

        return em.createQuery(q).getSingleResult();
    }

    public UsersLogo getLogoForUser(Users users) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UsersLogo> q = cb.createQuery(UsersLogo.class);
        Root<UsersLogo> from = q.from(UsersLogo.class);
        Predicate predicate = cb.equal(from.get(UsersLogo_.user), users);

        q.select(from).where(predicate);

        return em.createQuery(q).getSingleResult();
    }

}
