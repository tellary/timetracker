package ru.silvestrov.timetracker.data;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.hibernate.Query;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 12, 2008
 * Time: 9:30:20 PM
 */
public class TimeEntryDao extends HibernateDaoSupport {
    public void addTimeEntry(TimeEntry timeEntry) {
        getHibernateTemplate().save(timeEntry);
    }

    public void save(TimeEntry timeEntry) {
        getHibernateTemplate().saveOrUpdate(timeEntry);
    }

    @SuppressWarnings({"unchecked"})
    public long getTotalTime(long activityId) {
        Query q = getSession().createQuery("SELECT SUM(timeEnd - timeStart) FROM TimeEntry WHERE activity.id = ? AND timeEnd IS NOT NULL");
        q.setLong(0, activityId);
        q.setCacheable(true);
        Long time = (Long) q.uniqueResult();
        if (time != null)
            return time;
        else
            return 0;
    }

    public TimeEntry getLastTimeEntry(long activityId) {
        Query q = getSession().createQuery(
            "SELECT t FROM TimeEntry t " +
                "WHERE t.activity.id = ? " +
                "AND t.timeStart = (" +
                "   SELECT max(t1.timeStart) " +
                "   FROM TimeEntry t1" +
                "   WHERE t1.activity.id = ?)");
        q.setLong(0, activityId);
        q.setLong(1, activityId);
        q.setCacheable(true);
        return (TimeEntry) q.uniqueResult();
    }

    public void delete(TimeEntry timeEntry) {
        getHibernateTemplate().delete(timeEntry);
    }
}
