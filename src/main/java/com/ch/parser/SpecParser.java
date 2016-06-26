package com.ch.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses a spec file.
 * Created by sahil on 6/25/16.
 */
public class SpecParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecParser.class);

    private final Path specFilePath;

    public SpecParser(final Path specFilePath) {
        this.specFilePath = specFilePath;
    }

    public List<SpecColumnDescriptor> read() {
        final List<SpecColumnDescriptor> descriptors = new ArrayList<>();

        //assumes only 3 columns
        int columnNameIndex = -1;
        int columnWidthIndex = -1;
        int columnDataTypeIndex = -1;

        LOGGER.info("Reading spec file: " + this.specFilePath.toAbsolutePath().toString());
        try (final BufferedReader br = new BufferedReader(new FileReader(this.specFilePath.toAbsolutePath().toString()))) {
            String firstLine = null;
            String line;
            while ((line = br.readLine()) != null) {
                //first line contains description of columns
                if (firstLine == null) {
                    firstLine = line;
                    LOGGER.info("Interpreting first line of spec: " + firstLine);
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
                    LOGGER.info("Indexes of columns " +
                            "Name=" + columnNameIndex +
                            ", Width=" + columnWidthIndex +
                            ", Type=" + columnDataTypeIndex);
                }
                //every other line contains the actual columns
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
            LOGGER.error("Error parsing spec", e);
        }

        LOGGER.info("Column descriptors: " + descriptors);
        return descriptors;
    }
}
