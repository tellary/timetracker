package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 6/15/13
 * Time: 1:01 PM
 */
public final class ActivityTreeAndNodeMover {
    private ActivityTree activityTree;
    private ActivityTreeNodeMover activityTreeNodeMover;

    public ActivityTreeAndNodeMover(
            ActivityTree activityTree,
            ActivityTreeNodeMover activityTreeNodeMover) {
        this.activityTree = activityTree;
        this.activityTreeNodeMover = activityTreeNodeMover;
    }

    public ActivityTree getActivityTree() {
        return activityTree;
    }

    public ActivityTreeNodeMover getActivityTreeNodeMover() {
        return activityTreeNodeMover;
    }
}
