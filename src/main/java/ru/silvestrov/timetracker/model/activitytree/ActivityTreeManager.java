package ru.silvestrov.timetracker.model.activitytree;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.ActivityDao;
import ru.silvestrov.timetracker.data.TimeEntryDao;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Silvestrov Ilya
 * Date: 7/21/12
 * Time: 8:12 PM
 */
public class ActivityTreeManager {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private TimeEntryDao timeEntryDao;
    @Resource
    private TransactionTemplate transactionTemplate;

    public ActivityTreeAndNodeMover loadAllActivitiesTree() {
        ParentActivityTree activityTree = new LazyActivityTree();
        addChildActivitiesRecursive(activityTree, activityDao.findRootActivities());
        return new ActivityTreeAndNodeMover(
                activityTree,
                new ActivityTreeNodeMover() {
                    @Override
                    public void move(final ActivityTree newParent, final ActivityTreeNode child) {
                        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                            @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {
                                if (newParent.isNode()) {
                                    ActivityTreeNode newParentNode = (ActivityTreeNode) newParent;
                                    activityDao.setParent(newParentNode.getId(), child.getId());
                                } else {
                                    activityDao.unsetParent(child.getId());
                                }
                                ParentActivityTree parent = (ParentActivityTree) newParent;
                                parent.addChild((ChildActivityTreeNode) child);
                            }
                        });
                    }
                }
        );
    }

    private LazyActivityTreeNode treeNode(Activity activity) {
        return new LazyActivityTreeNode(
                activity.getId(),
                activity.getName(),
                timeEntryDao.getTotalTime(activity.getId()));
    }

    private void addChildActivitiesRecursive(ParentActivityTree tree, List<Activity> activities) {
        for (Activity activity : activities) {
            ChildActivityTreeNode childTreeNode = treeNode(activity);
            tree.addChild(childTreeNode);
            loadActivitiesForParent(childTreeNode, activity.getId());
        }
    }

    private void loadActivitiesForParent(ParentActivityTree tree, long parentId) {
        addChildActivitiesRecursive(tree, activityDao.findActivitiesByParentId(parentId));
    }
}
