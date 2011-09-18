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

  public static Calendar yesterday() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 12)
    cal.add(Calendar.DAY_OF_YEAR, -1)
    return cal
  }

  public static Calendar parseStartDate(String startDate) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy")
    cal.setTime(sdf.parse(startDate))
    //12:00 is set in calendar to avoid time shifts.
    //We don't want to know JIRA server TZ to do it properly
    cal.set(Calendar.HOUR_OF_DAY, 12)
    return cal;
  }
}
