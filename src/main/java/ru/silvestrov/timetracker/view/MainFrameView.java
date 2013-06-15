package ru.silvestrov.timetracker.view;

import ru.silvestrov.timetracker.model.activitytree.ActivityTreeManager;
import ru.silvestrov.timetracker.model.activitytree.MovableActivityTree;
import ru.silvestrov.timetracker.view.activitytree.ActivityJTree;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Silvestrov Ilya
 * Date: 7/23/12
 * Time: 12:20 AM
 */
public class MainFrameView {
    @Resource
    private ActivityControlListView activityControlListView;

    @Resource
    private ActivityTreeManager activityTreeManager;

    public void show() {
        JButton showActivityControlTree = new JButton("Show activity control list");
        JButton showAllActivitiesTree = new JButton("Show all activities tree");

        final JFrame mainFrame = new JFrame("Time tracker main frame");
        mainFrame.setLayout(new BorderLayout());

        mainFrame.add(showActivityControlTree, BorderLayout.NORTH);
        mainFrame.add(showAllActivitiesTree, BorderLayout.SOUTH);

        showActivityControlTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activityControlListView.show();
            }
        });
        showAllActivitiesTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MovableActivityTree tree = activityTreeManager.loadAllActivitiesTree();
                final JFrame frame = new JFrame();
                ActivityJTree jtree = new ActivityJTree(tree, tree);
                frame.add(jtree);
                jtree.setVisible(true);
                frame.pack();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        frame.setVisible(true);
                    }
                });
            }
        });

        mainFrame.pack();

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainFrame.setVisible(true);
            }
        });
    }
}
