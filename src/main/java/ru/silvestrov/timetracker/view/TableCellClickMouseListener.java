package ru.silvestrov.timetracker.view;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Ilya Silvestrov
 * Date: 1/5/12
 * Time: 5:45 PM
 */
public class TableCellClickMouseListener implements MouseListener {
    public void mouseClicked(MouseEvent e) {
        forwardMouseEvent(e);
    }

    public void mousePressed(MouseEvent e) {
        forwardMouseEvent(e);
    }

    public void mouseReleased(MouseEvent e) {
        forwardMouseEvent(e);
    }

    public void mouseEntered(MouseEvent e) {
        forwardMouseEvent(e);
    }

    public void mouseExited(MouseEvent e) {
        forwardMouseEvent(e);
    }

    private void forwardMouseEvent(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        TableColumnModel columnModel = table.getColumnModel();
        int column = columnModel.getColumnIndexAtX(e.getX());
        int row = e.getY() / table.getRowHeight();

        if (column < 0 || row < 0 ||
                column >= table.getColumnCount() ||
                row >= table.getRowCount())
            return;

        Object value = table.getValueAt(row, column);
        if (value instanceof Component) {
            Component c = (Component) value;
            MouseEvent targetEvent = SwingUtilities.convertMouseEvent(table, e, c);
            c.dispatchEvent(targetEvent);
        }
    }
}
