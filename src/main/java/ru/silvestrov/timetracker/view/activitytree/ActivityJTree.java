package ru.silvestrov.timetracker.view.activitytree;

import ru.silvestrov.timetracker.model.activitycontrollist.TimeHelp;
import ru.silvestrov.timetracker.model.activitytree.ActivityTree;
import ru.silvestrov.timetracker.model.activitytree.ActivityTreeNode;
import ru.silvestrov.timetracker.model.activitytree.ActivityTreeNodeMover;

import javax.swing.*;

/**
 * Created by Silvestrov Ilya
 * Date: 7/22/12
 * Time: 3:08 PM
 */
public class ActivityJTree extends JTree {
    public ActivityJTree(ActivityTree tree, ActivityTreeNodeMover treeNodeMover) {
        super(new ActivityTreeModel(tree));
        setDragEnabled(true);
        setTransferHandler(
                new ActivityJTreeTransferHandler(
                        (ActivityTreeModel) getModel(), treeNodeMover));
    }

    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof ActivityTreeNode) {
            ActivityTreeNode node = (ActivityTreeNode) value;
            long timeSpent = node.getTimeSpent();
            long aggregateTimeSpent = node.getAggregateTimeSpent();
            if (timeSpent == aggregateTimeSpent)
                return node.getName() + " (" + TimeHelp.formatTime(timeSpent) + ")";
            else
                return node.getName() +
                        " (" + TimeHelp.formatTime(timeSpent) + "/" +
                        TimeHelp.formatTime(aggregateTimeSpent) + ")";
        } else if (value instanceof ActivityTree) {
            ActivityTree tree = (ActivityTree) value;
            return TimeHelp.formatTime(tree.getAggregateTimeSpent());
        } else {
            return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        }
    }
}
