package com.ch.parser;

import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sahil on 6/25/16.
 */
public class SpecParserTest {

    @Test
    public void testBasicFormat() {
        final SpecParser parser = new SpecParser(Paths.get(SpecParserTest.class.getResource("format1.txt").getPath()));

        final SpecParser.DataRowParser dataRowParser = parser.read();
        final List<SpecParser.SpecColumnDescriptor> descriptorList = dataRowParser.getSpecColumnDescriptors();

        assertEquals(3, descriptorList.size());
        assertEquals(new SpecParser.SpecColumnDescriptor("name", 10, SpecParser.DataType.TEXT), descriptorList.get(0));
        assertEquals(new SpecParser.SpecColumnDescriptor("valid", 1, SpecParser.DataType.BOOLEAN), descriptorList.get(1));
        assertEquals(new SpecParser.SpecColumnDescriptor("count", 3, SpecParser.DataType.INTEGER), descriptorList.get(2));
    }

}
