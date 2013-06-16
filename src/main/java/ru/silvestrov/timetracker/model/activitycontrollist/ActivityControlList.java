package ru.silvestrov.timetracker.model.activitycontrollist;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.ActivityDao;
import ru.silvestrov.timetracker.data.TimeEntry;
import ru.silvestrov.timetracker.data.TimeEntryDao;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private static final Logger logger = Logger.getLogger(ActivityControlList.class);

    @Resource
    private ActivityDao activityDao;
    @Resource
    private TimeEntryDao timeEntryDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    private List<Long> activityIds;
    private long currentActivityId = -1;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ActivityControlListUpdateListener updateListener;
    private Future future;
    private long startTime;

    private long timeStep = 1000;
    private TimeUnit unit = TimeUnit.MILLISECONDS;

    @SuppressWarnings({"FieldCanBeLocal"})
    private long smallTimeThreshold = 3000;

    public void setUpdateListener(ActivityControlListUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public int size() {
        return activityIds.size();
    }

    public void addActivity(final String name) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Activity activity = new Activity();
                activity.setName(name);
                activityDao.save(activity);

                stopTimer();

                activityIds.add(activity.getId());
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


    public void startTimer() {
        this.startTime = System.currentTimeMillis();
        int i = 0;
        for (long activityId : activityIds) {
            if (activityId == currentActivityId) {
                future = executor.scheduleAtFixedRate(
                        new Notifier(i),
                        timeStep - (System.currentTimeMillis() - startTime),
                        timeStep, unit);
            }
            ++i;
        }
    }

    private void processStopData(final int activityIdx, final long timeEnd, final long activityId) {
        boolean timeDeleted = (Boolean) transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                Activity activity = activityDao.getActivityById(activityId);
                TimeEntry timeEntry = activity.getCurrentTimeEntry();
                boolean timeDeleted;
                if (timeEnd - timeEntry.getTimeStart() < smallTimeThreshold) {
                    timeEntryDao.delete(timeEntry);
                    timeDeleted = true;
                } else {
                    timeEntry.setTimeEnd(timeEnd);
                    timeEntryDao.save(timeEntry);
                    timeDeleted = false;
                }
                activity.setCurrentTimeEntry(null);
                activityDao.save(activity);
                currentActivityId = -1;

                if (logger.isDebugEnabled()) {
                    logger.debug(
                            String.format(
                                    "Processed stop data for activityIdx %d, activityId %d",
                                    activityIdx, activityId
                            ));

                }

                return timeDeleted;
            }
        });
        if (timeDeleted) {
            //update notification is sent here to redraw the row as timer data already drawn in the
            //timer cell is stale after last entry deletion
            updateListener.activityTimeUpdated(activityIdx);
        }

    }

    public void stopTimer() {
        if (future != null)
            future.cancel(false);

        long currentTime = System.currentTimeMillis();
        int i = 0;
        for (long activityId : activityIds) {
            if (activityId == currentActivityId) {
                processStopData(i, currentTime, activityId);
                startTime = currentTime;
            }
            ++i;
        }
    }

    private void processStartData(final long timeStart, final long activityId) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Activity activity = activityDao.getActivityById(activityId);
                TimeEntry timeEntry = new TimeEntry(timeStart, activity);
                timeEntryDao.addTimeEntry(timeEntry);
                activity.setCurrentTimeEntry(timeEntry);
                activityDao.save(activity);
                currentActivityId = activityId;
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            String.format(
                                    "Processed start data for activityId %d",
                                    activityId
                            ));
                }
            }
        });
    }

    public void makeActive(int index) {
        if (index < 0)
            throw new RuntimeException("Illegal index '" + index + "'. Must be zero or positive.");

        stopTimer();

        if (future != null)
            future.cancel(false);

        int i = 0;
        long currentTime = System.currentTimeMillis();
        for (long activityId : activityIds) {
            if (i == index) {
                processStartData(currentTime, activityId);
                future = executor.scheduleAtFixedRate(new Notifier(i), timeStep - (currentTime - startTime), timeStep, unit);
            }
            ++i;
        }
        startTime = currentTime;
    }

    public Activity getActivity(final int i) {
        return (Activity) transactionTemplate.execute(new TransactionCallback() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                return activityDao.getActivityById(activityIds.get(i));
            }
        });
    }

    public ActivityInfo getActivityInfo(final int i) {
        return (ActivityInfo) transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                long activityId = activityIds.get(i);
                Activity activity = activityDao.getActivityById(activityId);
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
                long activityId = activityIds.get(i);
                Activity activity = activityDao.getActivityById(activityId);
                activity.setFinished(true);
                activityDao.save(activity);

                stopTimer();
                //We remove activity after stopping timer
                //because stopTimer() iterates over
                //activities to find the current one
                //to stop it.
                activityIds.remove(i);
            }
        });

        startTimer();
        updateListener.invalidateList();
    }

    @SuppressWarnings({"unchecked"})
    public void afterPropertiesSet() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                activityIds = new ArrayList<>();
                boolean currentActivityFound = false;
                for (Activity activity : activityDao.listCurrentActivities()) {
                    activityIds.add(activity.getId());
                    if (activity.getCurrentTimeEntry() != null) {
                        if (currentActivityFound)
                            throw new IllegalStateException(
                                    String.format(
                                            "Multiple current activities found. " +
                                                    "Current activity id is %d. " +
                                                    "Another current activity id is %d.",
                                            currentActivityId, activity.getId()
                                    ));
                        currentActivityId = activity.getId();
                        currentActivityFound = true;
                        if (logger.isDebugEnabled()) {
                            logger.debug(
                                    String.format(
                                           "Loaded current activity %d", currentActivityId
                                    ));
                        }
                    }
                }
            }
        });
    }
}
