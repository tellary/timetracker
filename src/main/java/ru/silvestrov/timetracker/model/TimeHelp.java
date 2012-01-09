package ru.silvestrov.timetracker.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Silvestrov Ilya
 * Date: 1/9/12
 * Time: 10:03 PM
 */
public class TimeHelp {
    public static String formatTime(long time) {
        //Copy-pasted from TimeHelp#timeMillisToString
        //TODO: use common codebase
        long hours = time / 3600000;
        long hoursRemainder = time - hours * 3600000;
        long minutes = hoursRemainder / 60000;
        long minutesRemainder = hoursRemainder - minutes * 60000;
        long seconds = minutesRemainder / 1000;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private static final Pattern hourPattern = Pattern.compile("(\\d+)\\s*h");
    private static final Pattern minutePattern = Pattern.compile("(\\d+)\\s*m");
    private static final Pattern secondPattern = Pattern.compile("(\\d+)\\s*s");
    private static final Pattern lastMinutePattern = Pattern.compile("(\\d+)\\s*$");

    public static long timeStringToMillis(String timeString) {
        //Copy-pasted from TimeHelp#timeStringToMillis
        //TODO: use common codebase
        String[] timeSplit;
        if (timeString.matches(".*(h|m|s).*")) {
            timeSplit = new String[] {"0", "0", "0"};
            Matcher m;
            m = hourPattern.matcher(timeString);
            if (m.find())
                timeSplit[0] = m.group(1);
            m = minutePattern.matcher(timeString);
            if (m.find())
                timeSplit[1] = m.group(1);
            m = secondPattern.matcher(timeString);
            if (m.find())
                timeSplit[2] = m.group(1);
            m = lastMinutePattern.matcher(timeString);
            if (m.find())
                timeSplit[1] = m.group(1);
        } else {
            timeSplit = timeString.split(":");
            if (timeSplit.length == 2) {
                timeSplit = new String[] {"0", timeSplit[0], timeSplit[1]};
            } else if (timeSplit.length == 1) {
                timeSplit = new String[] {"0", timeSplit[0], "0"};
            }
        }
        int hours = Integer.parseInt(timeSplit[0]);
        int minutes = Integer.parseInt(timeSplit[1]);
        int seconds = Integer.parseInt(timeSplit[2]);
        return hours * 3600000 + minutes * 60000 + seconds * 1000;
    }
}
