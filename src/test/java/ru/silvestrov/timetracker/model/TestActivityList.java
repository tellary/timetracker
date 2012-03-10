package ru.silvestrov.timetracker.model;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.DataConfiguration;
import ru.silvestrov.timetracker.data.DataConfigurationTestSetup;
import ru.silvestrov.timetracker.model.activitycontrollist.ActivityControlList;
import ru.silvestrov.timetracker.model.activitycontrollist.ActivityControlListUpdateListener;

/**
 * Created by Silvestrov Ilya.
 * Date: Jul 20, 2008
 * Time: 4:52:40 PM
 */
public class TestActivityList {
    private ActivityControlList activityControlList;
    @Before
    public void setUp() {
        DataConfigurationTestSetup dataTestSetup = new DataConfigurationTestSetup();
        DataConfiguration dataConfig = dataTestSetup.getDataConfiguration();
        activityControlList = new ActivityControlList();
        activityControlList.setDataConfiguration(dataConfig);
        activityControlList.afterPropertiesSet();
        activityControlList.setUpdateListener(new ActivityControlListUpdateListener() {
            public void activityTimeUpdated(int i) {
                System.out.println("Activity time updated for index " + i);
            }

            public void invalidateList() {
                System.out.println("Invalidate list called");
            }
        });
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
