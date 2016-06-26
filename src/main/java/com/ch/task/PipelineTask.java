package com.ch.task;

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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Task that executes parsers and perform insertions
 * into persistence.
 * Created by sahil on 6/25/16.
 */
public class PipelineTask
    implements Callable<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PipelineTask.class);
    private static final int BATCH_SIZE = 1000; //accumulates this many rows before spawning a write task

    private final SpecFile specFile;
    private final Set<DataFile> dataFiles;
    private final PersistenceClient client;
    private final ExecutorService dataFileTaskPool;
    private final ExecutorService writePool;

    public PipelineTask(final SpecFile specFile,
                        final Set<DataFile> dataFiles,
                        final PersistenceClient client,
                        final ExecutorService dataFileTaskPool,
                        final ExecutorService writePool) {
        this.specFile = specFile;
        this.dataFiles = dataFiles;
        this.client = client;
        this.dataFileTaskPool = dataFileTaskPool;
        this.writePool = writePool;
    }


    @Override
    public Void call()
            throws PersistenceClient.PersistenceException {
        //read the spec to figure out how to structure the table, then create the table
        final List<SpecColumnDescriptor> columnDescriptors = new SpecParser(this.specFile.getPath()).read();
        this.client.createTable(this.specFile.getSpecName(), columnDescriptors);

        //loop through each of the data files for this spec and add them to db
        //TODO: have these parsing tasks consume files from a queue so file drops can be handled
        this.dataFiles.forEach(dataFile -> {
            LOGGER.info("Spawning data parsing task for " + dataFile);
            //TODO: actually have this spawn tasks. the main process can't exit until
            //the threadpool is shut down, but then the writePool can't be shutdown
            //because it is not known whether all the write tasks have been submitted
            newDataFileTask(dataFile, columnDescriptors).run();
        });

        this.dataFileTaskPool.shutdown();
        this.writePool.shutdown();
        return null;
    }

    private Runnable newDataFileTask(final DataFile dataFile,
                                     final List<SpecColumnDescriptor> columnDescriptors) {
        return () -> {
                final DataParser dataParser = new DataParser(dataFile.getPath(), columnDescriptors);

                //avoid reading all rows into memory, and do writes in a separate thread
                //to avoid blocking
                final List<DataRow> batch = new LinkedList<>();
                dataParser.read(dataRow -> {
                    batch.add(dataRow);

                    if (batch.size() >= BATCH_SIZE) {
                        final List<DataRow> submissionBatch = Collections.unmodifiableList(new ArrayList<>(batch));
                        this.writePool.submit(newPersistenceWriter(submissionBatch));
                        batch.clear();
                    }
                });

                //write any leftover rows
                if (batch.size() > 0) {
                    this.writePool.submit(newPersistenceWriter(batch));
                }
            };
    }

    private Runnable newPersistenceWriter(final List<DataRow> batch) {
        return () -> {
            try {
                PipelineTask.this.client.insertRecords(PipelineTask.this.specFile.getSpecName(), batch);
            }
            catch (final PersistenceClient.PersistenceException e) {
                LOGGER.error("Error trying to submit batch " + batch, e);
            }
        };
    }
}
