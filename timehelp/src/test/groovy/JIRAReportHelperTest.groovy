import org.junit.Test
import org.junit.Assert
/**
 * Created by Silvestrov Ilya
 * Date: 8/21/11
 * Time: 4:47 PM
 */
class JIRAReportHelperTest {
  @Test
  public void testParseIssueKey() {
    String projectName = "PS 5.0, RDS 10196: Create auto-test for RDS 10159 issue"
    String projectKey = "RDS"
    Assert.assertEquals('RDS-10196', JIRAReportHelper.parseJIRAIssueKey(projectKey, projectName))

    projectName = "DF Support, RDS-10229: SL3 support routines 3 Aug - 10 Aug"
    Assert.assertEquals('RDS-10229', JIRAReportHelper.parseJIRAIssueKey(projectKey, projectName))
  }

  @Test
  public void testParseStartDate() {
    Calendar cal = JIRAReportHelper.parseStartDate("26-Aug-2011")
    println cal
  }
}
