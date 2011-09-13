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

import java.awt.event.ActionListener
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

  def selectNoStretchTasks() {
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
                constraints: BorderLayout.CENTER,
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
            textField(
                actionPerformed: {ActionEvent event ->
                  JTextField field = (JTextField)event.getSource()
                  stretchModel.timeInOffice = TimeHelp.timeToFloatHours(field.getText())
                  println "Time in office successfully set to $stretchModel.timeInOffice"
                },
                constraints: BorderLayout.CENTER
            )
          }
          for (Task task: stretchModel.tasks) {
            panel(border: BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)) {
              borderLayout()
              label(text: task.projectName, constraints: BorderLayout.NORTH)
              panel(constraints: BorderLayout.SOUTH) {
                borderLayout()
                label(text: '   ', constraints: BorderLayout.WEST)
                label(text: "$task.taskName   ${TimeHelp.floatHoursToString(task.timeSpent)}", constraints: BorderLayout.CENTER)
                checkBox(
                    constraints: BorderLayout.EAST,
                    actionPerformed: {Task taskArg, event->
                      println taskArg.taskName
                      taskArg.noStretch = event.source.selected;
                    }.curry(task)
                )
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
