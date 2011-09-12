import com.atlassian.jira.rpc.soap.client.RemoteWorklog
/**
 * Created by Silvestrov Ilya
 * Date: 9/13/11
 * Time: 2:00 AM
 */
class MatchFirstJIRAMapping implements JIRAMapping {
  private JIRAMapping[] jiraMappings


  MatchFirstJIRAMapping(JIRAMapping... jiraMappings) {
    this.jiraMappings = jiraMappings
  }

  String mapToJIRA(Task task, RemoteWorklog worklog) {
    String issueKey
    for (JIRAMapping jiraMapping: jiraMappings) {
      issueKey = jiraMapping.mapToJIRA(task, worklog)
      if (issueKey != null)
        return issueKey
    }
    return null
  }
}
