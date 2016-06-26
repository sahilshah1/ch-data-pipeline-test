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
    private final SpecDataType dataType;
    private final String value;

    public DataRowColumnValue(final String columnName, final SpecDataType dataType, final String value) {
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

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Column Name: ").append(this.columnName).append(", ");
        builder.append("DataType: ").append(this.dataType).append(", ");
        builder.append("Value ").append(String.valueOf(this.value)).append("\n");
        return builder.toString();
    }

    public String getColumnName() {
        return this.columnName;
    }

    public String getValue() {
        return this.value;
    }
}
