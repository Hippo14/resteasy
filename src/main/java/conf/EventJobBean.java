package conf;

import dao.EventJobDAO;
import model.Events;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;
import java.util.Date;
import java.util.List;

/**
 * Created by MSI on 2017-05-03.
 */
@Startup
@DependsOn("ConfigurationBean")
@Singleton
public class EventJobBean {

    final static Logger LOG = Logger.getLogger(EventJobBean.class);

    @EJB
    EventJobDAO eventsDAO;

    @PostConstruct
    public void initialize() {
        LOG.info("CLASS :" + this.getClass().getSimpleName());
        LOG.info("METHOD :" + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Schedule(hour = "*/1", persistent = false)
    public void deletePastEvents() {
        LOG.info("---------- START JOB ----------");
        LOG.info("CLASS: " + this.getClass().getSimpleName().toString());

        Long start = System.currentTimeMillis();
        Date actualDate = new Date();

        LOG.info("ACTUAL DATE: " + actualDate.getTime());

        List<Events> terminatedEvents = eventsDAO.getTerminatedEvents(actualDate);

        LOG.info("NUMER OF TERMINATED EVENTS: " + terminatedEvents.size());

        LOG.info("DELETING TERMINATED EVENTS");
        eventsDAO.deletePastEvents(terminatedEvents);
        LOG.info("SUCCESS");

        Long stop = System.currentTimeMillis();

        LOG.info("EXECUTION TIME: " + (stop - start) + "ms");
        LOG.info("---------- END JOB ----------");
    }

    @PreDestroy
    public void terminate() {
        LOG.info("CLASS :" + this.getClass().getSimpleName());
        LOG.info("METHOD :" + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

}
