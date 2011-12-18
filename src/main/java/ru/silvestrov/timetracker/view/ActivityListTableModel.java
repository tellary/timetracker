package ru.silvestrov.timetracker.view;

import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.model.ActivityControlList;
import ru.silvestrov.timetracker.model.ActivityControlListUpdateListener;
import ru.silvestrov.timetracker.model.ActivityInfo;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
    private ActivityControlList activityControlList;

    public class UpdateListenerControl implements ActivityControlListUpdateListener {
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
                    activityControlList.makeActive(i - 1);
                else
                    activityControlList.stopTimer();
            }
        }
    }

    public ActivityListTableModel(ActivityControlList activityControlList) {
        this.activityControlList = activityControlList;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return rowIndex > 0;
    }

    public int getColumnCount() {
        return 2;
    }


    public int getRowCount() {
        return activityControlList.size() + 1;
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
            return activityControlList.getActivityInfo(row - 1).getName();
        else if (column == 1) {
            return activityControlList.getActivityInfo(row - 1).getTime();
        }
        throw new RuntimeException("Only 2 columns in the table");
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ActivityInfo activity = activityControlList.getActivityInfo(rowIndex - 1);
        System.out.println("Old activity name: " + activity.getName() + ", new name: " + aValue);
    }
}
