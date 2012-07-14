package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 3/10/12
 * Time: 10:18 PM
 */


public class LazyActivityTreeNode extends LazyActivityTree implements ActivityTreeNode {
    private long id;
    private String name;
    private long timeSpent;

    private long aggregateTimeSpent;


    public LazyActivityTreeNode(long id, String name, long timeSpent) {
        this.id = id;
        this.name = name;
        this.timeSpent = timeSpent;
    }

    public void aggregationComplete() {
        aggregateTimeSpent = timeSpent + super.getAggregateTimeSpent();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Iterable<ActivityTreeNode> getChildren() {
        return super.getChildren();
    }

    public long getTimeSpent() {
        return this.timeSpent;
    }

    public long getAggregateTimeSpent() {
        super.getAggregateTimeSpent();
        return aggregateTimeSpent;
    }
}
