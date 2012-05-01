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
        LazyActivityTreeNode root = new LazyActivityTreeNode(1, "Root activity", 100);
        LazyActivityTreeNode first = new LazyActivityTreeNode(2, "First", 200);
        root.addChild(first);
        LazyActivityTreeNode second = new LazyActivityTreeNode(3, "Second", 300);
        root.addChild(second);
        LazyActivityTreeNode secondFirst = new LazyActivityTreeNode(4, "SecondFirst", 100);
        second.addChild(secondFirst);
        LazyActivityTreeNode secondSecond = new LazyActivityTreeNode(5, "SecondSecond", 200);
        second.addChild(secondSecond);

        Assert.assertFalse(root.isValid());
        Assert.assertEquals(100+200+300+100+200, root.getAggregateTimeSpent());
        Assert.assertTrue(root.isValid());

        secondSecond.invalidateActivityTreeNode();
        Assert.assertFalse(secondSecond.isValid());
        Assert.assertTrue(secondFirst.isValid());
        Assert.assertFalse(second.isValid());
        Assert.assertTrue(first.isValid());
        Assert.assertFalse(root.isValid());

        Assert.assertEquals(100+200+300+100+200, root.getAggregateTimeSpent());
        Assert.assertEquals(300+100+200, second.getAggregateTimeSpent());
    }
}
