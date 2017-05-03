package conf;

import model.Events;
import model.Events_;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
@Stateless
public class EventJobDAO {

    final static Logger LOG = Logger.getLogger(EventJobDAO.class);

    @PersistenceContext(name = "NewPersistenceUnit", type = PersistenceContextType.EXTENDED)
    EntityManager em;

    public void deletePastEvents(List<Events> terminatedEvents) {
        LOG.info("---------- START ----------");
        LOG.info("CLASS: " + this.getClass().getSimpleName());
        LOG.info("METHOD: " + this.getClass().getEnclosingMethod().getName());

        for (Events elem : terminatedEvents) {
            LOG.info("DELETING EVENT: id=" + elem.getId() + " name=" + elem.getName() + " latitude" + elem.getLatitude() + " longitude" + elem.getLongitude());
            em.remove(elem);
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
                cb.equal(from.get(Events_.date_ending), date)
        );

        q.select(from).where(predicate);

        List<Events> terminatedEvents = new ArrayList<>();

        try {
            terminatedEvents = em.createQuery(q).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }

        return terminatedEvents;
    }

}
