package ru.silvestrov.timetracker.view;

import org.hibernate.sql.InsertSelect;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by Silvestrov Ilya
 * Date: 1/29/12
 * Time: 10:56 PM
 */
public class ResizeTableRowsListener extends ComponentAdapter {
    @Override
    public void componentResized(ComponentEvent e) {
        resizeTableRows((JTable) e.getComponent());
    }

    public static void resizeTableRows(JTable table) {
        for (int i = 0; i < table.getRowCount(); ++i) {
            TableCellRenderer renderer = table.getCellRenderer(i, 0);
            JTextArea textArea = (JTextArea) renderer.getTableCellRendererComponent(
                table, table.getValueAt(i, 0), false, false, i, 0);
            adjustRowHeight(table, textArea, i);
        }
    }

    public static void adjustRowHeight(JTable table, JTextArea textArea, int row) {
        int colWidth = table.getColumnModel().getColumn(0).getWidth();
        System.out.println("Adjusting row height for text: " + textArea.getText() + ",\ncolWidth : " + colWidth);
        Insets insets = textArea.getInsets();
        int sumMargin = insets.left + insets.right;
        sumMargin += table.getRowMargin() * 2;
        textArea.setSize(colWidth - sumMargin, 15);
        int height = (int) textArea.getPreferredSize().getHeight();

        System.out.println("Row height: " + height);

        table.setRowHeight(row, height);
    }
}
