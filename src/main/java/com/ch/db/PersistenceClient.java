package com.ch.db;

import com.ch.parser.DataRowColumnValue;
import com.ch.parser.SpecColumnDescriptor;

import java.util.List;

/**
 * Created by sahil on 6/25/16.
 */
public interface PersistenceClient {

    void createTable(String tableName, List<SpecColumnDescriptor> columnDescriptors)
            throws PersistenceException;

    void insertRecord(String tableName, List<DataRowColumnValue> dataRowColumnValues)
            throws PersistenceException;

    class PersistenceException
            extends Exception {
        PersistenceException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
