package ru.silvestrov.timetracker.model.activitytree;

import java.util.HashMap;

/**
 * Created by Silvestrov Ilya
 * Date: 7/15/12
 * Time: 1:06 AM
 */
public class HashActivityTree implements ActivityTree {
    private HashMap<Long, ActivityTreeNode> nodes = new HashMap<>();
    private ActivityTree tree;

    public HashActivityTree(ActivityTree tree) {
        this.tree = tree;
    }

    public long getAggregateTimeSpent() {
        return tree.getAggregateTimeSpent();
    }

    public Iterable<ActivityTreeNode> getChildren() {
        return tree.getChildren();
    }

    @Override
    public void addChild(ActivityTreeNode child) {
        tree.addChild(child);
    }

    @Override
    public void removeChild(ActivityTreeNode child) {
        tree.removeChild(child);
    }

    @Override
    public void invalidateTree() {
        tree.invalidateTree();
    }

    public ActivityTree getTree() {
        return tree;
    }

    public void addChild(ActivityTreeNode child, Long parentId) {
        if (nodes.put(child.getId(), child) != null) {
            throw new RuntimeException("Activity with the same id already exist");
        }
        if (parentId == null) {
            tree.addChild(child);
        } else {
            ActivityTree parentNode = nodes.get(parentId);
            parentNode.addChild(child);
        }
    }
}
