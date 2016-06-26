package com.ch.parser;

import java.util.Objects;

/**
 * Describes a column extracted from a spec.
 * 1 descriptor is equivalent to 1 row in the spec file.
 * Created by sahil on 6/25/16.
 */
public class SpecColumnDescriptor {
    private final String columnName;
    private final int width;
    private final SpecDataType dataType;

    public SpecColumnDescriptor(final String columnName, final int width, final SpecDataType dataType) {
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

    public SpecDataType getDataType() {
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
        builder.append("SpecDataType: ").append(this.dataType).append("\n");
        return builder.toString();
    }
}
