package ru.silvestrov.timetracker.data;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.TransactionStatus;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.ITable;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 13, 2008
 * Time: 2:21:09 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"test-db.xml", "dao.xml"})
public class ActivityDaoTest {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private TimeEntryDao timeEntryDao;
    @Resource
    private TransactionTemplate tt;
    @Resource
    private IDatabaseTester tester;
    @Resource
    private DataSetup dataSetup;
      

    @Before
    public void setUp() {
        dataSetup.setup();
    }

    @Test
    public void test() {
        tt.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                List<Activity> activities = activityDao.listCurrentActivities();
                Assert.assertEquals(3, activities.size());
                return null;
            }
        });
    }

    @Test
    public void testAddActivity() throws Exception {
        tt.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Activity a = new Activity();
                a.setName("test");
                activityDao.save(a);
            }
        });

        ITable activity = tester.getConnection().createQueryTable("activity", "select * from activity where name = 'test'");
        Assert.assertEquals(1, activity.getRowCount());
    }

    @Test
    public void testGetTotalTime() {
        tt.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Assert.assertEquals(200, timeEntryDao.getTotalTime(2L));
            }
        });
    }
}