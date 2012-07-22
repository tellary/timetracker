package ru.silvestrov.timetracker.view.activitytree;

import ru.silvestrov.timetracker.model.activitytree.HashActivityTree;
import ru.silvestrov.timetracker.model.activitytree.LazyActivityTreeNode;

import javax.swing.*;

/**
 * Created by Silvestrov Ilya
 * Date: 7/22/12
 * Time: 9:32 PM
 */
public class ActivityTreeModelTestRunner {
    public static void main(String[] args) {
        HashActivityTree tree = new HashActivityTree();
        tree.addChild(new LazyActivityTreeNode(1, "a1", 1), null);
        tree.addChild(new LazyActivityTreeNode(2, "Проверка цен", 90), null);
        tree.addChild(new LazyActivityTreeNode(20, "Проверка цен", 20), 2L);
        tree.addChild(new LazyActivityTreeNode(21, "Формирование отчета", 40), 2L);
        tree.addChild(new LazyActivityTreeNode(22, "Посылка отчета", 30), 2L);
        tree.addChild(new LazyActivityTreeNode(3, "a3", 3), null);

        final JFrame frame = new JFrame();
        ActivityJTree jtree = new ActivityJTree(tree);
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
