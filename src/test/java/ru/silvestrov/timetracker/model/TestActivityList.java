package ru.silvestrov.timetracker.model;

import org.junit.Before;
import org.junit.Test;
import ru.silvestrov.timetracker.data.DataConfiguration;
import ru.silvestrov.timetracker.data.DataConfigurationTestSetup;

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
    }


    @Test
    public void testSwitchActivity() {
        activityControlList.makeActive(0);
        activityControlList.makeActive(2);
        activityControlList.makeActive(1);
    }
}
