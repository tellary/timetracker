package ru.silvestrov.timetracker.view.activitytree;

import ru.silvestrov.timetracker.model.activitytree.HashActivityTree;
import ru.silvestrov.timetracker.model.activitytree.LazyActivityTree;
import ru.silvestrov.timetracker.model.activitytree.LazyActivityTreeNode;

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
        HashActivityTree tree = new HashActivityTree(new LazyActivityTree());
        tree.addChild(new LazyActivityTreeNode(1, "a1", mins(10)), null);
        tree.addChild(new LazyActivityTreeNode(2, "Проверка цен", mins(90)), null);
        tree.addChild(new LazyActivityTreeNode(20, "Проверка цен", mins(20)), 2L);
        tree.addChild(new LazyActivityTreeNode(21, "Формирование отчета", mins(40)), 2L);
        tree.addChild(new LazyActivityTreeNode(22, "Посылка отчета", mins(30)), 2L);
        tree.addChild(new LazyActivityTreeNode(3, "a3", mins(33)), null);

        final JFrame frame = new JFrame();
        ActivityJTree jtree = new ActivityJTree(tree, tree);
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
