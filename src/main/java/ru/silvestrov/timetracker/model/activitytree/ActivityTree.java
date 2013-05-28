package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 7/15/12
 * Time: 1:05 AM
 */
public interface ActivityTree<T extends ActivityTreeNode<T>> {
    /**
     * Return time spent by all activities in this tree
     *
     * @return time spent
     */
    long getAggregateTimeSpent();

    /**
     * @return nodes representing children of this activity.
     */
    Iterable<T> getChildren();

    void addChild(T child);
}
