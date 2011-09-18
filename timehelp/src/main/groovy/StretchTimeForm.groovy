import groovy.swing.SwingBuilder
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.Condition
import javax.swing.JFrame
import javax.swing.BoxLayout
import javax.swing.BorderFactory
import java.awt.Color
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.JTextField
import javax.swing.JCheckBox

/**
 * Created by Silvestrov Ilya
 * Date: 9/14/11
 * Time: 12:39 AM
 */
class StretchTimeForm {
  private StretchModel stretchModel


  StretchTimeForm(StretchModel stretchModel) {
    this.stretchModel = stretchModel
  }

  def display() {
    def swing = new SwingBuilder()

    Lock lock = new ReentrantLock()
    lock.lock()
    Condition tasksSelected = lock.newCondition();
    JFrame frame =
    swing.frame (
        title: 'Select no-stretch tasks',
        defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
        show: true,
        pack: true,
    ) {
      scrollPane {
        panel {
          boxLayout(axis:BoxLayout.Y_AXIS)
          panel {
            borderLayout()
            label(text: 'Report date:', constraints: BorderLayout.WEST)
            textField (
                constraints: BorderLayout.EAST,
                columns: 6,
                actionPerformed: {ActionEvent event ->
                  JTextField field = (JTextField)event.getSource()
                  String dateText = field.getText()
                  stretchModel.date = JIRAReportHelper.parseStartDate(dateText)
                  println "Date successfully set to $stretchModel.date.time"
                }
            )
          }
          panel {
            borderLayout()
            label(text: 'Time in office:', constraints: BorderLayout.WEST)
            JTextField timeInOfficeFld = textField(
                columns: 6,
                actionPerformed: {ActionEvent event ->
                  JTextField field = (JTextField)event.getSource()
                  stretchModel.timeInOffice = TimeHelp.timeToFloatHours(field.getText())
                  field.text = TimeHelp.floatHoursToString(stretchModel.timeInOffice)
                  println "Time in office successfully set to $stretchModel.timeInOffice"
                },
                constraints: BorderLayout.EAST
            )
            timeInOfficeFld.text = TimeHelp.floatHoursToString(stretchModel.timeInOffice)
          }
          for (Task task: stretchModel.tasks) {
            panel(border: BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)) {
              borderLayout()
              label(text: task.projectName, constraints: BorderLayout.NORTH)
              panel(constraints: BorderLayout.SOUTH) {
                borderLayout()
                hbox (constraints: BorderLayout.LINE_START) {
                  label(text: '   ', constraints: BorderLayout.WEST)
                  label(text: "$task.taskName   ${TimeHelp.floatHoursToString(task.timeSpent)}",
                      constraints: BorderLayout.CENTER)
                }
                hbox (constraints: BorderLayout.LINE_END) {
                  JCheckBox checkBox = checkBox(
                      constraints: BorderLayout.LINE_START,
                      actionPerformed: {Task taskArg, event->
                        println taskArg.taskName
                        taskArg.noStretch = event.source.selected;
                      }.curry(task)
                  )
                  JTextField timeStretchFld = textField(
                      constraints: BorderLayout.CENTER,
                      columns: 6,
                      actionPerformed: {Task taskArg, ActionEvent event ->
                        JTextField field = (JTextField)event.getSource()
                        String timeStr = field.getText()
                        taskArg.timeStretch = TimeHelp.timeToFloatHours(timeStr)
                        println "TimeStrech set to $taskArg.timeStretch"
                      }.curry(task)
                  )
                  timeStretchFld.setText(TimeHelp.floatHoursToString(task.timeStretch))
                  task.taskModificationListeners.add(new TaskModificationListener() {
                    void taskModified(Task taskEvt) {
                      timeStretchFld.setText(TimeHelp.floatHoursToString(taskEvt.timeStretch))
                      checkBox.setSelected(taskEvt.noStretch)
                    }
                  })
                }
              }
            }
          }
          panel {
            borderLayout()
            button(
                text: 'Ok',
                constraints: BorderLayout.WEST,
                actionPerformed: {
                  lock.lock()
                  tasksSelected.signal()
                  lock.unlock()
                }
            )
          }
        }
      }
    }
    tasksSelected.await()
    lock.unlock()
    frame.dispose()
  }
}
