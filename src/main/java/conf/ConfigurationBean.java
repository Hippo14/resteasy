package conf;


import dao.UsersDAO;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Date;

/**
 * Created by MSI on 2016-10-19.
 */
@Startup
@Singleton
public class ConfigurationBean {

    public enum States {BEFORESTARTED, STARTED, PAUSED, SHUTTINGDOWN};
    private States state;
    final static Logger LOG = Logger.getLogger(UsersDAO.class);

    @PostConstruct
    public void initialize() {
        state = States.BEFORESTARTED;
        // Perform initialization
        state = States.STARTED;
        LOG.info("Service Started");
    }

    @PreDestroy
    public void terminate() {
        state = States.SHUTTINGDOWN;
        // Permform termination
        LOG.info("Shut down in progress");
    }

    @Schedule(hour = "*/1", persistent = false)
    public void doWork() {
        Date date = new Date();
        LOG.info("Scheduling work - " + date.getTime());
    }

}
