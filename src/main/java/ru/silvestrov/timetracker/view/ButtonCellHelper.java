package ru.silvestrov.timetracker.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Ilya Silvestrov
 * Date: 1/6/12
 * Time: 1:06 AM
 */
public class ButtonCellHelper extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
    private JButton button;
    private CellButtonListener cellButtonListener;
    private Listener actionListener = new Listener();
    private DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();

    public static interface CellButtonListener {
        void cellButtonPressed(int row);
    }

    private class Listener implements ActionListener {
        private int rowNum;

        public void setRowNum(int rowNum) {
            this.rowNum = rowNum;
        }

        public void actionPerformed(ActionEvent e) {
            cellButtonListener.cellButtonPressed(rowNum);

            stopCellEditing();
        }
    }

    public ButtonCellHelper(String label, CellButtonListener cellButtonListener) {
        button = new JButton(label);
        this.cellButtonListener = cellButtonListener;
        button.addActionListener(actionListener);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        actionListener.setRowNum(row);
        return button;
    }

    public Object getCellEditorValue() {
        return null;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (row == 0)
            return defaultTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        return button;
    }
}
