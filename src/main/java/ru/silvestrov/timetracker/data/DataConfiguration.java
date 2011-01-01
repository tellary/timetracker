package ru.silvestrov.timetracker.data;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Properties;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 13, 2008
 * Time: 5:44:33 PM
 */
public class DataConfiguration {
    private ClassPathXmlApplicationContext ctx;

    public DataConfiguration(String dbName) {
        this(dbName, "context.xml", "db.xml");
    }

    public DataConfiguration(String dbName, String... configs) {
        ctx = new ClassPathXmlApplicationContext(configs, false) {
            protected Resource getResourceByPath(String path) {
                return new ClassPathResource(path, getClass());
            }
        };
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        Properties props = new Properties();
        props.setProperty("derby.dbName", dbName);
        configurer.setProperties(props);
        ctx.addBeanFactoryPostProcessor(configurer);
        ctx.refresh();
    }

    public ApplicationContext getApplicationContext() {
        return ctx;
    }

    public ActivityDao getActivityDao() {
        return (ActivityDao) ctx.getBean("activityDao");
    }

    public TimeEntryDao getTimeEntryDao() {
        return (TimeEntryDao) ctx.getBean("timeEntryDao");
    }

    public TransactionTemplate getTransactionTemplate() {
        return (TransactionTemplate) ctx.getBean("transactionTemplate");
    }

    public SessionFactory getSessionFactory() {
        return (SessionFactory) ctx.getBean("sessionFactory");
    }
}
