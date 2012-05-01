package ru.silvestrov.timetracker.data;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.CompositeOperation;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import javax.annotation.Resource;

/**
 * Created by Silvestrov Ilya.
 * Date: Jul 20, 2008
 * Time: 4:54:11 PM
 */
public class DataSetup {
    @Resource
    private IDatabaseTester tester;

    public void setup() {
        try {
            FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
            FlatXmlDataSet dataSet = flatXmlDataSetBuilder.build(
                ActivityDaoTest.class.getResourceAsStream("activities.xml"));
            ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
            replacementDataSet.addReplacementObject("[null]", null);
            tester.setDataSet(replacementDataSet);
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
}
