package ru.silvestrov.timetracker.data;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Silvestrov Ilya
 * Date: 1/29/12
 * Time: 1:46 AM
 */
public class SchemaUpdater extends JdbcDaoSupport {
    public void updateSchema() {
        getJdbcTemplate().execute(new ConnectionCallback() {
            public Object doInConnection(Connection con) throws SQLException, DataAccessException {
                ResultSet activityNameColumn = con.getMetaData().getColumns(null, null, "ACTIVITY", "NAME");
                if (!activityNameColumn.next())
                    throw new RuntimeException("Unable to get activity.name column description");
                int activityNameSize = activityNameColumn.getInt("COLUMN_SIZE");
                if (activityNameSize < 1024) {
                    con.createStatement().execute("alter table activity alter column name set data type varchar(1024)");
                }
                return null;
            }
        });
    }
}
