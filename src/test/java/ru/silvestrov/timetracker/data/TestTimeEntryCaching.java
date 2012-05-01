package ru.silvestrov.timetracker.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.TransactionStatus;
import org.hibernate.SessionFactory;
import junit.framework.Assert;

import javax.annotation.Resource;

/**
 * Created by Silvestrov Iliya.
 * Date: Jul 15, 2008
 * Time: 12:54:01 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestTimeEntryCaching.class)
public class TestTimeEntryCaching implements ContextLoader {
    @Resource
    private TimeEntryDao timeEntryDao;
    @Resource
    private TransactionTemplate tt;
    @Resource
    private SessionFactory sessionFactory;


    @Test
    public void testCaching() {
        tt.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                timeEntryDao.getTotalTime(2L);
                Assert.assertEquals(0, sessionFactory.getStatistics().getQueryCacheHitCount());
            }
        });
        tt.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                timeEntryDao.getTotalTime(2L);
                Assert.assertTrue("Query cache must be hit", sessionFactory.getStatistics().getQueryCacheHitCount() > 0);
            }
        });
    }

    public String[] processLocations(Class<?> clazz, String... locations) {
        return new String[] {"Non-existent.xml"};
    }

    public ApplicationContext loadContext(String... locations) throws Exception {
        return new ClassPathXmlApplicationContext(new String[]{"test-db.xml", "dao.xml"}, getClass());
    }
}

