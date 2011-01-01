package ru.silvestrov.timetracker.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 10, 2008
 * Time: 12:04:13 AM
 */
public class MockActivityDao extends ActivityDao {

    @SuppressWarnings({"unchecked"})
    public List<Activity> listCurrentActivities() {
        ArrayList<Activity> list = new ArrayList<Activity>(3);
        Activity activity;
        activity = new Activity();
        activity.setId(1);
        activity.setName("First Activity");
        activity.setFinished(false);
        list.add(activity);
        activity = new Activity();
        activity.setId(2);
        activity.setName("Second Activity");
        activity.setFinished(false);
//        activity.setActive(true);
        list.add(activity);
        activity = new Activity();
        activity.setId(3);
        activity.setName("Third Activity");
        activity.setFinished(false);
        list.add(activity);
        return list;
    }
}
