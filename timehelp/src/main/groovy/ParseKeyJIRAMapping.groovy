import com.atlassian.jira.rpc.soap.client.RemoteWorklog
/**
 * Created by Silvestrov Ilya
 * Date: 9/13/11
 * Time: 1:39 AM
 */
class ParseKeyJIRAMapping implements JIRAMapping {
  private String projectKey


  ParseKeyJIRAMapping(String projectKey) {
    this.projectKey = projectKey
  }

  String mapToJIRA(Task task, RemoteWorklog worklog) {
    try {
      return JIRAReportHelper.parseJIRAIssueKey(projectKey, task.projectName)
    } catch (Exception e) {
      return null;
    }
  }
}
