package com.ch.persistence;

import com.ch.parser.DataRow;
import com.ch.parser.DataRowColumnValue;
import com.ch.parser.SpecColumnDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Maintains a connection to a specific MySql database.
 * Created by sahil on 6/25/16.
 */
public class MySqlClient
        implements PersistenceClient {
    final Logger LOG = LoggerFactory.getLogger(MySqlClient.class);

    private final String databaseName;
    private final Connection connection;

    private MySqlClient(final String databaseName)
            throws PersistenceException {
        this.databaseName = databaseName;

        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=&serverTimezone=UTC");
            LOG.info("Creating MySql client for database " + databaseName);
            final Statement s = this.connection.createStatement();
            s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + this.databaseName + ";");
            s.execute("USE " + this.databaseName);
        }
        catch (final SQLException e) {
            throw new PersistenceException("Error trying to initialize client", e);
        }
    }

    public static MySqlClient newClient(final String databaseName) throws PersistenceException {
        return new MySqlClient(databaseName);
    }

    @Override
    public void createTable(final String tableName, final List<SpecColumnDescriptor> columnDescriptors)
            throws PersistenceException {
        final StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" ");
        query.append("(");
        for (final SpecColumnDescriptor columnDescriptor : columnDescriptors) {
            query.append(columnDescriptor.getColumnName()).append(" ");
            //TODO: USE OOP, change BOOLEAN TO BOOLEAN slql AND TEXT to TEXT sql
            switch (columnDescriptor.getDataType()) {
                case INTEGER:
                    query.append("INTEGER");
                    break;
                case BOOLEAN:
                    //represented as string literals "True" and "False" according to spec
                    query.append("VARCHAR(").append(5).append(")");
                    break;
                case TEXT:
                    query.append("VARCHAR(").append(columnDescriptor.getWidth()).append(")");
                    break;
            }
            query.append(",");
        }
        query.deleteCharAt(query.length() - 1); //remove last comma
        query.append(")");

        try {
            LOG.info("Creating table " + tableName + " in database " + this.databaseName);
            LOG.debug("Query: " + query);

            final Statement statement = this.connection.createStatement();
            statement.executeUpdate(query.toString());
        }
        catch (final SQLException e) {
            throw new PersistenceException("Error trying to set up table", e);
        }

    }

    @Override
    public void insertRecords(final String tableName, final List<DataRow> dataRows)
            throws PersistenceException {
        //TODO: duplicate record error checking??
        final StringBuilder query = new StringBuilder();

        //build part of query listing all the columns
        query.append("INSERT INTO ").append(tableName).append(" (");
        final String columnNames = dataRows.get(0).getDataRowColumnValues().stream()
                .map(DataRowColumnValue::getColumnName)
                .reduce((a,b) -> a + "," + b)
                .get();
        query.append(columnNames).append(") ");

        query.append("VALUES ");
        for (final DataRow row : dataRows) {
            query.append("(");

            final String values = row.getDataRowColumnValues().stream()
                    .map(cell -> "'" + cell.getValue() + "'")
                    .reduce((a, b) -> a + "," + b)
                    .get();
            query.append(values).append("),");
        }
        query.deleteCharAt(query.length() - 1); //remove last comma

        try {
            LOG.debug("Inserting records into " + tableName + " in database " + this.databaseName);
            final String queryId = UUID.randomUUID().toString();
            LOG.debug("Query (queryId=" + queryId + "): " + query);

            final Instant start = Instant.now();
            final Statement statement = this.connection.createStatement();
            statement.executeUpdate(query.toString());
            LOG.debug("Query " + queryId + " executed in " + Duration.between(start, Instant.now()).toMillis() + "ms");

        }
        catch (final SQLException e) {
            throw new PersistenceException("Error trying to enter record", e);
        }
    }
}
