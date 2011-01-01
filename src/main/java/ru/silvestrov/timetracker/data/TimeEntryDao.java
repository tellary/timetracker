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
        getHibernateTemplate().persist(timeEntry);
    }

    public void save(TimeEntry timeEntry) {
        getHibernateTemplate().merge(timeEntry);
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
}
