package ru.silvestrov.timetracker.view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Created by Silvestrov Ilya
 * Date: 1/29/12
 * Time: 11:19 PM
 */
public class TextWrapCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JTextArea textArea = new JTextArea();

    private Listener listener = new Listener();

    private boolean skipResize = false;

    private class Listener implements DocumentListener {
        private JTable table;

        private int row;

        public void setTable(JTable table) {
            this.table = table;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public void insertUpdate(DocumentEvent e) {
            resize();
        }

        public void removeUpdate(DocumentEvent e) {
            resize();
        }

        public void changedUpdate(DocumentEvent e) {
            resize();
        }

        public void resize() {
            //We have to skip any resize until getTableCellEditorComponent call isn't completed.
            //Thus change events triggered by setText during the call should be skipped.
            if (!skipResize) {
                //This is necessary to set value for the table as resize listener will use
                //them to properly size rows. If any resize event happen our adjusted row height
                //will be lost without call to setValueAt.
                //This is subject to change under T1.
                table.setValueAt(textArea.getText(), row, 0);
                ResizeTableRowsListener.adjustRowHeight(table, textArea, row);
            }
        }
    }

    public TextWrapCellEditor() {
        textArea.getDocument().addDocumentListener(listener);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public Object getCellEditorValue() {
        return textArea.getText();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String text = (String) value;
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        try {
            skipResize = true;
            textArea.setText(text);
        } finally {
            skipResize = false;
        }

        listener.setRow(row);
        listener.setTable(table);

        return textArea;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return e instanceof MouseEvent && ((MouseEvent) e).getClickCount() >= 2;
    }

    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }
}
