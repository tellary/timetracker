package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 3/10/12
 * Time: 10:18 PM
 */

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.ActivityDao;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedList;

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
    private long activityId;
    private LazyActivityTreeNode parentActivityTreeNode;
    private long timeSpent;
    private String beanName;
    private long aggregateTimeSpent;

    /**
     * Recalculates aggregateTimeSpent if not valid.
     */
    private void validate() {
        if (valid)
            return;
    }

    /**
     * Mark this node as invalid and call this method recursively.
     */
    public void invalidateActivityTreeNode() {
        valid = false;
        parentActivityTreeNode.invalidateActivityTreeNode();
    }

    public long getId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Iterable<ActivityTreeNode> getChildren() {
        validate();
        return null;
    }

    public long getTimeSpent() {
        validate();
        return timeSpent;
    }

    public long getAggregateTimeSpent() {
        validate();
        return aggregateTimeSpent;
    }
}
