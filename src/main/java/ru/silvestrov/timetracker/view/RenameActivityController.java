package ru.silvestrov.timetracker.view;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import ru.silvestrov.timetracker.data.Activity;
import ru.silvestrov.timetracker.data.ActivityDao;
import ru.silvestrov.timetracker.model.activitycontrollist.ActivityControlList;

/**
 * Created by Silvestrov Ilya
 * Date: 12/17/11
 * Time: 11:51 PM
 */
public class RenameActivityController {
    private static final Logger logger = Logger.getLogger(RenameActivityController.class);

    private ActivityDao activityDao;
    private ActivityControlList activityControlList;
    private TransactionTemplate transactionTemplate;

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public void setActivityControlList(ActivityControlList activityControlList) {
        this.activityControlList = activityControlList;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void renameActivity(int activityControlIdx, final String newName) {
        if (logger.isDebugEnabled()) {
            logger.debug("Going to rename activity with idx: " + activityControlIdx + " to new name " + newName);
        }
        final Activity activity = activityControlList.getActivity(activityControlIdx);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                activity.setName(newName);
                activityDao.save(activity);
            }
        });
    }
}
