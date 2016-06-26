package com.ch;

import com.ch.input.DataFile;
import com.ch.input.SpecFile;
import com.ch.parser.DataParser;
import com.ch.parser.DataRow;
import com.ch.parser.SpecColumnDescriptor;
import com.ch.parser.SpecParser;
import com.ch.persistence.PersistenceClient;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by sahil on 6/25/16.
 */
public class PipelineTask
    implements Callable<Void> {

    private final SpecFile specFile;
    private final Set<DataFile> dataFiles;
    private final PersistenceClient client;

    public PipelineTask(final SpecFile specFile,
                        final Set<DataFile> dataFiles,
                        final PersistenceClient client) {
        this.specFile = specFile;
        this.dataFiles = dataFiles;
        this.client = client;
    }


    @Override
    public Void call()
            throws PersistenceClient.PersistenceException {

        //read the spec to figure out how to structure the table, then create the table
        final List<SpecColumnDescriptor> columnDescriptors = new SpecParser(this.specFile.getPath()).read();
        this.client.createTable(this.specFile.getSpecName(), columnDescriptors);

        //loop through each of the data files for this spec and add them to db
        for (final DataFile dataFile : this.dataFiles) {
            final DataParser dataParser = new DataParser(dataFile.getPath(), columnDescriptors);
            //TODO: all rows are read into memory, blocking all transactions,
            //TODO: pipeline these so they can be inserted in parallel
            final List<DataRow> dataRows = dataParser.read();

            for (final DataRow dataRow : dataRows) {
                this.client.insertRecord(this.specFile.getSpecName(), dataRow);
            }
        }

        return null;
    }
}
