import java.util.regex.Matcher
import java.text.SimpleDateFormat
/**
 * Created by Silvestrov Ilya
 * Date: 9/12/11
 * Time: 1:11 AM
 */
class TimeHelp {
  static long timeStringToMillis(String timeString) {
    String[] timeSplit
    if (timeString.matches('.*(h|m|s).*')) {
      timeSplit = ["0", "0", "0"] as String[]
      Matcher m
      m = timeString =~ '(\\d+)\\s*h'
      if (m.find())
        timeSplit[0] = m.group(1)
      m = timeString =~ '(\\d+)\\s*m'
      if (m.find())
        timeSplit[1] = m.group(1)
      m = timeString =~ '(\\d+)\\s*s'
      if (m.find())
        timeSplit[2] = m.group(1)
      m = timeString =~ '(\\d+)\\s*$'
      if (m.find())
        timeSplit[1] = m.group(1)
    } else {
      timeSplit = timeString.split(":");
      if (timeSplit.length == 2) {
        timeSplit = ["0", timeSplit[0], timeSplit[1]] as String[];
      } else if (timeSplit.length == 1) {
        timeSplit = ["0", timeSplit[0], "0"] as String[];
      }
    }
    int hours = Integer.parseInt(timeSplit[0])
    int minutes = Integer.parseInt(timeSplit[1])
    int seconds = Integer.parseInt(timeSplit[2])
    return hours * 3600000 + minutes*60000 + seconds * 1000
  }
  static String timeMillisToString(long timeMillis) {
    long hours = timeMillis/3600000
    long hoursRemainder = timeMillis - hours*3600000
    long minutes = hoursRemainder/60000
    long minutesRemainder = hoursRemainder - minutes*60000
    long seconds = minutesRemainder/1000
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
  }

  public static String formatDate(Calendar date) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy")
    return sdf.format(date.time)
  }

  static int timeMillisToMinutes(long timeMillis) {
    return ((float)timeMillis/60000.0).round()
  }

  static float timeMillisToHours(long timeMillis) {
    return ((float)timeMillis/3600000.0).round(2)
  }

  static long sumUpTasksTime(Collection<Task> tasks) {
    long sum = 0
    tasks.each {Task it ->
      sum += it.timeSpent
    }
    return sum
  }

  static float sumUpStretchTasksTime(Collection<Task> tasks) {
    long sum = 0
    tasks.each {Task it ->
      sum += it.timeStretch
    }
    return sum
  }

  static def stretchTasks(StretchModel stretchModel) {
    println '''
===================='''
    long timeInOfficeMillis = stretchModel.timeInOffice
    println "Time in office hours ${TimeHelp.timeMillisToHours(timeInOfficeMillis)}"

    long timeEffectiveMillis = sumUpTasksTime(stretchModel.tasks)
    println "Time effective hours ${TimeHelp.timeMillisToHours(timeEffectiveMillis)}"

    long alreadyReported = stretchModel.alreadyReported

    long noStretchTime = sumUpStretchTasksTime(stretchModel.tasks.findAll {Task t-> t.noStretch})
    long stretchTime = sumUpTasksTime(stretchModel.tasks.findAll {!it.noStretch})

    println "No stretch tasks sum up time hours ${TimeHelp.timeMillisToHours(noStretchTime)}"
    println "Stretch tasks sum up time hours ${TimeHelp.timeMillisToHours(stretchTime)}"
    println "Overall efficency ${((float)timeEffectiveMillis/(timeInOfficeMillis - alreadyReported)).round(2)}"
    println "Office - effective difference hours ${TimeHelp.timeMillisToHours(timeInOfficeMillis - timeEffectiveMillis)}"
    println "Office - alreadyReported hours ${TimeHelp.timeMillisToHours(timeInOfficeMillis - alreadyReported)}"

    long timeInOfficeLeftMillis = timeInOfficeMillis - alreadyReported

    float leftEfficiency = (timeInOfficeLeftMillis - noStretchTime)/stretchTime
    println "Extension coef for timeLeft ${leftEfficiency.round(2)}"

    println '''
===================='''

    stretchModel.tasks.each {Task it -> it.doStretch((float)leftEfficiency)}
  }
}
