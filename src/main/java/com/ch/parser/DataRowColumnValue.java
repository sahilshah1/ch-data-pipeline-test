package com.ch.parser;

import java.util.Objects;

/**
 * Describes a cell: a singular column in a singular row of data.
 * Each instance contains the name of its column, data type,
 * and the actual value.
 *
 * Created by sahil on 6/25/16.
 */
public class DataRowColumnValue {
    private final String columnName;
    private final DataType dataType;
    private final String value;

    public DataRowColumnValue(final String columnName, final DataType dataType, final String value) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.value = value;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof DataRowColumnValue)) {
            return false;
        }

        final DataRowColumnValue o = (DataRowColumnValue) other;
        return this.columnName.equals(o.columnName) &&
                this.dataType == o.dataType &&
                this.value.equals(o.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.columnName, this.dataType, this.value);
    }

    public String getColumnName() {
        return this.columnName;
    }

    public String getValue() {
        return this.value;
    }
}
