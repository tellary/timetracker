import java.util.regex.Matcher
import java.text.SimpleDateFormat
/**
 * Created by Silvestrov Ilya
 * Date: 8/21/11
 * Time: 4:44 PM
 */
class JIRAReportHelper {
  public static String parseJIRAIssueKey(String projectKey, String projectName) {
    Matcher m = projectName =~ "$projectKey ?(\\d+)"
    if (m.find()) {
      return "$projectKey-${m.group(1)}"
    }
    m = projectName =~ "$projectKey-\\d+"
    if (m.find()) {
      return m.group()
    }
    throw new RuntimeException("Unable to parse issue key by project key '$projectKey' and project name '$projectName'")
  }

  public static Calendar parseStartDate(String startDate) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy")
    cal.setTime(sdf.parse(startDate))
    return cal;
  }
}
