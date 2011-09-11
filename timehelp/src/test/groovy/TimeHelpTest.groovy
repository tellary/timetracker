import org.junit.Test
import org.junit.Assert
/**
 * Created by Silvestrov Ilya
 * Date: 9/12/11
 * Time: 1:15 AM
 */
class TimeHelpTest {
  @Test
  void testFloatHoursToString() {
    String hours = TimeHelp.floatHoursToString(1.5f)
    Assert.assertEquals('1:30:0', hours)
  }

  @Test
  void testFloatHoursToMinutes() {
    Assert.assertEquals(90, TimeHelp.floatHoursToMinutes(1.5f))
  }

  @Test
  void testTimeFromToString() {
    String time = '00:30:01'
    float hours = TimeHelp.timeToFloatHours(time)
    String time1 = TimeHelp.floatHoursToString(hours)
    Assert.assertEquals(time, time1)

    time = '01:30:27'
    hours = TimeHelp.timeToFloatHours(time)
    time1 = TimeHelp.floatHoursToString(hours)
    Assert.assertEquals(time, time1)
  }
}
