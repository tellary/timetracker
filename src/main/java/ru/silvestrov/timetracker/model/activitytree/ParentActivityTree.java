package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 6/10/13
 * Time: 9:19 PM
 */
public interface ParentActivityTree extends ActivityTree {

    void addChild(ChildActivityTreeNode child);

    void removeChild(ChildActivityTreeNode child);

    void invalidateTree();
}
