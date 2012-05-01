package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 3/10/12
 * Time: 10:18 PM
 */

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     This class is responsible for lazy calculation of aggregate time spent for Activity.
 * </p>
 * <p>
 *     On every call of {@link LazyActivityTreeNode#getAggregateTimeSpent} method it checks whatever it is
 *     {@link LazyActivityTreeNode#valid aggregate time valid}. If it is not then
 *     {@link LazyActivityTreeNode#aggregateTimeSpent} is recalculated by summing up all children's
 *     aggregateTimeSpent values and its own timeSpent.
 * </p>
 * <p>
 *     On invalidation it invalidates parent ActivityTreeNode.
 * </p>
 */
public class LazyActivityTreeNode implements ActivityTreeNode {
    private boolean valid = false;
    private long id;
    private String name;
    private List<ActivityTreeNode> children = new LinkedList<ActivityTreeNode>();
    private long timeSpent;
    private long aggregateTimeSpent;
    private LazyActivityTreeNode parentActivityTreeNode;


    public LazyActivityTreeNode(long id, String name, long timeSpent) {
        this.id = id;
        this.name = name;
        this.timeSpent = timeSpent;
    }

    public void setParentActivityTreeNode(LazyActivityTreeNode parentActivityTreeNode) {
        this.parentActivityTreeNode = parentActivityTreeNode;
    }

    /**
     * Recalculates aggregateTimeSpent if not valid.
     */
    private void validate() {
        if (valid)
            return;

        aggregateTimeSpent = timeSpent;
        for (ActivityTreeNode child : children) {
            aggregateTimeSpent += child.getAggregateTimeSpent();
        }
        valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * Mark this node as invalid and call this method recursively.
     */
    public void invalidateActivityTreeNode() {
        valid = false;
        if (parentActivityTreeNode != null)
            parentActivityTreeNode.invalidateActivityTreeNode();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Iterable<ActivityTreeNode> getChildren() {
        return children;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public long getAggregateTimeSpent() {
        validate();
        return aggregateTimeSpent;
    }

    public void addChild(LazyActivityTreeNode child) {
        child.setParentActivityTreeNode(this);
        children.add(child);
    }
}
