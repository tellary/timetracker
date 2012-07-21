package ru.silvestrov.timetracker.model.activitytree;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.ActivityDao;
import ru.silvestrov.timetracker.data.TimeEntry;
import ru.silvestrov.timetracker.data.TimeEntryDao;

import javax.annotation.Resource;
import java.util.Iterator;

/**
 * Created by Silvestrov Ilya
 * Date: 7/21/12
 * Time: 10:44 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "/ru/silvestrov/timetracker/data/test-db.xml",
        "/ru/silvestrov/timetracker/data/dao.xml",
        "activityTree.xml"
})
public class ActivityTreeManagerWithRealDataTest {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private TimeEntryDao timeEntryDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private ActivityTreeManager activityTreeManager;


    @Before
    public void before() {
        jdbcTemplate.execute("delete from time_entry");
        jdbcTemplate.execute("delete from activity");
    }

    private Activity createActivity(String name, long timeSec) {
        Activity a = new Activity();
        a.setName(name);
        activityDao.save(a);
        long start = System.currentTimeMillis();
        TimeEntry te = new TimeEntry(start, a);
        te.setTimeEnd(start + timeSec*1000);
        timeEntryDao.save(te);
        return a;
    }

    private void setParent(Activity a, Activity p) {
        activityDao.setParent(a, p);
        activityDao.save(a);
    }


    @Test
    public void test() {
        Activity a1 = createActivity("a1", 60);
        Activity a2 = createActivity("a2", 30);
        createActivity("a3", 780);
        Activity a11 = createActivity("a11", 78);
        Activity a12 = createActivity("a12", 80);
        setParent(a11, a1);
        setParent(a12, a1);
        Activity a21 = createActivity("a21", 180);
        setParent(a21, a2);

        ActivityTree tree = activityTreeManager.loadAllActivitiesTree();
        Iterator<ActivityTreeNode> children = tree.getChildren().iterator();
        ActivityTreeNode child = children.next();
        Assert.assertEquals("a1", child.getName());
        Iterator<ActivityTreeNode> grandChildren = child.getChildren().iterator();
        ActivityTreeNode grandChild = grandChildren.next();
        Assert.assertEquals("a11", grandChild.getName());
        grandChild = grandChildren.next();
        Assert.assertEquals("a12", grandChild.getName());
        Assert.assertFalse(grandChildren.hasNext());
        child = children.next();
        Assert.assertEquals("a2", child.getName());
        grandChildren = child.getChildren().iterator();
        grandChild = grandChildren.next();
        Assert.assertEquals("a21", grandChild.getName());
        Assert.assertFalse(grandChildren.hasNext());
        child = children.next();
        Assert.assertEquals("a3", child.getName());
        grandChildren = child.getChildren().iterator();
        Assert.assertFalse(grandChildren.hasNext());
        Assert.assertFalse(children.hasNext());
        Assert.assertEquals((60+30+780+78+80+180)*1000, tree.getAggregateTimeSpent());
    }
}
