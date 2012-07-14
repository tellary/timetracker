package ru.silvestrov.timetracker.model.activitytree;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Silvestrov Ilya
 * Date: 7/15/12
 * Time: 1:15 AM
 */
/**
 * <p>
 *     This class is responsible for lazy calculation of aggregate time spent for Activity.
 * </p>
 * <p>
 *     On every call of {@link LazyActivityTreeNode#getTimeSpent} method it checks whatever it is
 *     {@link LazyActivityTree#valid aggregate time valid}. If it is not then
 *     {@link LazyActivityTree#aggregateTimeSpent} is recalculated by summing up all children's
 *     aggregateTimeSpent values and its own timeSpent.
 * </p>
 * <p>
 *     On invalidation it invalidates parent ActivityTreeNode.
 * </p>
 */
public class LazyActivityTree implements ActivityTree {
    private boolean valid = false;
    private List<ActivityTreeNode> children = new LinkedList<ActivityTreeNode>();
    private long aggregateTimeSpent;
    private LazyActivityTree parentActivityTree;

    /**
     * Recalculates aggregateTimeSpent if not valid.
     */
    private void validate() {
        if (valid)
            return;

        aggregateTimeSpent = 0;
        for (ActivityTree child : children) {
            aggregateTimeSpent += child.getAggregateTimeSpent();
        }
        valid = true;
        aggregationComplete();
    }

    public long getAggregateTimeSpent() {
        validate();
        return aggregateTimeSpent;
    }

    public Iterable<ActivityTreeNode> getChildren() {
        return children;
    }

    public void invalidateAggregateTimeSpent() {
        valid = false;
        if (parentActivityTree != null)
            parentActivityTree.invalidateAggregateTimeSpent();
    }

    public void addChild(LazyActivityTreeNode child) {
        child.setParentActivityTree(this);
        children.add(child);
    }

    public void aggregationComplete() {
    }

    public void setParentActivityTree(LazyActivityTree parentActivityTree) {
        this.parentActivityTree = parentActivityTree;
    }
}
