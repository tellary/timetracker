package ru.silvestrov.timetracker.model.activitytree;

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

    public ActivityTree<? extends ActivityTreeNode<?>> loadAllActivitiesTree() {
        HashActivityTree<LazyActivityTreeNode> activityTree = new HashActivityTree<>(new LazyActivityTree());
        List<Activity> activities = activityDao.findRootActivities();
        for (Activity activity : activities) {
            activityTree.addChild(
                    treeNode(activity),
                    null);
            loadActivitiesForParent(activityTree, activity.getId());
        }
        return activityTree;
    }

    private LazyActivityTreeNode treeNode(Activity activity) {
        return new LazyActivityTreeNode(
                activity.getId(),
                activity.getName(),
                timeEntryDao.getTotalTime(activity.getId()));
    }

    public ActivityTree loadActivitiesForParent(long parentId) {
        HashActivityTree<LazyActivityTreeNode> activityTree = new HashActivityTree<>(new LazyActivityTree());
        Activity activity = activityDao.getActivityById(parentId);
        activityTree.addChild(treeNode(activity), null);

        loadActivitiesForParent(activityTree, parentId);
        return activityTree;
    }

    private void loadActivitiesForParent(HashActivityTree<LazyActivityTreeNode> tree, long parentId) {
        List<Activity> activities = activityDao.findActivitiesByParentId(parentId);
        for (Activity activity : activities) {
            tree.addChild(treeNode(activity), parentId);
            loadActivitiesForParent(tree, activity.getId());
        }
    }
}
