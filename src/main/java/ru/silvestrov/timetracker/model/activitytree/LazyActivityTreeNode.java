package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 3/10/12
 * Time: 10:18 PM
 */

import org.springframework.transaction.support.TransactionTemplate;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.ActivityDao;

/**
 * <p>
 *     This class loads information about children and aggregate time spent lazily.
 *
 *     On every call to getTimeSpent, getAggregateTimeSpent or getChildren it start validation procedure.
 *     Validation procedure includes getting children nodes from the database and calling calculateAggregateTimeSpent
 *     procedure. This procedure sum ups time aggregateTimeSpent through all child tree nodes.
 *
 *     LazyActivityTreeNode manages transaction itself. If validation procedure is necessary then it is performed
 *     within a transaction.
 *
 *     On invalidation it invalidates parent ActivityTreeNode.
 * </p>
 *
 */
public class LazyActivityTreeNode implements ActivityTreeNode {
    private boolean valid = false;
    private LazyActivityTreeNode parentActivityTreeNode;
    private ActivityDao activityDao;
    private TransactionTemplate tt;

    public LazyActivityTreeNode(Activity activity, LazyActivityTreeNode parentActivityTreeNode) {
        this.parentActivityTreeNode = parentActivityTreeNode;
    }

    private void calculateAggregateTimeSpent() {

    }

    private void validate() {

        if (valid)
            //noinspection UnnecessaryReturnStatement
            return;

        //TODO: Implement and remove noinpection above
    }

    public void invalidateActivityTreeNode() {
        valid = false;

        parentActivityTreeNode.invalidateActivityTreeNode();
    }

    public Iterable<ActivityTreeNode> getChildren() {
        validate();
        return null;
    }

    public long getTimeSpent() {
        validate();
        return 0;
    }

    public long getAggregateTimeSpent() {
        validate();
        return 0;
    }
}
