package ru.silvestrov.timetracker.data;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * Created by Silvestrov Ilya
 * Date: 4/30/12
 * Time: 10:40 PM
 */
public class ContextUtil {
    public static ApplicationContext createContext(String dbName, String... configs) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(configs, false) {
            protected Resource getResourceByPath(String path) {
                return new ClassPathResource(path, getClass());
            }
        };
        PropertyPlaceholderConfigurer configurator = new PropertyPlaceholderConfigurer();
        Properties props = new Properties();
        props.setProperty("derby.dbName", dbName);
        configurator.setProperties(props);
        ctx.addBeanFactoryPostProcessor(configurator);
        ctx.refresh();

        return ctx;
    }

    public static ApplicationContext createContext(String dbName) {
        return createContext(dbName, "db.xml", "dao.xml", "/context.xml",
                "/ru/silvestrov/timetracker/model/activitytree/activityTree.xml");
    }
}
