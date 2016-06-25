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
        final List<SpecParser.SpecColumnDescriptor> descriptors = Arrays.asList(
                new SpecParser.SpecColumnDescriptor("name", 10, SpecParser.DataType.TEXT),
                new SpecParser.SpecColumnDescriptor("valid", 1, SpecParser.DataType.BOOLEAN),
                new SpecParser.SpecColumnDescriptor("count", 3, SpecParser.DataType.INTEGER)
        );
        final SpecParser.DataRowParser rowParser = new SpecParser.DataRowParser(descriptors);
        final DataParser parser = new DataParser(Paths.get(DataParserTest.class.getResource("data1.txt").getPath()), rowParser);

        final List<SpecParser.DataRow> rows = parser.read();

        assertEquals(3, rows.size());
        //check first row
        final List<SpecParser.DataColumnValue> columnValues0 = rows.get(0).getDataColumnValues();
        assertEquals(new SpecParser.DataColumnValue("name", SpecParser.DataType.TEXT, "Foonyor"), columnValues0.get(0));
        assertEquals(new SpecParser.DataColumnValue("valid", SpecParser.DataType.BOOLEAN, "1"), columnValues0.get(1));
        assertEquals(new SpecParser.DataColumnValue("count", SpecParser.DataType.INTEGER, "1"), columnValues0.get(2));
        //check second row
        final List<SpecParser.DataColumnValue> columnValues1 = rows.get(1).getDataColumnValues();
        assertEquals(new SpecParser.DataColumnValue("name", SpecParser.DataType.TEXT, "Barzane"), columnValues1.get(0));
        assertEquals(new SpecParser.DataColumnValue("valid", SpecParser.DataType.BOOLEAN, "0"), columnValues1.get(1));
        assertEquals(new SpecParser.DataColumnValue("count", SpecParser.DataType.INTEGER, "-12"), columnValues1.get(2));
        //check third row
        final List<SpecParser.DataColumnValue> columnValues2 = rows.get(2).getDataColumnValues();
        assertEquals(new SpecParser.DataColumnValue("name", SpecParser.DataType.TEXT, "Quuxitude"), columnValues2.get(0));
        assertEquals(new SpecParser.DataColumnValue("valid", SpecParser.DataType.BOOLEAN, "1"), columnValues2.get(1));
        assertEquals(new SpecParser.DataColumnValue("count", SpecParser.DataType.INTEGER, "103"), columnValues2.get(2));
    }

}
