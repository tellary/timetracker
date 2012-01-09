package ru.silvestrov.timetracker.view;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.TimeEntry;
import ru.silvestrov.timetracker.data.TimeEntryDao;
import ru.silvestrov.timetracker.model.ActivityControlList;
import ru.silvestrov.timetracker.model.TimeHelp;

/**
 * Created by Silvestrov Ilya
 * Date: 1/9/12
 * Time: 9:59 PM
 */
public class AdjustTimeController {
    private TransactionTemplate transactionTemplate;
    private TimeEntryDao timeEntryDao;
    private ActivityControlList activityControlList;

    public AdjustTimeController(
        TransactionTemplate transactionTemplate,
        TimeEntryDao timeEntryDao,
        ActivityControlList activityControlList) {

        this.transactionTemplate = transactionTemplate;
        this.timeEntryDao = timeEntryDao;
        this.activityControlList = activityControlList;
    }

    public void adjustTime(final int activityIdx, final String adjustment) {
        final long timeEnd = System.currentTimeMillis();
        if (adjustment.trim().isEmpty())
            return;

        if (adjustment.startsWith("+")) {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    Activity activity = activityControlList.getActivity(activityIdx);
                    long adjustmentMillis = TimeHelp.timeStringToMillis(adjustment.substring(1));
                    TimeEntry timeEntry = new TimeEntry(timeEnd - adjustmentMillis, activity);
                    timeEntry.setTimeEnd(timeEnd);
                    timeEntryDao.addTimeEntry(timeEntry);
                }
            });
        } else {
            throw new RuntimeException("Time decrease is not supported yet");
        }
    }
}
