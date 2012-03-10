package ru.silvestrov.timetracker.view;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.TimeEntry;
import ru.silvestrov.timetracker.data.TimeEntryDao;
import ru.silvestrov.timetracker.model.activitycontrollist.ActivityControlList;
import ru.silvestrov.timetracker.model.activitycontrollist.TimeHelp;

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
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    Activity activity = activityControlList.getActivity(activityIdx);
                    TimeEntry lastTimeEntry = timeEntryDao.getLastTimeEntry(activity.getId());
                    if (lastTimeEntry == null) {
                        //Some additional handling here should be done for U24.
                         System.out.println(String.format("Null time entry found for activity with id=%s, name='%s'",
                             activity.getId(), activity.getName()));
                        return;
                    }

                    if (lastTimeEntry.isActive())
                        throw new RuntimeException("Active activity is prohibit to be decreased");

                    long size = lastTimeEntry.getTimeEnd() - lastTimeEntry.getTimeStart();

                    long adjustmentMillis;
                    if (adjustment.startsWith("-"))
                        adjustmentMillis = TimeHelp.timeStringToMillis(adjustment.substring(1));
                    else
                        adjustmentMillis = TimeHelp.timeStringToMillis(adjustment);

                    if (adjustmentMillis >= size) {
                        timeEntryDao.delete(lastTimeEntry);
                        if (adjustmentMillis > size) {
                            //Some additional handling here should be done for U24.
                            System.out.println(String.format("Deleting time entry for smaller size %s when adjustment %s", size, adjustment));
                        }
                    } else {
                        long timeEnd = lastTimeEntry.getTimeEnd() - adjustmentMillis;
                        lastTimeEntry.setTimeEnd(timeEnd);
                        timeEntryDao.save(lastTimeEntry);
                    }
                }
            });
        }
    }
}
