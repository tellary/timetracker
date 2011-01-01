package ru.silvestrov.timetracker.data;

import org.dbunit.operation.DatabaseOperation;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.statement.IStatementFactory;
import org.dbunit.database.statement.IBatchStatement;
import org.dbunit.dataset.IDataSet;
import org.dbunit.DatabaseUnitException;

import java.sql.SQLException;

/**
 * Created by Silvestrov Iliya.
 * Date: Jul 20, 2008
 * Time: 1:13:29 PM
 */
public abstract class ExecuteStatementOperation extends DatabaseOperation {
    public void execute(IDatabaseConnection connection, IDataSet dataSet) throws DatabaseUnitException, SQLException {
        DatabaseConfig databaseConfig = connection.getConfig();
        IStatementFactory statementFactory = (IStatementFactory)databaseConfig.getProperty(DatabaseConfig.PROPERTY_STATEMENT_FACTORY);
        IBatchStatement statement = statementFactory.createBatchStatement(connection);

        try {
            statement.addBatch(getStatement());
            statement.executeBatch();
        } finally {
            statement.close();
        }
    }

    protected abstract String getStatement();
}
