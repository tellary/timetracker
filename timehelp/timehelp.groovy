import com.atlassian.jira_soapclient.SOAPSession
import javax.swing.JFrame
import javax.swing.BoxLayout
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.Condition
import groovy.swing.SwingBuilder
import com.atlassian.jira.rpc.soap.client.JiraSoapService
import com.atlassian.jira.rpc.soap.client.RemoteWorklog

//--------------------
date = "21-Aug-2011"
projectKey='TST'
String timeInOffice = '12:34:00'
String togglCSV = "report.htm"
float alreadyReported = 0
//--------------------

List<Task> parseTasksFromTogglCSV(String filename) {
  tasks = [] as List<Task>
  new File(filename).eachLine {String line ->
    if (!line.startsWith("User,Client,Project") && !line.trim().isEmpty()) {
      List<String> splits = [] as List<String>
      String currentSplit
      line.split(',').each {String split ->
        if (split.startsWith('"')) {
          currentSplit = ''
        }

        if (currentSplit == null) {
          splits.add(split)
        } else
          currentSplit += split

        if (split.endsWith('"')) {
          splits.add(currentSplit)
          currentSplit = null
        }

      }

      Task task = new Task();
      task.projectName = splits[2]
      task.taskName = splits[3]
      task.timeSpent = splits[7]
      tasks.add(task)
    }
  }

  return tasks
}

List selectNoStretchTasks(List<Task> tasks) {
  def swing = new SwingBuilder()

  Lock lock = new ReentrantLock()
  lock.lock()
  Condition tasksSelected = lock.newCondition();
  JFrame frame =
  swing.frame (
      title: 'Select no-stretch tasks',
      defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
      show: true,
      pack: true,
  ) {
    scrollPane {
      panel {
        boxLayout(axis:BoxLayout.Y_AXIS)
        for (Task task: tasks) {
          panel(border: BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)) {
            borderLayout()
            label(text: task.projectName, constraints: BorderLayout.NORTH)
            panel(constraints: BorderLayout.SOUTH) {
              borderLayout()
              label(text: '   ', constraints: BorderLayout.WEST)
              label(text: "$task.taskName   $task.timeSpent", constraints: BorderLayout.CENTER)
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

  return null;
}

float sumUpTasksTime(Collection tasks) {
  float sum = 0
  tasks.each {
    sum += timeToFloat(it.timeSpent)
  }
  return sum
}

float timeToFloat(String timeString) {
  String[] timeSplit = timeString.split(":");
  if (timeSplit.length == 2) {
    timeSplit = ["0", timeSplit[0], timeSplit[1]] as String[];
  }
  return Integer.parseInt(timeSplit[0]) + Integer.parseInt(timeSplit[1])/60f + Integer.parseInt(timeSplit[2])/3600f
}

int timeSecs(String timeString) {
  float timeInHours = timeToFloat(timeString)
  return timeInHours * 3600;
}

Collection<Task> reportIntoJIRA(Collection<Task> tasks) {
  String baseUrl = "http://sandbox.onjira.com/rpc/soap/jirasoapservice-v2";
  SOAPSession soapSession = new SOAPSession(new URL(baseUrl));
  String username = "tellarytest";
  soapSession.connect(username, "tellarytest");
  println "Connected to JIRA with user $username"

  JiraSoapService jiraSoapService = soapSession.getJiraSoapService();
  String authToken = soapSession.getAuthenticationToken();

  List<Task> notLogged = new LinkedList<Task>()

  for (Task task: tasks) {
    RemoteWorklog worklog = new RemoteWorklog();
    worklog.setComment(task.taskName);
    worklog.setTimeSpent("${(timeToFloat(task.timeSpent)*60).round()}m");
    worklog.setStartDate(JIRAReportHelper.parseStartDate(date))

    String issueKey = JIRAReportHelper.parseJIRAIssueKey(projectKey, task.projectName)

    println("""Going to add worklog for issue ${issueKey}
with time spent '${worklog.getTimeSpent()}'
and comment '${worklog.comment}'""")

    char confirm = System.in.read()
    if (confirm != 'y') {
      println "Skipping..."
      notLogged.add(task)
      continue
    }

    try {
      jiraSoapService.addWorklogAndAutoAdjustRemainingEstimate(authToken, issueKey, worklog);
      println """Successfully added worklog for issue ${issueKey}
with time spent '${worklog.getTimeSpent()}'
and comment '${worklog.comment}'"""
    } catch (Exception e) {
      e.printStackTrace()
      notLogged.add(task)
    }
  }

  return notLogged
}

tasks = parseTasksFromTogglCSV(togglCSV)

selectNoStretchTasks(tasks)

tasks = reportIntoJIRA(tasks)

println '''
===================='''

float round

float timeInOfficeFloat = timeToFloat(timeInOffice)
println "Time in office float ${timeInOfficeFloat.round(2)}"

float timeEffectiveFloat = sumUpTasksTime(tasks)
println "Time effective float ${timeEffectiveFloat.round(2)}"

float noStretchTime = sumUpTasksTime(tasks.findAll {Task t-> t.noStretch})
float stretchTime = sumUpTasksTime(tasks.findAll {!it.noStretch})

println "No stretch tasks sum up time ${noStretchTime.round(2)}"
println "Stretch tasks sum up time ${stretchTime.round(2)}"
println "Overall efficency ${(timeEffectiveFloat/(timeInOfficeFloat - alreadyReported)).round(2)}"
println "Office - effective difference ${(timeInOfficeFloat - timeEffectiveFloat).round(2)}"
println "Office - alreadyReported ${(timeInOfficeFloat - alreadyReported).round(2)}"

float timeInOfficeLeft = timeInOfficeFloat - alreadyReported

leftEffeciency = (timeInOfficeLeft - noStretchTime)/stretchTime
println "Extension coef for timeLeft ${leftEffeciency.round(2)}"

println '''
===================='''
tasks.each {Task it ->
  println "${it.projectName}"
  float timeSpent =  timeToFloat(it.timeSpent)
  if (!it.noStretch) {
    timeSpent = timeSpent * leftEffeciency
  }
  timeSpent = timeSpent * 60
  println "${it.taskName} ${it.timeSpent} ${timeSpent.round()} min"
  println ""
}
