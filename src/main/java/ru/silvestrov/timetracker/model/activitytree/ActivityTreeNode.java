package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 3/10/12
 * Time: 10:13 PM
 */
public interface ActivityTreeNode {
    Iterable<ActivityTreeNode> getChildren();

    long getTimeSpent();

    long getAggregateTimeSpent();
}
