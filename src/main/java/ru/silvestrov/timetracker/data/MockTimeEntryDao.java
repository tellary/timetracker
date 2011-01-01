package ru.silvestrov.timetracker.data;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 13, 2008
 * Time: 1:26:31 AM
 */
public class MockTimeEntryDao extends TimeEntryDao {
    public long getTotalTime(long activityId) {
        return 600 + 200*activityId;
    }
}
