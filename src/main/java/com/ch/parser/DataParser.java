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
    private final SpecParser.DataRowParser dataRowParser;

    public DataParser(final Path dataFilePath, final SpecParser.DataRowParser dataRowParser) {
        this.dataFilePath = dataFilePath;
        this.dataRowParser = dataRowParser;
    }

    public List<SpecParser.DataRow> read() {
        final List<SpecParser.DataRow> rows = new LinkedList<>();

        try (final BufferedReader br = new BufferedReader(new FileReader(this.dataFilePath.toAbsolutePath().toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(this.dataRowParser.parseCsv(line));
            }
        }
        catch (final IOException e) {
            System.out.println(e.toString());
        }

        return rows;
    }
}
