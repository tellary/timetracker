package ru.silvestrov.timetracker.model.activitytree;

import java.util.HashMap;

/**
 * Created by Silvestrov Ilya
 * Date: 7/15/12
 * Time: 1:06 AM
 */
public class HashActivityTree<T extends ActivityTreeNode<T>> implements ActivityTree<T> {
    private HashMap<Long, T> nodes = new HashMap<>();
    private ActivityTree<T> tree;

    public HashActivityTree(ActivityTree<T> tree) {
        this.tree = tree;
    }

    public long getAggregateTimeSpent() {
        return tree.getAggregateTimeSpent();
    }

    public Iterable<T> getChildren() {
        return tree.getChildren();
    }

    @Override
    public void addChild(T child) {
        tree.addChild(child);
    }

    public ActivityTree<T> getTree() {
        return tree;
    }

    public void addChild(T child, Long parentId) {
        if (nodes.put(child.getId(), child) != null) {
            throw new RuntimeException("Activity with the same id already exist");
        }
        if (parentId == null) {
            tree.addChild(child);
        } else {
            T parentNode = nodes.get(parentId);
            parentNode.addChild(child);
        }
    }
}
