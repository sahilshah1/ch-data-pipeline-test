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

    public DataRowParser read() {
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
                            DataType.valueOf(values[columnDataTypeIndex])));
                }


            }
        }
        catch (final IOException e) {
            System.out.println(e.toString());
        }

        return new DataRowParser(descriptors);
    }

    /**
     * parses 1 CSV String at a time according to the column descriptors
     */
    public static class DataRowParser {

        private final List<SpecColumnDescriptor> specColumnDescriptors;

        public DataRowParser(final List<SpecColumnDescriptor> specColumnDescriptors) {
            this.specColumnDescriptors = specColumnDescriptors;
        }

        public List<SpecColumnDescriptor> getSpecColumnDescriptors() {
            return this.specColumnDescriptors;
        }

        public DataRow parseCsv(final String csv) {
            final List<DataColumnValue> values = new ArrayList<>(this.specColumnDescriptors.size());

            int cursor = 0;
            for (final SpecColumnDescriptor descriptor : this.specColumnDescriptors) {
                final String columnVal = csv.substring(cursor, cursor + descriptor.getWidth());
                values.add(new DataColumnValue(descriptor.getColumnName(), descriptor.getDataType(), columnVal.trim()));
                cursor += descriptor.getWidth();
            }
            return new DataRow(values);
        }
    }

    public static class DataRow {
        private final List<DataColumnValue> dataColumnValues;

        public DataRow(final List<DataColumnValue> dataColumnValues) {
            this.dataColumnValues = dataColumnValues;
        }

        public List<DataColumnValue> getDataColumnValues() {
            return this.dataColumnValues;
        }
    }


    public static class DataColumnValue {
        private final String columnName;
        private final DataType dataType;
        private final String value;

        public DataColumnValue(final String columnName, final DataType dataType, final String value) {
            this.columnName = columnName;
            this.dataType = dataType;
            this.value = value;
        }

        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof DataColumnValue)){
                return false;
            }

            final DataColumnValue o = (DataColumnValue) other;
            return this.columnName.equals(o.columnName) &&
                    this.dataType == o.dataType &&
                    this.value.equals(o.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.columnName, this.dataType, this.value);
        }
    }

    public static class SpecColumnDescriptor {
        private final String columnName;
        private final int width;
        private final DataType dataType;

        SpecColumnDescriptor(final String columnName, final int width, final DataType dataType) {
            this.columnName = columnName;
            this.width = width;
            this.dataType = dataType;
        }

        public String getColumnName() {
            return this.columnName;
        }

        public int getWidth() {
            return this.width;
        }

        public DataType getDataType() {
            return this.dataType;
        }

        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof SpecColumnDescriptor)) {
                return false;
            }
            final SpecColumnDescriptor other = (SpecColumnDescriptor) o;
            return this.columnName.equals(other.columnName) &&
                    this.width == other.width &&
                    this.dataType == other.dataType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.columnName, this.width, this.dataType);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Column Name: ").append(this.columnName).append("\n");
            builder.append("Width ").append(String.valueOf(this.width)).append("\n");
            builder.append("DataType: ").append(this.dataType).append("\n");
            return builder.toString();
        }
    }

    public enum DataType {
        TEXT,
        BOOLEAN,
        INTEGER;

        private static final Map<String, DataType> NAME_TO_VALUE = new HashMap<>();
        static {
            Arrays.stream(DataType.values()).forEach(e -> NAME_TO_VALUE.put(e.toString(), e));
        }

        public static DataType fromString(final String name) {
            return NAME_TO_VALUE.get(name);
        }

    }
}
