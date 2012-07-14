package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 3/10/12
 * Time: 10:13 PM
 */

/**
 * <p>
 *      {@code ActivityTreeNode}s compose an activity tree which is used to render activity tree.
 *      It is not intended to work with database. It is populated by some other code instead.
 * </p>
 *
 */
public interface ActivityTreeNode extends ActivityTree {
    /**
     * Method returns id of the activity represented by this node.
     * It is used to find ActivityTreeNode by id when necessary.
     *
     * @return id of the activity for this node
     */
    long getId();

    /**
     * Name of the activity used for rendering
     */
    String getName();

    /**
     * @return time spent by the activity itself
     */
    long getTimeSpent();
}
