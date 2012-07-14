package ru.silvestrov.timetracker.model.activitytree;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Silvestrov Ilya
 * Date: 5/2/12
 * Time: 1:39 AM
 */
public class LazyActivityTreeNodeTest {
    @Test
    public void test() {
        LazyActivityTree root = new LazyActivityTree();
        LazyActivityTreeNode first = new LazyActivityTreeNode(2, "First", 200);
        root.addChild(first);
        LazyActivityTreeNode second = new LazyActivityTreeNode(3, "Second", 300);
        root.addChild(second);
        LazyActivityTreeNode secondFirst = new LazyActivityTreeNode(4, "SecondFirst", 100);
        second.addChild(secondFirst);
        LazyActivityTreeNode secondSecond = new LazyActivityTreeNode(5, "SecondSecond", 200);
        second.addChild(secondSecond);

        Assert.assertEquals(200 + 300 + 100 + 200, root.getAggregateTimeSpent());

        secondSecond.invalidateAggregateTimeSpent();

        Assert.assertEquals(200+300+100+200, root.getAggregateTimeSpent());
        Assert.assertEquals(300+100+200, second.getAggregateTimeSpent());

        secondSecond.addChild(new LazyActivityTreeNode(6, "SecondSecondFirst", 400));
        secondSecond.invalidateAggregateTimeSpent();
        Assert.assertEquals(200+300+100+200 + 400, root.getAggregateTimeSpent());
    }
}
