
/**
 * Created by Silvestrov Ilya
 * Date: 9/12/11
 * Time: 1:11 AM
 */
class TimeHelp {
  static float timeToFloatHours(String timeString) {
    String[] timeSplit = timeString.split(":");
    if (timeSplit.length == 2) {
      timeSplit = ["0", timeSplit[0], timeSplit[1]] as String[];
    }
    return Integer.parseInt(timeSplit[0]) + Integer.parseInt(timeSplit[1])/60f + Integer.parseInt(timeSplit[2])/3600f
  }
  static String floatHoursToString(float floatHours) {
    int hours = floatHours;
    float minutesPart = floatHours - hours
    int minutes = minutesPart * 60
    float floatSeconds = (minutesPart*60 - minutes) * 60
    int seconds = floatSeconds.round()
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
  }

  static int floatHoursToMinutes(float floatHours) {
    return (floatHours*60).round();
  }
}
