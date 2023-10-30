package tweb.persistence;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class PersistenceContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
    }

    public void contextDestroyed(ServletContextEvent sce) {
        BasicPersistenceManager.cleanup();
        PoolingPersistenceManager.cleanup();
    }
}
