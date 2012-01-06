package ru.silvestrov.timetracker.model;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.ActivityDao;
import ru.silvestrov.timetracker.data.DataConfiguration;
import ru.silvestrov.timetracker.data.TimeEntry;
import ru.silvestrov.timetracker.data.TimeEntryDao;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 12, 2008
 * Time: 9:13:30 PM
 */
public class ActivityControlList implements InitializingBean {
    private ActivityDao activityDao;
    private TimeEntryDao timeEntryDao;
    private TransactionTemplate transactionTemplate;
    private List<Activity> activities;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ActivityControlListUpdateListener updateListener;
    private Future future;
    private long startTime;

    private long timeStep = 1000;
    private TimeUnit unit = TimeUnit.MILLISECONDS;


    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public void setTimeEntryDao(TimeEntryDao timeEntryDao) {
        this.timeEntryDao = timeEntryDao;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void setDataConfiguration(DataConfiguration dataConfiguration) {
        setActivityDao(dataConfiguration.getActivityDao());
        setTimeEntryDao(dataConfiguration.getTimeEntryDao());
        setTransactionTemplate(dataConfiguration.getTransactionTemplate());
    }

    public void setUpdateListener(ActivityControlListUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public int size() {
        return activities.size();
    }

    public void addActivity(final String name) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Activity activity = new Activity();
                activity.setName(name);
                activityDao.save(activity);

                stopTimer();

                activities =  activityDao.listCurrentActivities();
            }
        });

        startTimer();
        updateListener.invalidateList();
    }

    private class Notifier implements Runnable {
        private int index;

        public Notifier(int index) {
            this.index = index;
        }

        public void run() {
            updateListener.activityTimeUpdated(index);
        }
    }


    private void startTimer() {
        this.startTime = System.currentTimeMillis();
        boolean found = false;
        int i = 0;
        for (Activity activity : activities) {
            if (activity.getCurrentTimeEntry() != null) {
                if (found)
                    throw new RuntimeException("Unable to schedule multiple concurrent activities");
                found = true;
                future = executor.scheduleAtFixedRate(new Notifier(i), timeStep - (System.currentTimeMillis() - startTime), timeStep, unit);
            }
            ++i;
        }
    }

    private void processStopData(final long timeEnd, final Activity activity) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                TimeEntry timeEntry = activity.getCurrentTimeEntry();
                timeEntry.setTimeEnd(timeEnd);
                timeEntryDao.save(timeEntry);
                activity.setCurrentTimeEntry(null);
                activityDao.save(activity);
            }
        });
    }

    public void stopTimer() {
        boolean found = false;
        if (future != null)
            future.cancel(false);

        long currentTime = System.currentTimeMillis();
        for (Activity activity : activities) {
            if (activity.getCurrentTimeEntry() != null) {
                if (found)
                    throw new RuntimeException("Unable to schedule multiple concurrent activities");
                found = true;
                processStopData(currentTime, activity);
                startTime = currentTime;
            }
        }
    }

    private void processStartData(final long timeStart, final Activity activity) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                TimeEntry timeEntry = new TimeEntry(timeStart, activity);
                timeEntryDao.addTimeEntry(timeEntry);
                activity.setCurrentTimeEntry(timeEntry);
                activityDao.save(activity);
            }
        });
    }

    public void makeActive(int index) {
        if (index < 0)
            throw new RuntimeException("Illegal index '" + index + "'. Must be zero or positive.");

        if (future != null)
            future.cancel(false);

        boolean found = false;
        int i = 0;
        long currentTime = System.currentTimeMillis();
        for (Activity activity : activities) {
            if (activity.getCurrentTimeEntry() != null) {
                if (found)
                    throw new RuntimeException("Unable to schedule multiple concurrent activities");
                found = true;
                processStopData(currentTime, activity);
            }
            if (i == index) {
                processStartData(currentTime, activity);
                future = executor.scheduleAtFixedRate(new Notifier(i), timeStep - (currentTime - startTime), timeStep, unit);
            }
            ++i;
        }
        startTime = currentTime;
    }

    public Activity getActivity(final int i) {
        return activities.get(i);
    }

    public ActivityInfo getActivityInfo(final int i) {
        return (ActivityInfo) transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                Activity activity = activities.get(i);
                long time = timeEntryDao.getTotalTime(activity.getId());
                TimeEntry timeEntry = activity.getCurrentTimeEntry();
                if (timeEntry != null) {
                    return new ActivityInfo(
                        activity.getId(),
                        activity.getName(),
                        time + System.currentTimeMillis() - timeEntry.getTimeStart());
                } else {
                    return new ActivityInfo(activity.getId(), activity.getName(), time);
                }
            }
        });
    }

    public void finishActivity(final int i) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Activity activity = activities.get(i);
                activity.setFinished(true);
                activityDao.save(activity);
                activities.remove(i);

                stopTimer();
            }
        });

        startTimer();
        updateListener.invalidateList();
    }

    @SuppressWarnings({"unchecked"})
    public void afterPropertiesSet() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                activities =  activityDao.listCurrentActivities();
            }
        });
        startTimer();
    }
}
