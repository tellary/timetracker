package ru.silvestrov.timetracker.view;

import ru.silvestrov.timetracker.model.ActivityList;
import ru.silvestrov.timetracker.model.ActivityListUpdateListener;

import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.*;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 12, 2008
 * Time: 9:20:23 PM
 */
public class ActivityListTableModel extends AbstractTableModel {
    private ActivityList activityList;

    public class UpdateListener implements ActivityListUpdateListener {
        public void activityTimeUpdated(int i) {
            final Integer index = ++i;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    fireTableRowsUpdated(index, index);
                }
            });
        }

        public void invalidateList() {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    fireTableDataChanged();
                }
            });
        }
    }

    public class SelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                int i = lsm.getLeadSelectionIndex();
                if (i > 0)
                    activityList.makeActive(i - 1);
                else
                    activityList.stopTimer();
            }
        }
    }

    public ActivityListTableModel(ActivityList activityList) {
        this.activityList = activityList;
    }

    public int getColumnCount() {
        return 2;
    }


    public int getRowCount() {
        return activityList.size() + 1;
    }


    public Object getValueAt(int row, int column) {
        if (row == 0) {
            if (column == 0)
                return "(nothing)";
            else if (column == 1) {
                return "";
            }
        }
        if (column == 0)
            return activityList.getActivityInfo(row - 1).getName();
        else if (column == 1) {
            return activityList.getActivityInfo(row - 1).getTime();
        }
        throw new RuntimeException("Only 2 columns in the table");
    }
}
