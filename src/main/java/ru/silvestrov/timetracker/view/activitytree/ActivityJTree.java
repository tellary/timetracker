package ru.silvestrov.timetracker.view.activitytree;

import ru.silvestrov.timetracker.model.activitytree.ActivityTree;
import ru.silvestrov.timetracker.model.activitytree.ActivityTreeNode;

import javax.swing.*;

/**
 * Created by Silvestrov Ilya
 * Date: 7/22/12
 * Time: 3:08 PM
 */
public class ActivityJTree extends JTree {
    public ActivityJTree(ActivityTree tree) {
        super(new ActivityTreeModel(tree));
    }

    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof ActivityTreeNode) {
            ActivityTreeNode node = (ActivityTreeNode) value;
            return node.getName() + "(" + node.getTimeSpent() + " mins)";
        } else if (value instanceof ActivityTree) {
            return "";
        } else {
            return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        }
    }
}
