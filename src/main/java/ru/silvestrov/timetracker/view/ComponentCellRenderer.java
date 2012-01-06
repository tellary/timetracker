package ru.silvestrov.timetracker.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by Ilya Silvestrov
 * Date: 1/5/12
 * Time: 5:24 PM
 */
public class ComponentCellRenderer extends JButton implements TableCellRenderer{
    private TableCellRenderer defaultCellRenderer;

    public ComponentCellRenderer(TableCellRenderer defaultCellRenderer) {
        this.defaultCellRenderer = defaultCellRenderer;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Component)
            return (Component) value;
        else
            return defaultCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
