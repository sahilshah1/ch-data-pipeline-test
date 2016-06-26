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

        final List<SpecColumnDescriptor> descriptorList = parser.read();

        assertEquals(3, descriptorList.size());
        assertEquals(new SpecColumnDescriptor("name", 10, SpecDataType.TEXT), descriptorList.get(0));
        assertEquals(new SpecColumnDescriptor("valid", 1, SpecDataType.BOOLEAN), descriptorList.get(1));
        assertEquals(new SpecColumnDescriptor("count", 3, SpecDataType.INTEGER), descriptorList.get(2));
    }

}
