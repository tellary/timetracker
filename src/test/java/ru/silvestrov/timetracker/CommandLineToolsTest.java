package ru.silvestrov.timetracker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.silvestrov.timetracker.data.*;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Ilya Silvestrov
 * Date: 3/31/13
 * Time: 5:41 PM
 */
public class CommandLineToolsTest {
    DataConfiguration dataConfig;

    @Before
    public void before() {
        DataConfigurationTestSetup dataTestSetup = new DataConfigurationTestSetup();
        dataConfig = dataTestSetup.getDataConfiguration();
        ActivityDao activityDao = dataConfig.getActivityDao();
        TimeEntryDao timeEntryDao = dataConfig.getTimeEntryDao();
        long baseTime = System.currentTimeMillis() - 10000;

        //Adding whole number of seconds
        TimeEntry timeEntry = new TimeEntry(baseTime, activityDao.getActivityById(1));
        timeEntry.setTimeEnd(baseTime + 1000);
        timeEntryDao.addTimeEntry(timeEntry);

        timeEntry = new TimeEntry(baseTime + 1000, activityDao.getActivityById(2));
        timeEntry.setTimeEnd(baseTime + 3000);
        timeEntryDao.addTimeEntry(timeEntry);
    }

    @Test
    public void testList() throws Exception {
        StringWriter sw = new StringWriter();
        CommandLineTools commandLineTools =
                new CommandLineTools(
                        dataConfig.getActivityDao(),
                        dataConfig.getTimeEntryDao(),
                        new PrintWriter(sw)
                );
        commandLineTools.list();
        System.out.println(sw);

        //Here first and second activity do actually have
        //1100 and 1200 milliseconds,
        //but only whole number of seconds is shown.
        Assert.assertEquals(
                "First Activity\t00:00:01\n" +
                "Second Activity\t00:00:02\n" +
                "Third Activity\t00:00:00\n" +
                "Total:\t00:00:03\n",
                sw.toString());
    }
}
