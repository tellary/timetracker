package ru.silvestrov.timetracker.data;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.Collection;
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
    private DataSetup dataSetup;
    @Resource
    private JdbcTemplate jdbcTemplate;
      

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

        Assert.assertEquals(1, jdbcTemplate.queryForInt("select count(*) from activity where name = 'test'"));
    }

    @Test
    public void testGetTotalTime() {
        tt.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Assert.assertEquals(200, timeEntryDao.getTotalTime(2L));
            }
        });
    }

    @Test
    public void testChildren() {
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Activity activity = activityDao.getActivityById(1);
                Collection<Activity> children = activity.getChildren();
                Assert.assertEquals(2, children.size());

                for (Activity child : children) {
                    if (child.getId() == 2) {
                        Assert.assertEquals("Second Activity", child.getName());
                    } else if (child.getId() == 3) {
                        Assert.assertEquals("Third Activity", child.getName());
                    }
                }
            }
        });
    }



    @Test
    public void testChangeParent() {
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Activity activity = activityDao.getActivityById(1);
                Collection<Activity> children = activity.getChildren();
                Assert.assertEquals(2, children.size());

                for (Activity child : children) {
                    if (child.getId() == 2) {
                        Assert.assertEquals("Second Activity", child.getName());
                    } else if (child.getId() == 3) {
                        Assert.assertEquals("Third Activity", child.getName());
                        activityDao.setParent(child, activityDao.getActivityById(2));
                    }
                }
            }
        });
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Activity activity = activityDao.getActivityById(1);
                Collection<Activity> children = activity.getChildren();
                Assert.assertEquals(1, children.size());

                Activity child = children.iterator().next();
                Assert.assertEquals(2, child.getId());

                children = child.getChildren();
                Assert.assertEquals(1, children.size());
                Activity grandChild = children.iterator().next();
                Assert.assertEquals(3, grandChild.getId());
                Assert.assertEquals(0, grandChild.getChildren().size());
            }
        });
    }
}