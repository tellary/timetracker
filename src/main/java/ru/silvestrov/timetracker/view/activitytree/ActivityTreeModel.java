package ru.silvestrov.timetracker.view.activitytree;

import ru.silvestrov.timetracker.model.activitytree.ActivityTree;
import ru.silvestrov.timetracker.model.activitytree.ActivityTreeNode;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Silvestrov Ilya
 * Date: 7/22/12
 * Time: 3:33 PM
 */
public class ActivityTreeModel implements TreeModel {
    private ActivityTree tree;
    private List<TreeModelListener> treeModelListeners = new ArrayList<>();

    public ActivityTreeModel(ActivityTree tree) {
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

        ActivityTree<? extends ActivityTreeNode> parentNode = (ActivityTree<? extends ActivityTreeNode>) parent;
        Iterator<? extends ActivityTreeNode> iterator = parentNode.getChildren().iterator();
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
        ActivityTree<? extends ActivityTreeNode> tree = (ActivityTree<? extends ActivityTreeNode>) parent;
        Iterator<? extends ActivityTreeNode> children = tree.getChildren().iterator();
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
        ActivityTree<? extends ActivityTreeNode> parentNode = (ActivityTree<? extends ActivityTreeNode>) parent;
        int idx = 0;
        for (ActivityTreeNode childNode : parentNode.getChildren()) {
            if (childNode == child) {
                return idx;
            }
            ++idx;
        }
        return -1;
    }

    public void treeNodeInserted(TreePath parentPath, ActivityTreeNode child) {
        for (TreeModelListener listener : treeModelListeners) {
            int[] childIndices = new int[1];
            Object parent = parentPath.getLastPathComponent();
            childIndices[0] = getIndexOfChild(parent, child);
            TreeModelEvent e = new TreeModelEvent(this, parentPath, childIndices, new Object[]{child});
            listener.treeNodesInserted(e);
        }
    }

    public void treeNodeRemoved(TreePath parentPath, ActivityTreeNode child, int oldIndex) {
        for (TreeModelListener listener : treeModelListeners) {
            int[] childIndices = new int[1];
            childIndices[0] = oldIndex;
            TreeModelEvent e = new TreeModelEvent(this, parentPath, childIndices, new Object[]{child});
            listener.treeNodesRemoved(e);
        }
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(l);
    }
}
