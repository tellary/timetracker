package ru.silvestrov.timetracker.model.activitytree;

/**
 * Created by Silvestrov Ilya
 * Date: 3/10/12
 * Time: 10:18 PM
 */


public class LazyActivityTreeNode extends LazyActivityTree implements ChildActivityTreeNode {
    private long id;
    private String name;
    private long timeSpent;
    private ParentActivityTree parentActivityTree;

    public LazyActivityTreeNode(long id, String name, long timeSpent) {
        this.id = id;
        this.name = name;
        this.timeSpent = timeSpent;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getTimeSpent() {
        return this.timeSpent;
    }

    @Override
    public void invalidateTree() {
        super.invalidateTree();
        parentActivityTree.invalidateTree();
    }

    @Override
    public void setParent(ParentActivityTree parentActivityTree) {
        if (this.parentActivityTree != null)
            this.parentActivityTree.removeChild(this);
        this.parentActivityTree = parentActivityTree;
    }
}
