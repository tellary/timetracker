import org.junit.Test
import org.junit.Assert
/**
 * Created by Silvestrov Ilya
 * Date: 9/25/11
 * Time: 5:38 PM
 */
class SumTasksTimeTest {
  @Test
  void test() {
    StretchModel model = TogglHelper.parseTasksFromTogglCSV(
        new InputStreamReader(getClass().getResourceAsStream("report1h30m.html")))
    long timeSpentMillis = TimeHelp.sumUpTasksTime(model.tasks)
    String timeSpent = TimeHelp.timeMillisToString(timeSpentMillis)
    Assert.assertEquals('01:30:00', timeSpent)
  }
}
