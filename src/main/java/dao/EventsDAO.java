package dao;

import model.Events;
import model.Events_;

import javax.ejb.Stateful;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by MSI on 2016-09-25.
 */
@Stateful
public class EventsDAO {

    @PersistenceContext(name = "NewPersistenceUnit", type = PersistenceContextType.EXTENDED)
    EntityManager em;


    public List<Events> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Events> q = cb.createQuery(Events.class);
        Root<Events> c = q.from(Events.class);
        q.select(c);
        TypedQuery<Events> query = em.createQuery(q);

        return query.getResultList();
    }

    public Events getById(Integer id) {
        return em.find(Events.class, id);
    }

    public Events getByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Events> q = cb.createQuery(Events.class);
        Root<Events> from = q.from(Events.class);
        Predicate predicate = cb.equal(from.get(Events_.name), name);

        q.select(from).where(predicate);

        Events events = null;
        try {
            events = em.createQuery(q).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            events = em.createQuery(q).setMaxResults(1).getResultList().get(0);
        }

        return events;
    }

    public void add(Events event) {
        em.persist(event);
    }
}
