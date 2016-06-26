package com.ch.db;

import com.ch.parser.DataRowColumnValue;
import com.ch.parser.DataType;
import com.ch.parser.SpecColumnDescriptor;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sahil on 6/25/16.
 */
@Ignore("test is not set up for repeatable integration testing, hence ignored")
public class MySqlClientTest {

    @Test
    public void testMySqlInit()
            throws PersistenceClient.PersistenceException {
        final PersistenceClient client = new MySqlClient("SOME_DB");
    }

    @Test
    public void testMySqlCreateTable()
            throws PersistenceClient.PersistenceException {
        final PersistenceClient client = new MySqlClient("SOME_DB");
        final List<SpecColumnDescriptor> descriptors = Arrays.asList(
                new SpecColumnDescriptor("name", 10, DataType.TEXT),
                new SpecColumnDescriptor("valid", 1, DataType.BOOLEAN),
                new SpecColumnDescriptor("count", 3, DataType.INTEGER)
        );

        client.createTable("testTable", descriptors);
    }

    @Test
    public void testInsertRecord()
            throws PersistenceClient.PersistenceException {
        final PersistenceClient client = new MySqlClient("SOME_DB");
        final List<DataRowColumnValue> values = Arrays.asList(
                new DataRowColumnValue("name", DataType.TEXT, "Sahil"),
                new DataRowColumnValue("valid", DataType.BOOLEAN, "1"),
                new DataRowColumnValue("count", DataType.INTEGER, "5")
        );

        client.insertRecord("testTable", values);
    }
}
