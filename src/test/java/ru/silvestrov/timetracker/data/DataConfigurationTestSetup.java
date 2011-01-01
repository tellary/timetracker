package ru.silvestrov.timetracker.data;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.operation.CompositeOperation;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

/**
 * Created by Silvestrov Iliya.
 * Date: Jul 20, 2008
 * Time: 4:54:11 PM
 */
public class DataConfigurationTestSetup {
    private DataConfiguration dataConfiguration;
    private IDatabaseTester tester;

    public DataConfigurationTestSetup() {
        dataConfiguration = new DataConfiguration("./testDB");
        ApplicationContext ctx = dataConfiguration.getApplicationContext();

        tester = new DataSourceDatabaseTester((DataSource) ctx.getBean("dataSource"));
        try {
            tester.setDataSet(new FlatXmlDataSet(TestDataConfiguration.class.getResourceAsStream("activities.xml")));
            tester.setSetUpOperation(new CompositeOperation(
                    new DatabaseOperation[]{
                            new ExecuteStatementOperation() {
                                protected String getStatement() {
                                    return "update activity set currenttimeentry_id = null";
                                }
                            },
                            DatabaseOperation.CLEAN_INSERT,
                            new ExecuteStatementOperation() {
                                protected String getStatement() {
                                    return "update activity set currenttimeentry_id = 3 where id = 2";
                                }
                            }
                    }));
            tester.onSetup();
        } catch (Exception e) {
            throw new RuntimeException("Failed to setup data configuration", e);
        }

    }

    public DataConfiguration getDataConfiguration() {
        return dataConfiguration;
    }

    public IDatabaseTester getTester() {
        return tester;
    }
}
