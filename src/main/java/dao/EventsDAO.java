package dao;

import model.*;
import org.apache.log4j.Logger;

import javax.ejb.Stateful;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by MSI on 2016-09-25.
 */
@Stateful
public class EventsDAO implements Serializable {

    final static Logger LOG = Logger.getLogger(EventsDAO.class);

    private UUID uuid = java.util.UUID.randomUUID();

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

    public List<Events> getByLocation(String cityName, double latitude, double longitude, Timestamp actualDate) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Events> q = cb.createQuery(Events.class);
        Root<Events> from = q.from(Events.class);

        Path<Double> latitudeDb = from.get("latitude");
        Path<Double> longitudeDb = from.get("longitude");
        Path<Timestamp> dateEndingDb = from.get("date_ending");
        Predicate predicate = cb.and(
                cb.gt(latitudeDb, latitude - 0.5),
                cb.lt(latitudeDb, latitude + 0.5),
                cb.gt(longitudeDb, longitude - 0.5),
                cb.lt(longitudeDb, longitude + 0.5),
                cb.greaterThanOrEqualTo(dateEndingDb, actualDate),
                cb.equal(from.get(Events_.deleted), 0),
                cb.equal(from.get(Events_.active), 1)
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
        String description = events.getDescription();
        if (description.length() >= 16)
            description = description.substring(0, 16);
        marker.setDescription(description);

        return marker;
    }

    public Events getByLocation(double latitude, double longitude) {
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

        return events;
    }

    public List<Events> getTopEvents(Timestamp actualDate, double latitude, double longitude, int top) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Events> q = cb.createQuery(Events.class);
        Root<Events> from = q.from(Events.class);

        Path<Double> latitudeDb = from.get("latitude");
        Path<Double> longitudeDb = from.get("longitude");
        Path<Timestamp> dateEndingDb = from.get("date_ending");
        Predicate predicate = cb.and(
                cb.gt(latitudeDb, latitude - 0.5),
                cb.lt(latitudeDb, latitude + 0.5),
                cb.gt(longitudeDb, longitude - 0.5),
                cb.lt(longitudeDb, longitude + 0.5),
                cb.greaterThanOrEqualTo(dateEndingDb, actualDate),
                cb.equal(from.get(Events_.deleted), 0),
                cb.equal(from.get(Events_.active), 1)
        );

        q.select(from).where(predicate);

        List<Events> board = new ArrayList<>();
        try {
            board = em.createQuery(q).setMaxResults(top).getResultList();
        } catch (NoResultException e) {
            return null;
        }

        return board;
    }

    public List<Events> getByUsername(Users username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Events> q = cb.createQuery(Events.class);
        Root<Events> from = q.from(Events.class);

        Predicate predicate = cb.equal(from.get(Events_.users), username);

        q.select(from).where(predicate);

        List<Events> events = new ArrayList<>();
        try {
            events = em.createQuery(q).getResultList();
        } catch (NoResultException e) {
            return null;
        }

        return events;
    }

    public List<UsersEvents> getUserListEvent(Double latitude, Double longitude) {
        Events event = getByLocation(latitude, longitude);

        CriteriaBuilder cb1 = em.getCriteriaBuilder();
        CriteriaQuery<UsersEvents> q1 = cb1.createQuery(UsersEvents.class);
        Root<UsersEvents> from1 = q1.from(UsersEvents.class);

        Predicate predicate1 = cb1.equal(from1.get(UsersEvents_.events), event);
        q1.select(from1).where(predicate1);

        List<UsersEvents> usersEvents;
        try {
            usersEvents = em.createQuery(q1).getResultList();
        } catch (NoResultException e) {
            usersEvents = new ArrayList<>();
        }

        return usersEvents;
    }

    public Users addUserToEvent(Users user, Double latitude, Double longitude) {
        Events event = getByLocation(latitude, longitude);
        UsersEvents usersEvents = getUsersEvent(user, event);
        if (usersEvents != null) {
            // Błąd nic nie rób!!!
            return null;
        }
        usersEvents = new UsersEvents(user, event);
        em.persist(usersEvents);

        return user;
    }

    private UsersEvents getUsersEvent(Users user, Events event) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UsersEvents> q = cb.createQuery(UsersEvents.class);
        Root<UsersEvents> from = q.from(UsersEvents.class);
        Predicate predicate = cb.and(
                cb.equal(from.get(UsersEvents_.events), event),
                cb.equal(from.get(UsersEvents_.users), user)
        );

        q.select(from).where(predicate);


        UsersEvents usersEvents = null;

        try {
            usersEvents = em.createQuery(q).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            usersEvents = em.createQuery(q).setMaxResults(1).getResultList().get(0);
        }

        return usersEvents;
    }

    public Users removeUserFromEvent(Users user, Double latitude, Double longitude) {
        Events event = getByLocation(latitude, longitude);
        UsersEvents usersEvents = getUsersEvent(user, event);

        em.remove(usersEvents);

        return user;
    }

    public Boolean getUserStatusEvent(Users user, Double latitude, Double longitude) {
        Events event = getByLocation(latitude, longitude);
        UsersEvents usersEvents = getUsersEvent(user, event);

        return (usersEvents != null);
    }

}
