import org.junit.Test
import org.junit.Assert
/**
 * Created by Silvestrov Ilya
 * Date: 9/12/11
 * Time: 1:15 AM
 */
class TimeHelpTest {
  @Test
  void testTimeStringToMillis() {
    String time = '00:00:01'
    long timeMillis = TimeHelp.timeStringToMillis(time)
    Assert.assertEquals(1000, timeMillis)
  }

  @Test
  void testTimeFromToString() {
    String time = '00:30:01'
    long timeMillis = TimeHelp.timeStringToMillis(time)
    String time1 = TimeHelp.timeMillisToString(timeMillis)
    Assert.assertEquals(time, time1)

    time = '01:30:27'
    timeMillis = TimeHelp.timeStringToMillis(time)
    time1 = TimeHelp.timeMillisToString(timeMillis)
    Assert.assertEquals(time, time1)
  }

  @Test
  void testTimeFromToString60Sec() {
    String time = '4h59m'
    long timeMillis = TimeHelp.timeStringToMillis(time)
    String time1 = TimeHelp.timeMillisToString(timeMillis)
    Assert.assertEquals('04:59:00', time1)

    time = '00:21:36'
    timeMillis = TimeHelp.timeStringToMillis(time)
    time1 = TimeHelp.timeMillisToString(timeMillis)
    Assert.assertEquals(time, time1)

    time = '01:09:59'
    timeMillis = TimeHelp.timeStringToMillis(time)
    time1 = TimeHelp.timeMillisToString(timeMillis)
    Assert.assertEquals(time, time1)
  }
}
