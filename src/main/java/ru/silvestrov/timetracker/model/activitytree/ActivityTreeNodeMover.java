package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 6/10/13
 * Time: 9:18 PM
 */
public interface ActivityTreeNodeMover {
    void move(ActivityTree newParent, ActivityTreeNode child);
}
