package ru.silvestrov.timetracker.data;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.TransactionStatus;
import org.hibernate.SessionFactory;
import junit.framework.Assert;

/**
 * Created by Silvestrov Iliya.
 * Date: Jul 15, 2008
 * Time: 12:54:01 AM
 */
public class TestTimeEntryCaching {
    private TimeEntryDao timeEntryDao;
    private TransactionTemplate tt;
    private SessionFactory sessionFactory;

    @Before
    public void setUp() {
        DataConfiguration dataConfiguration = new DataConfiguration("./testDB", "test-db.xml", "context.xml");
        timeEntryDao = dataConfiguration.getTimeEntryDao();
        tt = dataConfiguration.getTransactionTemplate();
        sessionFactory = dataConfiguration.getSessionFactory();
    }

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
}

