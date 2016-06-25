package com.ch.parser;

import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
    }

}
