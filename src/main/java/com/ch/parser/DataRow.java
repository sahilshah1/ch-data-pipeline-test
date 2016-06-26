package com.ch.parser;

import java.util.List;

/**
 * Model for 1 row of data. Contains an ordered list of {@link DataRowColumnValue}
 * instances.
 * Created by sahil on 6/25/16.
 */
class DataRow {
    private final List<DataRowColumnValue> dataRowColumnValues;

    DataRow(final List<DataRowColumnValue> dataRowColumnValues) {
        this.dataRowColumnValues = dataRowColumnValues;
    }

    List<DataRowColumnValue> getDataRowColumnValues() {
        return this.dataRowColumnValues;
    }
}
