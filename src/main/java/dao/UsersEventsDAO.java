package dao;

import model.*;
import org.apache.log4j.Logger;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by MSI on 2017-05-04.
 */
@Stateful
public class UsersEventsDAO {

    final static Logger LOG = Logger.getLogger(UsersEventsDAO.class);

    private UUID uuid = java.util.UUID.randomUUID();

    @PersistenceContext(name = "NewPersistenceUnit", type = PersistenceContextType.EXTENDED)
    EntityManager em;

    public List<Events> getByUser(Users users) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UsersEvents> q = cb.createQuery(UsersEvents.class);
        Root<UsersEvents> from = q.from(UsersEvents.class);

        Predicate predicate = cb.equal(from.get(UsersEvents_.users), users);

        q.select(from).where(predicate);

        List<UsersEvents> usersEventses = new ArrayList<>();
        try {
            usersEventses = em.createQuery(q).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }

        List<Events> result = new ArrayList<>();

        for (UsersEvents elem : usersEventses) {
            result.add(elem.getEvents());
        }

        return result;
    }

}
