package ru.silvestrov.timetracker.model.activitycontrollist;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.DataSetup;

import javax.annotation.Resource;

/**
 * Created by Silvestrov Ilya.
 * Date: Jul 20, 2008
 * Time: 4:52:40 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/ru/silvestrov/timetracker/data/test-db.xml",
    "/ru/silvestrov/timetracker/data/dao.xml", "activityControlList.xml"})
public class TestActivityList {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private DataSetup dataSetup;
    @Resource(shareable = false)
    private ActivityControlList activityControlList;

    @Before
    public void setUp() {
        activityControlList = (ActivityControlList) applicationContext.getBean("activityControlList");
        activityControlList.setUpdateListener(new ActivityControlListUpdateListener() {
            public void activityTimeUpdated(int i) {
                System.out.println("Activity time updated for index " + i);
            }

            public void invalidateList() {
                System.out.println("Invalidate list called");
            }
        });
        dataSetup.setup();
        //This call is necessary to reset activity list to be in sync with db
        activityControlList.afterPropertiesSet();
    }

    @After
    public void after() {
        activityControlList.stopTimer();
    }


    @Test
    public void testSwitchActivity() {
        activityControlList.makeActive(0);
        activityControlList.makeActive(2);
        activityControlList.makeActive(1);
    }

    @Test
    public void testFinishInactive() {
        int size = activityControlList.size();
        activityControlList.makeActive(0);
        activityControlList.finishActivity(1);
        Assert.assertEquals(size - 1, activityControlList.size());
        checkAllInactive();
    }

    @Test
    public void testFinishActive() {
        int size = activityControlList.size();
        activityControlList.makeActive(0);
        activityControlList.finishActivity(0);
        Assert.assertEquals(size - 1, activityControlList.size());
        checkAllInactive();
    }

    private void checkAllInactive() {
        for (int i = 0; i < activityControlList.size(); ++i) {
            Activity a = activityControlList.getActivity(i);
            Assert.assertNull(a.getCurrentTimeEntry());
        }
    }
}
