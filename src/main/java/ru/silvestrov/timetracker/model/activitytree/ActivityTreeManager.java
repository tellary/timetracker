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
        final HashActivityTree hashActivityTree = new HashActivityTree(new LazyActivityTree());
        List<Activity> activities = activityDao.findRootActivities();
        for (Activity activity : activities) {
            hashActivityTree.addChild(
                    treeNode(activity),
                    null);
            loadActivitiesForParent(hashActivityTree, activity.getId());
        }
        return new ActivityTreeAndNodeMover(
                hashActivityTree,
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
                                hashActivityTree.move(newParent, child);
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

    public ActivityTree loadActivitiesForParent(long parentId) {
        HashActivityTree activityTree = new HashActivityTree(new LazyActivityTree());
        Activity activity = activityDao.getActivityById(parentId);
        activityTree.addChild(treeNode(activity), null);

        loadActivitiesForParent(activityTree, parentId);
        return activityTree.getTree();
    }

    private void loadActivitiesForParent(HashActivityTree tree, long parentId) {
        List<Activity> activities = activityDao.findActivitiesByParentId(parentId);
        for (Activity activity : activities) {
            tree.addChild(treeNode(activity), parentId);
            loadActivitiesForParent(tree, activity.getId());
        }
    }
}
