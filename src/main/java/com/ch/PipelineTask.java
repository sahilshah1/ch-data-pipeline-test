package com.ch;

import com.ch.parser.DataParser;
import com.ch.parser.DataRow;
import com.ch.parser.SpecColumnDescriptor;
import com.ch.parser.SpecParser;
import com.ch.persistence.PersistenceClient;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by sahil on 6/25/16.
 */
public class PipelineTask
    implements Callable<Void> {

    private final Path specPath;
    private final Set<Path> dataPaths;
    private final PersistenceClient client;

    public PipelineTask(final Path specPath, final Set<Path> dataPaths, final PersistenceClient client) {
        this.specPath = specPath;
        this.dataPaths = dataPaths;
        this.client = client;
    }


    @Override
    public Void call()
            throws PersistenceClient.PersistenceException {

        //read the spec to figure out how to structure the table, then create the table
        final List<SpecColumnDescriptor> columnDescriptors = new SpecParser(this.specPath).read();
        final String tableName = getSpecName(); //name of table will be name of spec
        this.client.createTable(tableName, columnDescriptors);

        //loop through each of the data files for this spec and add them to db
        for (final Path dataPath : this.dataPaths) {
            final DataParser dataParser = new DataParser(dataPath, columnDescriptors);
            final List<DataRow> dataRows = dataParser.read();

            for (final DataRow dataRow : dataRows) {
                this.client.insertRecord(tableName, dataRow);
            }
        }

        return null;
    }

    private String getSpecName() {
        return StringUtils.substringBefore(this.specPath.getFileName().toString(), ".csv");
    }
}
