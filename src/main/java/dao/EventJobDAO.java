package dao;

import model.Events;
import model.Events_;
import org.apache.log4j.Logger;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MSI on 2017-05-03.
 */
@Stateful
public class EventJobDAO {

    final static Logger LOG = Logger.getLogger(EventJobDAO.class);

    @PersistenceContext(name = "NewPersistenceUnit", type = PersistenceContextType.EXTENDED)
    EntityManager em;

    public void deletePastEvents(List<Events> terminatedEvents) {
        LOG.info("---------- START ----------");
        LOG.info("CLASS: " + this.getClass().getSimpleName());

        for (Events elem : terminatedEvents) {
            LOG.info("DELETING EVENT: id=" + elem.getId() + " name=" + elem.getName() + " latitude" + elem.getLatitude() + " longitude" + elem.getLongitude());
            elem.setDeleted((byte) 1);
            elem.setActive((byte) 0);
            em.merge(elem);
            LOG.info("EVENT DELETED");
        }
        LOG.info("ALL EVENTS SUCCESSFULLY DELETED");
        LOG.info("---------- END ----------");
    }

    public List<Events> getTerminatedEvents(Date date) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Events> q = cb.createQuery(Events.class);
        Root<Events> from = q.from(Events.class);
        Predicate predicate = cb.and(
                cb.lessThan(from.get(Events_.date_ending), date),
                cb.equal(from.get(Events_.active), 1),
                cb.equal(from.get(Events_.deleted), 0)
        );

        q.select(from).where(predicate);

        List<Events> terminatedEvents = new ArrayList<>();

        terminatedEvents = em.createQuery(q).getResultList();

        return terminatedEvents;
    }

}
