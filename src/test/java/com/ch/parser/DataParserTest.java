package com.ch.parser;

import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sahil on 6/25/16.
 */
public class DataParserTest {


    @Test
    public void testBasicData() {
        final List<SpecColumnDescriptor> descriptors = Arrays.asList(
                new SpecColumnDescriptor("name", 10, DataType.TEXT),
                new SpecColumnDescriptor("valid", 1, DataType.BOOLEAN),
                new SpecColumnDescriptor("count", 3, DataType.INTEGER)
        );
        final DataRowParser rowParser = new DataRowParser(descriptors);
        final DataParser parser = new DataParser(Paths.get(DataParserTest.class.getResource("data1.txt").getPath()), rowParser);

        final List<DataRow> rows = parser.read();

        assertEquals(3, rows.size());
        //check first row
        final List<DataRowColumnValue> columnValues0 = rows.get(0).getDataRowColumnValues();
        assertEquals(new DataRowColumnValue("name", DataType.TEXT, "Foonyor"), columnValues0.get(0));
        assertEquals(new DataRowColumnValue("valid", DataType.BOOLEAN, "1"), columnValues0.get(1));
        assertEquals(new DataRowColumnValue("count", DataType.INTEGER, "1"), columnValues0.get(2));
        //check second row
        final List<DataRowColumnValue> columnValues1 = rows.get(1).getDataRowColumnValues();
        assertEquals(new DataRowColumnValue("name", DataType.TEXT, "Barzane"), columnValues1.get(0));
        assertEquals(new DataRowColumnValue("valid", DataType.BOOLEAN, "0"), columnValues1.get(1));
        assertEquals(new DataRowColumnValue("count", DataType.INTEGER, "-12"), columnValues1.get(2));
        //check third row
        final List<DataRowColumnValue> columnValues2 = rows.get(2).getDataRowColumnValues();
        assertEquals(new DataRowColumnValue("name", DataType.TEXT, "Quuxitude"), columnValues2.get(0));
        assertEquals(new DataRowColumnValue("valid", DataType.BOOLEAN, "1"), columnValues2.get(1));
        assertEquals(new DataRowColumnValue("count", DataType.INTEGER, "103"), columnValues2.get(2));
    }

}
