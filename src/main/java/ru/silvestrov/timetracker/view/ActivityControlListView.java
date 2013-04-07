package ru.silvestrov.timetracker.view;

import org.apache.log4j.Logger;
import ru.silvestrov.timetracker.model.activitycontrollist.ActivityControlList;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 9, 2008
 * Time: 10:58:13 PM
 */
public class ActivityControlListView {
    private static final Logger logger = Logger.getLogger(ActivityControlListView.class);

    @Resource
    private ActivityControlList activityControlList;
    @Resource
    private RenameActivityController renameActivityController;
    @Resource
    private AdjustTimeController adjustTimeController;

    public void show() {
        final JFrame mainFrame = new JFrame();

        ActivityListTableModel tableModel = new ActivityListTableModel(
            activityControlList, renameActivityController, adjustTimeController);
        activityControlList.setUpdateListener(tableModel.new UpdateListenerControl());
        activityControlList.startTimer();

        final JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(tableModel.new SelectionListener());
        tableModel.addTableModelListener(table);
        JTextField timeField = new JTextField("-3");
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(timeField) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return editorComponent;
            }
        });
        FocusListener resetTimerFocusListener = new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Focus gained!");
                }
                table.changeSelection(0, 0, true, true);
            }

            public void focusLost(FocusEvent e) {
            }
        };
        timeField.addFocusListener(resetTimerFocusListener);

        TextWrapCellRenderer textWrapCellRenderer = new TextWrapCellRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(textWrapCellRenderer);
        table.addComponentListener(new ResizeTableRowsListener());

        TextWrapCellEditor textWrapCellEditor = new TextWrapCellEditor();
        table.getColumnModel().getColumn(0).setCellEditor(textWrapCellEditor);
        textWrapCellEditor.getTextArea().addFocusListener(resetTimerFocusListener);

        Icon finishIcon = new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.drawString("x", x + 1, y + getIconHeight());
            }

            public int getIconWidth() {
                return 10;
            }

            public int getIconHeight() {
                return 10;
            }
        };

        ButtonCellHelper buttonCellHelper = new ButtonCellHelper(finishIcon, new ButtonCellHelper.CellButtonListener() {
            public void cellButtonPressed(int row) {
                activityControlList.finishActivity(row - 1);
            }
        });
        table.getColumnModel().getColumn(2).setCellRenderer(buttonCellHelper);
        table.getColumnModel().getColumn(2).setCellEditor(buttonCellHelper);


        Component timeRendererComponent = table.getDefaultRenderer(String.class).getTableCellRendererComponent(table, "00:00:00", true, true, 2, 1);
        table.getColumnModel().getColumn(1).setMaxWidth((int) timeRendererComponent.getPreferredSize().getWidth() + 10);
        table.getColumnModel().getColumn(2).setMaxWidth(finishIcon.getIconWidth());


        final JButton addActivityButton = new JButton("Add activity");
        addActivityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JFrame frame = new JFrame("Add activity");
                        JPanel panel = new JPanel(new GridLayout());
                        JTextField text = new JTextField();
                        panel.add(new JLabel("Name"));
                        panel.add(text);

                        JButton addActivityButton = new JButton("Add activity");
                        AddActivityController addActivityController = new AddActivityController();
                        addActivityController.setActivityControlList(activityControlList);
                        addActivityController.setName(text);
                        addActivityController.setForm(frame);
                        addActivityButton.addActionListener(addActivityController);
                        panel.add(addActivityButton);
                        frame.add(panel);
                        frame.setVisible(true);
                    }
                });
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.add(topPanel, BorderLayout.NORTH);
        topPanel.add(addActivityButton);
        JPanel centerPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        centerPanel.add(table);
        panel.add(new JScrollPane(centerPanel),BorderLayout.CENTER);

        mainFrame.add(panel);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final WindowSettingsSaver settingsSaver = new WindowSettingsSaver();

        Toolkit.getDefaultToolkit().addAWTEventListener(
            new AWTEventListener() {
                public void eventDispatched(AWTEvent event) {
                    WindowEvent we = (WindowEvent) event;
                    switch (event.getID()) {
                        case WindowEvent.WINDOW_OPENED:
                            settingsSaver.windowOpened(we);
                            break;
                        case WindowEvent.WINDOW_CLOSING:
                            settingsSaver.windowClosing(we);
                    }
                }
            }, AWTEvent.WINDOW_EVENT_MASK
        );

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainFrame.setVisible(true);
                addActivityButton.setVisible(true);
            }
        });

    }
}
