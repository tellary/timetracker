package ru.silvestrov.timetracker.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by Silvestrov Ilya
 * Date: 1/29/12
 * Time: 1:00 PM
 */
public class TextWrapCellRenderer extends JTextArea implements TableCellRenderer {
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        String text = (String) value;
        this.setWrapStyleWord(true);
        this.setLineWrap(true);
        this.setText(text);

        if (isSelected) {
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            super.setForeground(table.getForeground());
            super.setBackground(table.getBackground());
        }

        return this;
    }
}
