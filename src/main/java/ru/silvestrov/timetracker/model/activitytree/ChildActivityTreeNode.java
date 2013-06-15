package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 6/10/13
 * Time: 9:20 PM
 */
public interface ChildActivityTreeNode extends ActivityTreeNode, ParentActivityTree {
    void setParent(ParentActivityTree parent);
}
