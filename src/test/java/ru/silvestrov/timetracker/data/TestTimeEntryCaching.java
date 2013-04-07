package ru.silvestrov.timetracker.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
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
@ContextConfiguration(locations = {"test-db.xml", "dao.xml"})
public class TestTimeEntryCaching {
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
                timeEntryDao.getTotalTime(2L);
                Assert.assertTrue(sessionFactory.getStatistics().getQueryCacheHitCount() > 0);
                sessionFactory.getCache().evictQueryRegions();
                sessionFactory.getCache().evictCollectionRegions();
                sessionFactory.getCache().evictEntityRegions();
                sessionFactory.getCache().evictDefaultQueryRegion();
                sessionFactory.getStatistics().clear();
                Assert.assertEquals(0, sessionFactory.getStatistics().getQueryCacheHitCount());
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

