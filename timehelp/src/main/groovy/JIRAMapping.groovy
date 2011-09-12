import com.atlassian.jira.rpc.soap.client.RemoteWorklog
/**
 * Created by Silvestrov Ilya
 * Date: 9/13/11
 * Time: 1:37 AM
 */
public interface JIRAMapping {
  String mapToJIRA(Task task, RemoteWorklog worklog);
}