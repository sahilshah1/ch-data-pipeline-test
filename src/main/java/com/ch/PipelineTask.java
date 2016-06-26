package com.ch;

import com.ch.input.DataFile;
import com.ch.input.SpecFile;
import com.ch.parser.DataParser;
import com.ch.parser.DataRow;
import com.ch.parser.SpecColumnDescriptor;
import com.ch.parser.SpecParser;
import com.ch.persistence.PersistenceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Task that executes parsers and perform insertions
 * into persistence.
 * Created by sahil on 6/25/16.
 */
public class PipelineTask
    implements Callable<Void> {

    private static final int BATCH_SIZE = 20; //accumulates this many rows before spawning a write task
    private static final Logger LOGGER = LoggerFactory.getLogger(PipelineTask.class);

    private final SpecFile specFile;
    private final Set<DataFile> dataFiles;
    private final PersistenceClient client;
    private final ExecutorService threadPool;

    public PipelineTask(final SpecFile specFile,
                        final Set<DataFile> dataFiles,
                        final PersistenceClient client,
                        final ExecutorService threadPool) {
        this.specFile = specFile;
        this.dataFiles = dataFiles;
        this.client = client;
        this.threadPool = threadPool;
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

            //avoid reading all rows into memory, and do writes in a separate thread
            //to avoid blocking
            final List<DataRow> batch = new LinkedList<>();
            dataParser.read(dataRow -> {
                batch.add(dataRow);
                if (batch.size() >= BATCH_SIZE) {
                    this.threadPool.submit(new PersistenceWriter(batch));
                    batch.clear();
                }
            });
        }

        this.threadPool.shutdown();
        return null;
    }

    /**
     * Task that writes to the database.
     */
    private class PersistenceWriter
            implements Runnable {

        private final List<DataRow> batch;

        private PersistenceWriter(final List<DataRow> batch) {
            this.batch = new ArrayList<>(batch);
        }

        @Override
        public void run() {
            try {
                PipelineTask.this.client.insertRecords(PipelineTask.this.specFile.getSpecName(), this.batch);
            }
            catch (final PersistenceClient.PersistenceException e) {
                LOGGER.error("Error trying to submit batch " + this.batch, e);
            }
        }
    }
}
