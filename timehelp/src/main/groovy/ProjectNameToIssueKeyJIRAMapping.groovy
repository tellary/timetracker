import com.atlassian.jira.rpc.soap.client.RemoteWorklog
/**
 * Created by Silvestrov Ilya
 * Date: 9/13/11
 * Time: 1:51 AM
 */
class ProjectNameToIssueKeyJIRAMapping implements JIRAMapping {
  private String projectName
  private String issueKey
  private boolean addProjectToComment = false

  ProjectNameToIssueKeyJIRAMapping(String projectName, String issueKey) {
    this.projectName = projectName
    this.issueKey = issueKey
  }

  ProjectNameToIssueKeyJIRAMapping addProjectToComment(boolean addProjectToComment) {
    this.addProjectToComment = addProjectToComment
    return this
  }

  String mapToJIRA(Task task, RemoteWorklog worklog) {
    if (task.projectName.equals(projectName)) {
      if (addProjectToComment) {
        worklog.setComment("$projectName, $task.taskName")
      }
      return issueKey
    } else
      return null
  }
}
