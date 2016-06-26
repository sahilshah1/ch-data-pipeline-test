package com.ch.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Parses a data file according to some spec.
 *
 * Created by sahil on 6/25/16.
 */
public class DataParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataParser.class);

    private final Path dataFilePath;
    private final List<SpecColumnDescriptor> specColumnDescriptors;

    public DataParser(final Path dataFilePath,
                      final List<SpecColumnDescriptor> specColumnDescriptors) {
        this.dataFilePath = dataFilePath;
        this.specColumnDescriptors = specColumnDescriptors;
    }

    public void read(final Consumer<DataRow> dataRowConsumer) {
        LOGGER.info("Reading data file: " + this.dataFilePath.toAbsolutePath().toString());
        try (final BufferedReader br =
                     new BufferedReader(new FileReader(this.dataFilePath.toAbsolutePath().toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                dataRowConsumer.accept(new DataRow(parseRow(line)));
            }
        }
        catch (final IOException e) {
            LOGGER.error("Error parsing data", e);
        }
    }

    private List<DataRowColumnValue> parseRow(final String rowString) {
        final List<DataRowColumnValue> values = new ArrayList<>(this.specColumnDescriptors.size());

        int cursor = 0;
        for (final SpecColumnDescriptor descriptor : this.specColumnDescriptors) {
            final String columnVal = rowString.substring(cursor, cursor + descriptor.getWidth());
            values.add(new DataRowColumnValue(descriptor.getColumnName(), descriptor.getDataType(), columnVal.trim()));
            cursor += descriptor.getWidth();
        }
        return values;
    }
}
