package ru.silvestrov.timetracker;

import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.ActivityDao;
import ru.silvestrov.timetracker.data.TimeEntryDao;
import ru.silvestrov.timetracker.model.activitycontrollist.TimeHelp;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Ilya Silvestrov
 * Date: 3/31/13
 * Time: 5:34 PM
 */
public class CommandLineTools {
    private ActivityDao activityDao;
    private TimeEntryDao timeEntryDao;
    private PrintWriter printWriter;

    public CommandLineTools(
            ActivityDao activityDao,
            TimeEntryDao timeEntryDao,
            PrintWriter printWriter) {
        this.activityDao = activityDao;
        this.timeEntryDao = timeEntryDao;
        this.printWriter = printWriter;
    }

    public CommandLineTools(
            ActivityDao activityDao,
            TimeEntryDao timeEntryDao) {
        this.activityDao = activityDao;
        this.timeEntryDao = timeEntryDao;
    }

    public static boolean isCommandSupported(String[] args) {
        return args.length > 0 && args[0].equals("list");
    }

    public void main(String[] args) {
        String cmd = args[0];
        if ("list".equals(cmd)) {
            list();
        } else {
            throw new RuntimeException(
                    String.format("Unknown command '%s'" + cmd)
            );
        }
    }

    public void list() {
        List<Activity> activities = activityDao.listCurrentActivities();
        long totalTime = 0;
        long time;
        for (Activity activity : activities) {
            printWriter.print(activity.getName());
            printWriter.print("\t");
            time = timeEntryDao.getTotalTime(activity.getId());
            totalTime += time;
            printWriter.println(TimeHelp.formatTime(time));
            printWriter.flush();
        }
        printWriter.print("Total:\t");
        printWriter.println(TimeHelp.formatTime(totalTime));
        printWriter.flush();
    }
}
