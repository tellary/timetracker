package ru.silvestrov.timetracker.model.activitytree;

import java.util.List;

/**
 * Created by Silvestrov Ilya
 * Date: 8/18/13
 * Time: 10:04 PM
 */
public interface TreeNodeLoadStrategy {
    List<ActivityTreeNode> loadChildNodes(long parentActivityId);
}
