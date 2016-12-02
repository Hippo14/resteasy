package dao;

import model.Events;
import model.Events_;
import model.Marker;

import javax.ejb.Stateful;
import javax.persistence.*;
import javax.persistence.criteria.*;
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

    public List<Events> getByLocation(String cityName, double latitude, double longitude) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Events> q = cb.createQuery(Events.class);
        Root<Events> from = q.from(Events.class);

        Path<Double> latitudeDb = from.get("latitude");
        Path<Double> longitudeDb = from.get("longitude");
        Predicate predicate = cb.and(
                cb.gt(latitudeDb, latitude - 0.5),
                cb.lt(latitudeDb, latitude + 0.5),
                cb.gt(longitudeDb, longitude - 0.5),
                cb.lt(longitudeDb, longitude + 0.5)
        );
        q.select(from).where(predicate);

        TypedQuery<Events> query = em.createQuery(q);


       return query.getResultList();
    }

    public Marker getMarkerDetails(String cityName, double latitude, double longitude) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Events> q = cb.createQuery(Events.class);
        Root<Events> from = q.from(Events.class);
        Predicate predicate = cb.and(
                cb.equal(from.get(Events_.latitude), latitude),
                cb.equal(from.get(Events_.longitude), longitude)
        );

        q.select(from).where(predicate);

        Events events = null;
        try {
            events = em.createQuery(q).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            events = em.createQuery(q).setMaxResults(1).getResultList().get(0);
        }

        Marker marker = new Marker();
        marker.setTitle(events.getName());
        marker.setUsername(events.getUsers().getName());
        marker.setDescription(events.getDescription());

        return marker;
    }
}
