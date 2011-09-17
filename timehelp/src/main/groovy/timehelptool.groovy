import com.atlassian.jira_soapclient.SOAPSession
import com.atlassian.jira.rpc.soap.client.JiraSoapService
import com.atlassian.jira.rpc.soap.client.RemoteWorklog

run(new File('config.groovy'))

List<Task> tasks = parseTasksFromTogglCSV(togglCSV)

StretchModel model = new StretchModel()
model.tasks = tasks
new StretchTimeForm(model).selectNoStretchTasks()

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
        //Begin of multi-split sentense
        if (currentSplit == null && split.startsWith('"')) {
          split = split.substring(1)
          currentSplit = split
          return
        }

        //End of multi-split sentense
        if (currentSplit != null && split.endsWith('"')) {
          split = split.substring(0, split.length() - 1)
          currentSplit += ','
          currentSplit += split
          splits.add(currentSplit)
          currentSplit = null
          return
        }
        //Middle of multi-split sentence
        if (currentSplit != null) {
          currentSplit += ','
          currentSplit += split
        }
        //Single split sentense
        else {
          splits.add(split)
        }
      }

      Task task = new Task();
      task.projectName = splits[2]
      task.taskName = splits[3]
      task.timeSpent = TimeHelp.timeToFloatHours(splits[7])
      task.timeStretch = task.timeSpent
      tasks.add(task)
    }
  }

  return tasks
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
    issueKey = jiraMapping.mapToJIRA(task, worklog)
    if (issueKey == null) {
      println "Skipping task with projectName '$task.projectName' as no issueKey found ..."
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

