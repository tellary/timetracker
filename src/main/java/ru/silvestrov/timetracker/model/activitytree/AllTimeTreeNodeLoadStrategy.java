package ru.silvestrov.timetracker.model.activitytree;

import org.springframework.transaction.annotation.Transactional;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.ActivityDao;
import ru.silvestrov.timetracker.data.TimeEntryDao;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Silvestrov Ilya
 * Date: 8/18/13
 * Time: 10:48 PM
 */
public class AllTimeTreeNodeLoadStrategy implements TreeNodeLoadStrategy {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private TimeEntryDao timeEntryDao;

    @Override
    @Transactional
    public List<ActivityTreeNode> loadChildNodes(long parentActivityId) {
        List<Activity> activities = activityDao.findActivitiesByParentId(parentActivityId);
        List<ActivityTreeNode> activityNodes = new ArrayList<>(activities.size());
        for (Activity activity : activities) {
            long totalOwnTime = timeEntryDao.getTotalTime(activity.getId());
            activityNodes.add(new LazyActivityTreeNode(
                    activity.getId(), activity.getName(),
                    totalOwnTime));
        }
        return activityNodes;
    }
}
