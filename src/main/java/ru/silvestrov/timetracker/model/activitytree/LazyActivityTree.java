package ru.silvestrov.timetracker.model.activitytree;

import org.apache.log4j.Logger;

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
    private static final Logger logger = Logger.getLogger(LazyActivityTree.class);

    private boolean valid = false;
    private List<ActivityTreeNode> children = new LinkedList<>();
    private long aggregateTimeSpent;

    /**
     * Recalculates aggregateTimeSpent if not valid.
     */
    private void validate() {
        if (valid)
            return;

        logger.debug("Found \"invalid\" state, recalculating aggregate time");
        for (ActivityTree child : children) {
            aggregateTimeSpent += child.getAggregateTimeSpent();
        }
        aggregateTimeSpent += getTimeSpent();
        valid = true;
    }

    protected long getTimeSpent() {
        return 0;
    }

    public long getAggregateTimeSpent() {
        validate();
        return aggregateTimeSpent;
    }

    public Iterable<ActivityTreeNode> getChildren() {
        return children;
    }

    private void invalidateAggregateTimeSpent() {
        valid = false;
        aggregateTimeSpent = 0;
        logger.debug("Time invalidated");
    }

    @Override
    public void invalidateTree() {
        invalidateAggregateTimeSpent();
    }

    public void addChild(ActivityTreeNode child) {
        child.setParent(this);
        children.add(child);
        invalidateTree();
    }

    public void removeChild(ActivityTreeNode child) {
        children.remove(child);
        invalidateTree();
    }
}
