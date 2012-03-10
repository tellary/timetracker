package ru.silvestrov.timetracker.view;

import ru.silvestrov.timetracker.model.activitycontrollist.ActivityControlList;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 13, 2008
 * Time: 6:02:24 PM
 */
public class AddActivityController implements ActionListener {
    private JTextField name;
    private ActivityControlList activityControlList;
    private Window form;

    public void setName(JTextField name) {
        this.name = name;
    }

    public void setForm(Window form) {
        this.form = form;
    }

    public void setActivityControlList(ActivityControlList activityControlList) {
        this.activityControlList = activityControlList;
    }

    public void actionPerformed(ActionEvent e) {
        activityControlList.addActivity(name.getText());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                form.dispose();
            }
        });
    }
}
