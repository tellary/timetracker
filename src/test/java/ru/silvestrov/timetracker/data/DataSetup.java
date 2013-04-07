package ru.silvestrov.timetracker.data;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * Created by Silvestrov Ilya.
 * Date: Jul 20, 2008
 * Time: 4:54:11 PM
 */
public class DataSetup extends JdbcDaoSupport {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private TimeEntryDao timeEntryDao;
    @Resource
    private TransactionTemplate tt;
    @Resource
    private SessionFactory sessionFactory;


    public void cleanup() {
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                getJdbcTemplate().
                        execute("update activity set currenttimeentry_id = null");
                getJdbcTemplate().
                        execute("delete from time_entry");
                getJdbcTemplate().
                        execute("delete from activity");

                sessionFactory.getCache().evictQueryRegions();
                sessionFactory.getCache().evictCollectionRegions();
                sessionFactory.getCache().evictEntityRegions();
                sessionFactory.getCache().evictDefaultQueryRegion();
            }
        });
    }

    public void setup() {
        cleanup();
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                getJdbcTemplate().
                        execute("insert into activity (id, name, finished) " +
                                "values (1, 'First Activity', 0)");
                getJdbcTemplate().
                        execute("insert into activity (id, name, finished, parent_id) " +
                                "values (2, 'Second Activity', 0, 1)");
                getJdbcTemplate().
                        execute("insert into activity (id, name, finished, parent_id) " +
                                "values (3, 'Third Activity', 0, 1)");
                getJdbcTemplate().
                        execute("insert into time_entry (id, time_start, time_end, activity_id) " +
                                "values (1, 0, 100, 2)");
                getJdbcTemplate().
                        execute("insert into time_entry (id, time_start, time_end, activity_id) " +
                                "values (2, 100, 200, 2)");
                getJdbcTemplate().
                        execute("insert into time_entry (id, time_start, activity_id) " +
                                "values (3, 200, 2)");
                getJdbcTemplate().
                        execute("update activity set currenttimeentry_id = 3 where id = 2");


            }
        });
    }
}
