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
jiraUrl = "http://sandbox.onjira.com/rpc/soap/jirasoapservice-v2";
jiraUsername = "tellarytest";
jiraPassword = "tellarytest";
projectKey='TST'
//--------------------
date = "06-Sep-2011"
timeInOffice = '04:26:00'
togglCSV = "report.htm"
alreadyReported = 0
//--------------------

List<Task> tasks = parseTasksFromTogglCSV(togglCSV)

selectNoStretchTasks(tasks)

stretchTasks(tasks)

tasks = reportIntoJIRA(tasks)

println '''
Remaining tasks:
===================='''

tasks.each {Task it ->
  println "${it.projectName}"
  println "${it.taskName} ${TimeHelp.floatHoursToString(it.timeSpent)} ${TimeHelp.floatHoursToMinutes(it.timeStretch)} min"
}


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
      task.timeSpent = TimeHelp.timeToFloatHours(splits[7])
      tasks.add(task)
    }
  }

  return tasks
}

def selectNoStretchTasks(List<Task> tasks) {
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
        for (Task task: tasks) {
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

float sumUpTasksTime(Collection<Task> tasks) {
  float sum = 0
  tasks.each {Task it ->
    sum += it.timeSpent
  }
  return sum
}

Collection<Task> reportIntoJIRA(Collection<Task> tasks) {
  SOAPSession soapSession = new SOAPSession(new URL(jiraUrl));
  soapSession.connect(jiraUsername, jiraPassword);
  println "Connected to JIRA with user $jiraUsername"

  JiraSoapService jiraSoapService = soapSession.getJiraSoapService();
  String authToken = soapSession.getAuthenticationToken();

  List<Task> notLogged = new LinkedList<Task>()

  for (Task task: tasks) {
    RemoteWorklog worklog = new RemoteWorklog();
    worklog.setComment(task.taskName);
    worklog.setTimeSpent("${(TimeHelp.floatHoursToMinutes(task.timeStretch))}m");
    worklog.setStartDate(JIRAReportHelper.parseStartDate(date))

    String issueKey
    try {
      issueKey = JIRAReportHelper.parseJIRAIssueKey(projectKey, task.projectName)
    } catch (Exception e) {
      println "Skipping as $e.message ..."
      notLogged.add(task)
      continue
    }

    println("""Going to add worklog for issue ${issueKey}
with actual time spent ${TimeHelp.floatHoursToMinutes(task.timeSpent)}m
with worklog time spent '${worklog.getTimeSpent()}'
on date '$worklog.startDate.time'
for project '$task.projectName'
and comment '${worklog.comment}'.
Please confirm:""")

    char confirm;
    //noinspection GroovyEmptyStatementBody
    while (Character.isWhitespace(confirm = System.in.read())) {}
    if (confirm != 'y') {
      println "Skipping..."
      notLogged.add(task)
      continue
    }

    try {
      jiraSoapService.addWorklogAndAutoAdjustRemainingEstimate(authToken, issueKey, worklog);
      println """-->Successfully added worklog for issue ${issueKey}
with time spent '${worklog.getTimeSpent()}'
and comment '${worklog.comment}'
"""
    } catch (Exception e) {
      e.printStackTrace()
      notLogged.add(task)
    }
  }

  return notLogged
}

def stretchTasks(List<Task> tasks) {
  println '''
===================='''

  float timeInOfficeFloat = TimeHelp.timeToFloatHours(timeInOffice)
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
    float timeSpent =  it.timeSpent
    if (!it.noStretch) {
      it.timeStretch = timeSpent * leftEffeciency
    } else {
      it.timeStretch = timeSpent
    }
  }
}

