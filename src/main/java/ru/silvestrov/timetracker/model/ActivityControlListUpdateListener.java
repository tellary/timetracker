package ru.silvestrov.timetracker.model;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 12, 2008
 * Time: 9:19:10 PM
 */
public interface ActivityControlListUpdateListener {
    public void activityTimeUpdated(int i);
    public void invalidateList();
}
