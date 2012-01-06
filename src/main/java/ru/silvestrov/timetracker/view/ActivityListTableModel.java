package ru.silvestrov.timetracker.view;

import ru.silvestrov.timetracker.model.ActivityControlList;
import ru.silvestrov.timetracker.model.ActivityControlListUpdateListener;

import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 12, 2008
 * Time: 9:20:23 PM
 */
public class ActivityListTableModel extends AbstractTableModel {
    private ActivityControlList activityControlList;
    private RenameActivityController renameActivityController;

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

    public ActivityListTableModel(ActivityControlList activityControlList,
                                  RenameActivityController renameActivityController) {
        this.activityControlList = activityControlList;
        this.renameActivityController = renameActivityController;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return rowIndex > 0 && columnIndex != 1;
    }

    public int getColumnCount() {
        return 3;
    }


    public int getRowCount() {
        return activityControlList.size() + 1;
    }

    private Map<Integer, JButton> hideButtons = new HashMap<Integer, JButton>();

    public Object getValueAt(final int row, int column) {
        if (row == 0) {
            if (column == 0)
                return "(nothing)";
            else {
                return "";
            }
        }
        if (column == 0)
            return activityControlList.getActivityInfo(row - 1).getName();
        else if (column == 1) {
            return formatTime(activityControlList.getActivityInfo(row - 1).getTime());
        } else if (column == 2) {
            return null;
        }

        throw new RuntimeException(String.format("There are only 3 columns in the table, but column %d is requested", column));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex != 2)
            return super.getColumnClass(columnIndex);
        else
            return JButton.class;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            renameActivityController.renameActivity(rowIndex - 1, (String) aValue);
    }

    public static String formatTime(long time) {
        //Copy-pasted from TimeHelp#timeMillisToString
        //TODO: use common codebase
        long hours = time / 3600000;
        long hoursRemainder = time - hours * 3600000;
        long minutes = hoursRemainder / 60000;
        long minutesRemainder = hoursRemainder - minutes * 60000;
        long seconds = minutesRemainder / 1000;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
