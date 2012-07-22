package ru.silvestrov.timetracker.view.activitytree;

import ru.silvestrov.timetracker.model.activitytree.ActivityTree;
import ru.silvestrov.timetracker.model.activitytree.ActivityTreeNode;
import ru.silvestrov.timetracker.model.activitytree.HashActivityTree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Iterator;

/**
 * Created by Silvestrov Ilya
 * Date: 7/22/12
 * Time: 3:33 PM
 */
public class ActivityTreeModel implements TreeModel {
    private HashActivityTree tree;

    public ActivityTreeModel(HashActivityTree tree) {
        this.tree = tree;
    }

    @Override
    public Object getRoot() {
        return tree;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (index < 0)
            throw new RuntimeException("Negative index is not supported");

        ActivityTree parentNode = (ActivityTree) parent;
        Iterator<ActivityTreeNode> iterator = parentNode.getChildren().iterator();
        ActivityTreeNode child = null;

        for (int i = 0; i <= index; ++i) {
            if (iterator.hasNext()) {
                child = iterator.next();
            } else {
                throw new RuntimeException(
                        String.format("No child on index %s", index));
            }
        }
        return child;
    }

    @Override
    public int getChildCount(Object parent) {
        int count = 0;
        ActivityTree tree = (ActivityTree) parent;
        Iterator<ActivityTreeNode> children = tree.getChildren().iterator();
        //noinspection WhileLoopReplaceableByForEach
        while(children.hasNext()) {
            children.next();
            ++count;
        }
        return count;
    }

    @Override
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        ActivityTree parentNode = (ActivityTree) parent;
        int idx = 0;
        for (ActivityTreeNode childNode : parentNode.getChildren()) {
            if (childNode == child) {
                return idx;
            }
            ++idx;
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        System.out.println(String.format("Setting TreeModelListener %s", l));
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        System.out.println(String.format("Removing TreeModelListener %s", l));
    }
}
