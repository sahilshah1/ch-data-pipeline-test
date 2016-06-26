package com.ch.db;

import com.ch.parser.SpecParser;
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
        final PersistenceClient client = new MySqlClient("SOME_DB", "some_table");
    }

    @Test
    public void testMySqlCreateTable()
            throws PersistenceClient.PersistenceException {
        final PersistenceClient client = new MySqlClient("SOME_DB", "some_table");
        final List<SpecParser.SpecColumnDescriptor> descriptors = Arrays.asList(
                new SpecParser.SpecColumnDescriptor("name", 10, SpecParser.DataType.TEXT),
                new SpecParser.SpecColumnDescriptor("valid", 1, SpecParser.DataType.BOOLEAN),
                new SpecParser.SpecColumnDescriptor("count", 3, SpecParser.DataType.INTEGER)
        );

        client.createTable("testTable", descriptors);
    }

    @Test
    public void testInsertRecord()
            throws PersistenceClient.PersistenceException {
        final PersistenceClient client = new MySqlClient("SOME_DB", "some_table");
        final List<SpecParser.DataColumnValue> values = Arrays.asList(
                new SpecParser.DataColumnValue("name", SpecParser.DataType.TEXT, "Sahil"),
                new SpecParser.DataColumnValue("valid", SpecParser.DataType.BOOLEAN, "True"),
                new SpecParser.DataColumnValue("count", SpecParser.DataType.INTEGER, "5")
        );

        client.insertRecord("testTable", values);
    }
}
