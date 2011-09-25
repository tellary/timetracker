import com.atlassian.jira_soapclient.SOAPSession
import com.atlassian.jira.rpc.soap.client.JiraSoapService
import com.atlassian.jira.rpc.soap.client.RemoteWorklog

run(new File('config.groovy'))

StretchModel model = TogglHelper.parseTasksFromTogglCSV(togglCSV)
model.timeInOffice = TimeHelp.sumUpTasksTime(model.tasks)
model.date = JIRAReportHelper.yesterday()
model.alreadyReported = alreadyReported
new StretchTimeForm(model).display()

tasks = reportIntoJIRA(model)

println '''
Remaining tasks:
===================='''

tasks.each {Task it ->
  println "${it.projectName}"
  println "${it.taskName} ${TimeHelp.timeMillisToString(it.timeSpent)} ${TimeHelp.timeMillisToMinutes(it.timeStretch)} min"
}

Collection<Task> reportIntoJIRA(StretchModel model) {
  SOAPSession soapSession = new SOAPSession(new URL(jiraUrl));
  soapSession.connect(jiraUsername, jiraPassword);
  println "Connected to JIRA with user $jiraUsername"

  JiraSoapService jiraSoapService = soapSession.getJiraSoapService();
  String authToken = soapSession.getAuthenticationToken();

  List<Task> notLogged = new LinkedList<Task>()

  for (Task task: model.tasks) {
    RemoteWorklog worklog = new RemoteWorklog();
    worklog.setComment(task.taskName);
    worklog.setTimeSpent("${(TimeHelp.timeMillisToMinutes(task.timeStretch))}m");
    worklog.setStartDate(model.date)

    String issueKey
    issueKey = jiraMapping.mapToJIRA(task, worklog)
    if (issueKey == null) {
      println "Skipping task with projectName '$task.projectName' as no issueKey found ..."
      notLogged.add(task)
      continue
    }

    println("""Going to add worklog for issue ${issueKey}
with actual time spent ${TimeHelp.timeMillisToMinutes(task.timeSpent)}m
with worklog time spent '${worklog.getTimeSpent()}'
on date '$worklog.startDate.time'
for project '$task.projectName'
and comment '${worklog.comment}'""")

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