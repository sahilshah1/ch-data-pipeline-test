package com.ch.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Parses a data file according to some spec.
 * Created by sahil on 6/25/16.
 */
public class DataParser {

    private final Path dataFilePath;
    private final DataRowParser dataRowParser;

    public DataParser(final Path dataFilePath, final List<SpecColumnDescriptor> specColumnDescriptors) {
        this.dataFilePath = dataFilePath;
        this.dataRowParser = new DataRowParser(specColumnDescriptors);
    }

    public List<DataRow> read() {
        final List<DataRow> rows = new LinkedList<>();

        try (final BufferedReader br =
                     new BufferedReader(new FileReader(this.dataFilePath.toAbsolutePath().toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(new DataRow(this.dataRowParser.parseRow(line)));
            }
        }
        catch (final IOException e) {
            System.out.println(e.toString());
        }

        return rows;
    }
}
