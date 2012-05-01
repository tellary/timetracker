package ru.silvestrov.timetracker.data;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 9, 2008
 * Time: 11:53:37 PM
 */
public class ActivityDao extends HibernateDaoSupport {
    public void save(Activity activity) {
        getHibernateTemplate().saveOrUpdate(activity);
    }

    @SuppressWarnings({"unchecked"})
    public List<Activity> listCurrentActivities() {
        return getHibernateTemplate().find("FROM Activity WHERE finished = false");
    }

    public Activity getActivityById(long id) {
        return (Activity) getHibernateTemplate().find("FROM Activity WHERE id = ?", id).get(0);
    }
}
