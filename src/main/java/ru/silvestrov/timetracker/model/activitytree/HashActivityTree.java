package ru.silvestrov.timetracker.model.activitytree;

import java.util.HashMap;

/**
 * Created by Silvestrov Ilya
 * Date: 7/15/12
 * Time: 1:06 AM
 */
public class HashActivityTree implements ActivityTree {
    private HashMap<Long, LazyActivityTreeNode> nodes = new HashMap<Long, LazyActivityTreeNode>();
    private LazyActivityTree tree = new LazyActivityTree();

    public long getAggregateTimeSpent() {
        return tree.getAggregateTimeSpent();
    }

    public Iterable<ActivityTreeNode> getChildren() {
        return tree.getChildren();
    }

    public void invalidateAggregateTimeSpent() {
        tree.invalidateAggregateTimeSpent();
    }

    public void addChild(LazyActivityTreeNode child, Long parentId) {
        if (nodes.put(child.getId(), child) != null) {
            throw new RuntimeException("Activity with the same id used to already exist in the Hash");
        }
        if (parentId == null) {
            tree.addChild(child);
        } else {
            LazyActivityTreeNode parentNode = nodes.get(parentId);
            parentNode.addChild(child);
        }
    }
}
