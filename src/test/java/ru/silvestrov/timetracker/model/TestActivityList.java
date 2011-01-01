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
    private ActivityList activityList;
    @Before
    public void setUp() {
        DataConfigurationTestSetup dataTestSetup = new DataConfigurationTestSetup();
        DataConfiguration dataConfig = dataTestSetup.getDataConfiguration();
        activityList = new ActivityList();
        activityList.setDataConfiguration(dataConfig);
        activityList.afterPropertiesSet();
    }


    @Test
    public void testSwitchActivity() {
        activityList.makeActive(0);
        activityList.makeActive(2);
        activityList.makeActive(1);
    }
}
