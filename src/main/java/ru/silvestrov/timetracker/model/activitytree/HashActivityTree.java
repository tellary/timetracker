package ru.silvestrov.timetracker.model.activitytree;

import java.util.HashMap;

/**
 * Created by Silvestrov Ilya
 * Date: 7/15/12
 * Time: 1:06 AM
 */
public class HashActivityTree implements MovableActivityTree, ParentActivityTree {
    private HashMap<Long, ChildActivityTreeNode> nodes = new HashMap<>();
    private ParentActivityTree tree;

    public HashActivityTree(ParentActivityTree tree) {
        this.tree = tree;
    }

    public long getAggregateTimeSpent() {
        return tree.getAggregateTimeSpent();
    }

    public Iterable<ActivityTreeNode> getChildren() {
        return tree.getChildren();
    }

    @Override
    public void addChild(ChildActivityTreeNode child) {
        tree.addChild(child);
    }

    @Override
    public void removeChild(ChildActivityTreeNode child) {
        tree.removeChild(child);
    }

    @Override
    public void invalidateTree() {
        tree.invalidateTree();
    }

    public ActivityTree getTree() {
        return tree;
    }

    @Override
    public void move(ActivityTree newParent, ActivityTreeNode childNode) {
        ParentActivityTree parent = (ParentActivityTree) newParent;
        ChildActivityTreeNode child = (ChildActivityTreeNode) childNode;
        parent.addChild(child);
    }

    public void addChild(ChildActivityTreeNode child, Long parentId) {
        if (nodes.put(child.getId(), child) != null) {
            throw new RuntimeException("Activity with the same id already exist");
        }
        if (parentId == null) {
            tree.addChild(child);
        } else {
            ParentActivityTree parentNode = nodes.get(parentId);
            parentNode.addChild(child);
        }
    }
}
