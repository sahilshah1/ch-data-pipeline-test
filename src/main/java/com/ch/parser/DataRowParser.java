package com.ch.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * parses 1 row of data at a time according to the column descriptors
 */
public class DataRowParser {

    private final List<SpecColumnDescriptor> specColumnDescriptors;

    DataRowParser(final List<SpecColumnDescriptor> specColumnDescriptors) {
        this.specColumnDescriptors = specColumnDescriptors;
    }

    List<DataRowColumnValue> parseRow(final String csv) {
        final List<DataRowColumnValue> values = new ArrayList<>(this.specColumnDescriptors.size());

        int cursor = 0;
        for (final SpecColumnDescriptor descriptor : this.specColumnDescriptors) {
            final String columnVal = csv.substring(cursor, cursor + descriptor.getWidth());
            values.add(new DataRowColumnValue(descriptor.getColumnName(), descriptor.getDataType(), columnVal.trim()));
            cursor += descriptor.getWidth();
        }
        return values;
    }
}
