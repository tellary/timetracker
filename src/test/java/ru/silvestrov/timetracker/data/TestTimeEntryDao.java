package ru.silvestrov.timetracker.data;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Silvestrov Ilya
 * Date: 1/19/12
 * Time: 10:47 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"test-db.xml", "dao.xml"})
public class TestTimeEntryDao {
    @Resource
    private TimeEntryDao timeEntryDao;
    @Resource
    private DataSetup setup;

    @Before
    public void setUp() {
        setup.setup();
    }

    @Test
    public void testLatestTimeEntry() {
        TimeEntry t = timeEntryDao.getLastTimeEntry(2);
        Assert.assertNotNull(t);
        Assert.assertEquals(200, t.getTimeStart());
    }

    @Test
    public void testLatestTimeEntryForEmptyActivity() {
        TimeEntry t = timeEntryDao.getLastTimeEntry(1);
        Assert.assertNull(t);
    }
}
