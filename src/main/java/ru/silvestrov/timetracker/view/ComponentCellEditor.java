package ru.silvestrov.timetracker.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created by Ilya Silvestrov
 * Date: 1/6/12
 * Time: 1:06 AM
 */
public class ComponentCellEditor extends AbstractCellEditor implements TableCellEditor {
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return (Component) value;
    }

    public Object getCellEditorValue() {
        return null;
    }
}
