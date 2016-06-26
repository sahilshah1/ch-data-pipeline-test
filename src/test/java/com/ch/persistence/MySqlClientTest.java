package com.ch.persistence;

import com.ch.parser.DataRow;
import com.ch.parser.DataRowColumnValue;
import com.ch.parser.SpecDataType;
import com.ch.parser.SpecColumnDescriptor;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sahil on 6/25/16.
 */
@Ignore("test is not set up for repeatable integration testing, hence ignored")
public class MySqlClientTest {

    @Test
    public void testMySqlInit()
            throws PersistenceClient.PersistenceException {
        final PersistenceClient client = MySqlClient.newClient("SOME_DB");
    }

    @Test
    public void testMySqlCreateTable()
            throws PersistenceClient.PersistenceException {
        final PersistenceClient client = MySqlClient.newClient("SOME_DB");
        final List<SpecColumnDescriptor> descriptors = Arrays.asList(
                new SpecColumnDescriptor("name", 10, SpecDataType.TEXT),
                new SpecColumnDescriptor("valid", 1, SpecDataType.BOOLEAN),
                new SpecColumnDescriptor("count", 3, SpecDataType.INTEGER)
        );

        client.createTable("testTable", descriptors);
    }

    @Test
    public void testInsertRecord()
            throws PersistenceClient.PersistenceException {
        final PersistenceClient client = MySqlClient.newClient("SOME_DB");
        final List<DataRowColumnValue> values = Arrays.asList(
                new DataRowColumnValue("name", SpecDataType.TEXT, "Sahil"),
                new DataRowColumnValue("valid", SpecDataType.BOOLEAN, "1"),
                new DataRowColumnValue("count", SpecDataType.INTEGER, "5")
        );

        client.insertRecords("testTable", Collections.singletonList(new DataRow(values)));
    }
}
