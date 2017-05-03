package conf;

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
@LocalBean
public class EventJobBean {

    final static Logger LOG = Logger.getLogger(EventJobBean.class);

    @EJB
    EventJobDAO eventsDAO;

    @PostConstruct
    public void initialize() {
        LOG.info("CLASS :" + this.getClass().getSimpleName());
        LOG.info("METHOD :" + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Schedule(minute = "*/5",hour = "*", persistent = false)
    public void deletePastEvents() {
        LOG.info("---------- START JOB ----------");
        LOG.info("CLASS: " + this.getClass().getSimpleName().toString());
        LOG.info("METHOD :" + Thread.currentThread().getStackTrace()[1].getMethodName());

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
