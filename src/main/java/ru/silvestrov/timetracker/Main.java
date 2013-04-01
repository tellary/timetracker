package ru.silvestrov.timetracker;

import org.springframework.context.ApplicationContext;
import ru.silvestrov.timetracker.data.ActivityDao;
import ru.silvestrov.timetracker.data.ContextUtil;
import ru.silvestrov.timetracker.data.TimeEntryDao;

/**
 * Created by Silvestrov Ilya
 * Date: 7/23/12
 * Time: 12:22 AM
 */
public class Main {
    private static final String DB_NAME = "./timeDB";
    public static void main(String[] args) {
		if (CommandLineTools.isCommandSupported(args)) {
            ApplicationContext ctx = ContextUtil.createDaoContext(DB_NAME);
            CommandLineTools commandLineTools =
                    new CommandLineTools(
                            (ActivityDao)ctx.getBean("activityDao"),
                            (TimeEntryDao)ctx.getBean("timeEntryDao")
                    );

            commandLineTools.main(args);
        } else {
            ContextUtil.createContext(DB_NAME);
        }
    }
}
