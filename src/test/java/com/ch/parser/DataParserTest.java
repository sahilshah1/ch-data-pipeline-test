package com.ch.parser;

import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sahil on 6/25/16.
 */
public class DataParserTest {


    @Test
    public void testBasicData() {
        final List<SpecColumnDescriptor> descriptors = Arrays.asList(
                new SpecColumnDescriptor("name", 10, SpecDataType.TEXT),
                new SpecColumnDescriptor("valid", 1, SpecDataType.BOOLEAN),
                new SpecColumnDescriptor("count", 3, SpecDataType.INTEGER)
        );
        final DataParser parser = new DataParser(Paths.get(DataParserTest.class.getResource("data1.txt").getPath()), descriptors);

        final List<DataRow> rows = readAllRows(parser);

        assertEquals(3, rows.size());
        //check first row
        final List<DataRowColumnValue> columnValues0 = rows.get(0).getDataRowColumnValues();
        assertEquals(new DataRowColumnValue("name", SpecDataType.TEXT, "Foonyor"), columnValues0.get(0));
        assertEquals(new DataRowColumnValue("valid", SpecDataType.BOOLEAN, "1"), columnValues0.get(1));
        assertEquals(new DataRowColumnValue("count", SpecDataType.INTEGER, "1"), columnValues0.get(2));
        //check second row
        final List<DataRowColumnValue> columnValues1 = rows.get(1).getDataRowColumnValues();
        assertEquals(new DataRowColumnValue("name", SpecDataType.TEXT, "Barzane"), columnValues1.get(0));
        assertEquals(new DataRowColumnValue("valid", SpecDataType.BOOLEAN, "0"), columnValues1.get(1));
        assertEquals(new DataRowColumnValue("count", SpecDataType.INTEGER, "-12"), columnValues1.get(2));
        //check third row
        final List<DataRowColumnValue> columnValues2 = rows.get(2).getDataRowColumnValues();
        assertEquals(new DataRowColumnValue("name", SpecDataType.TEXT, "Quuxitude"), columnValues2.get(0));
        assertEquals(new DataRowColumnValue("valid", SpecDataType.BOOLEAN, "1"), columnValues2.get(1));
        assertEquals(new DataRowColumnValue("count", SpecDataType.INTEGER, "103"), columnValues2.get(2));
    }

    @Test
    public void testData2Format() {
        final List<SpecColumnDescriptor> descriptors = Arrays.asList(
                new SpecColumnDescriptor("isValid", 1, SpecDataType.BOOLEAN),
                new SpecColumnDescriptor("colName", 5, SpecDataType.TEXT),
                new SpecColumnDescriptor("counts", 3, SpecDataType.INTEGER)
        );
        final DataParser parser = new DataParser(Paths.get(DataParserTest.class.getResource("data2.txt").getPath()), descriptors);

        final List<DataRow> rows = readAllRows(parser);

        assertEquals(2, rows.size());
        //check first row
        final List<DataRowColumnValue> columnValues0 = rows.get(0).getDataRowColumnValues();
        assertEquals(new DataRowColumnValue("isValid", SpecDataType.BOOLEAN, "1"), columnValues0.get(0));
        assertEquals(new DataRowColumnValue("colName", SpecDataType.TEXT, "hello"), columnValues0.get(1));
        assertEquals(new DataRowColumnValue("counts", SpecDataType.INTEGER, "033"), columnValues0.get(2));
        //check second row
        final List<DataRowColumnValue> columnValues1 = rows.get(1).getDataRowColumnValues();
        assertEquals(new DataRowColumnValue("isValid", SpecDataType.BOOLEAN, "0"), columnValues1.get(0));
        assertEquals(new DataRowColumnValue("colName", SpecDataType.TEXT, "no"), columnValues1.get(1));
        assertEquals(new DataRowColumnValue("counts", SpecDataType.INTEGER, "1"), columnValues1.get(2));
    }

    private List<DataRow> readAllRows(final DataParser parser) {
        final List<DataRow> rows = new LinkedList<>();
        parser.read(rows::add);
        return rows;
    }

}
