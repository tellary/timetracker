import java.util.regex.Matcher
/**
 * Created by Silvestrov Ilya
 * Date: 9/12/11
 * Time: 1:11 AM
 */
class TimeHelp {
  static float timeToFloatHours(String timeString) {
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

  static float sumUpTasksTime(Collection<Task> tasks) {
    float sum = 0
    tasks.each {Task it ->
      sum += it.timeSpent
    }
    return sum
  }

  static float sumUpStretchTasksTime(Collection<Task> tasks) {
    float sum = 0
    tasks.each {Task it ->
      sum += it.timeStretch
    }
    return sum
  }

  static def stretchTasks(StretchModel stretchModel) {
    println '''
===================='''
    float timeInOfficeFloat = stretchModel.timeInOffice
    println "Time in office float ${timeInOfficeFloat.round(2)}"

    float timeEffectiveFloat = sumUpTasksTime(stretchModel.tasks)
    println "Time effective float ${timeEffectiveFloat.round(2)}"

    float alreadyReported = stretchModel.alreadyReported

    float noStretchTime = sumUpStretchTasksTime(stretchModel.tasks.findAll {Task t-> t.noStretch})
    float stretchTime = sumUpTasksTime(stretchModel.tasks.findAll {!it.noStretch})

    println "No stretch tasks sum up time ${noStretchTime.round(2)}"
    println "Stretch tasks sum up time ${stretchTime.round(2)}"
    println "Overall efficency ${(timeEffectiveFloat/(timeInOfficeFloat - alreadyReported)).round(2)}"
    println "Office - effective difference ${(timeInOfficeFloat - timeEffectiveFloat).round(2)}"
    println "Office - alreadyReported ${(timeInOfficeFloat - alreadyReported).round(2)}"

    float timeInOfficeLeft = timeInOfficeFloat - alreadyReported

    float leftEfficiency = (timeInOfficeLeft - noStretchTime)/stretchTime
    println "Extension coef for timeLeft ${leftEfficiency.round(2)}"

    println '''
===================='''

    stretchModel.tasks.each {Task it -> it.doStretch((float)leftEfficiency)}
  }
}
