package com.ch.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Parses a spec file.
 * Created by sahil on 6/25/16.
 */
public class SpecParser {

    private final Path specFilePath;

    public SpecParser(final Path specFilePath) {
        this.specFilePath = specFilePath;
    }

    public List<SpecColumnDescriptor> read() {
        final List<SpecColumnDescriptor> descriptors = new ArrayList<>();

        int columnNameIndex = -1;
        int columnWidthIndex = -1;
        int columnDataTypeIndex = -1;

        try (final BufferedReader br = new BufferedReader(new FileReader(this.specFilePath.toAbsolutePath().toString()))) {
            String firstLine = null;
            String line;
            while ((line = br.readLine()) != null) {
                //first line contains description of columns
                if (firstLine == null) {
                    firstLine = line;
                    //TODO: make this not hardcoded
                    final String[] values = firstLine.split(",");
                    for(int i = 0; i < values.length; i++) {
                        if (values[i].matches("\"column name\"")) {
                            columnNameIndex = i;
                        }
                        if (values[i].matches("width")) {
                            columnWidthIndex = i;
                        }
                        if (values[i].matches("datatype")) {
                            columnDataTypeIndex = i;
                        }
                    }
                }
                //every other line contains
                else {
                    final String[] values = line.split(",");
                    descriptors.add(new SpecColumnDescriptor(
                            values[columnNameIndex],
                            Integer.valueOf(values[columnWidthIndex]),
                            SpecDataType.valueOf(values[columnDataTypeIndex])));
                }


            }
        }
        catch (final IOException e) {
            System.out.println(e.toString());
        }

        return descriptors;
    }
}
