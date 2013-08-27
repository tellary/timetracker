package ru.silvestrov.timetracker.view.activitytree;

import ru.silvestrov.timetracker.model.activitytree.*;

import javax.swing.*;

/**
 * Created by Silvestrov Ilya
 * Date: 7/22/12
 * Time: 9:32 PM
 */

/**
 * Test runner to test ActivityJTree
 */
public class ActivityTreeModelTestRunner {
    private static long mins(int mins) {
        return mins*60*1000;
    }
    public static void main(String[] args) {
        ParentActivityTree tree = new LazyActivityTree();
        ChildActivityTreeNode a1 = new LazyActivityTreeNode(1, "a1", mins(10));
        tree.addChild(a1);
        ChildActivityTreeNode priceCheck2 = new LazyActivityTreeNode(2, "Проверка цен", mins(90));
        tree.addChild(priceCheck2);
        ChildActivityTreeNode priceCheck20 = new LazyActivityTreeNode(20, "Проверка цен", mins(20));
        priceCheck2.addChild(priceCheck20);
        ChildActivityTreeNode reportCalc21 = new LazyActivityTreeNode(21, "Формирование отчета", mins(40));
        priceCheck2.addChild(reportCalc21);
        ChildActivityTreeNode reportSending22 = new LazyActivityTreeNode(22, "Посылка отчета", mins(30));
        priceCheck2.addChild(reportSending22);
        tree.addChild(new LazyActivityTreeNode(3, "a3", mins(33)));

        final JFrame frame = new JFrame();
        ActivityJTree jtree = new ActivityJTree(
                tree,
                new ActivityTreeNodeMover() {
                    @Override
                    public void move(ActivityTree newParent, ActivityTreeNode child) {
                        ParentActivityTree parent = (ParentActivityTree) newParent;
                        parent.addChild((ChildActivityTreeNode) child);
                    }
                });
        frame.add(jtree);
        jtree.setVisible(true);
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });

    }
}
