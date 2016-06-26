package com.ch;

import com.ch.input.DataFile;
import com.ch.input.SpecFile;
import com.ch.persistence.MySqlClient;
import com.ch.persistence.PersistenceClient;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sahil on 6/25/16.
 */
public class Pipeline {

    public static void main(final String[] args)
            throws PersistenceClient.PersistenceException, IOException, ExecutionException, InterruptedException {
        configureSlf4j();
        final Logger logger = LoggerFactory.getLogger(Pipeline.class);

        final String targetDatabaseName = args[0];
        logger.info("Target database (will be created if nonexistent: " + targetDatabaseName);

        final Path specFileDir = Paths.get(args[1]);
        logger.info("Spec File Dir: " + specFileDir);

        final Path dataFileDir = Paths.get(args[2]);
        logger.info("Data File Dir: " + dataFileDir);

        final List<SpecFile> specFiles = SpecFile.getSpecFiles(specFileDir);
        final Map<String, Set<DataFile>> dataFileMap = DataFile.getDataFiles(dataFileDir);

        final ExecutorService pipelineTaskThreadPool = Executors.newCachedThreadPool();
        final ExecutorService writeThreadPool = Executors.newCachedThreadPool();

        final List<PipelineTask> tasks = new LinkedList<>();
        for (final SpecFile specFile : specFiles) {
            logger.info("Spawning task for " + specFile);
            tasks.add(new PipelineTask(specFile,
                    dataFileMap.get(specFile.getSpecName()),
                    MySqlClient.newClient(targetDatabaseName),
                    writeThreadPool));
        }
        pipelineTaskThreadPool.invokeAll(tasks); //blocks until all tasks finished
        pipelineTaskThreadPool.shutdown();
    }

    private static void configureSlf4j() {
        PropertyConfigurator.configure(Pipeline.class.getResource("log4j.properties").getPath());
    }

}
