package com.ch;

import com.ch.input.DataFile;
import com.ch.input.SpecFile;
import com.ch.persistence.MySqlClient;
import com.ch.persistence.PersistenceClient;
import com.ch.task.PipelineTask;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class that runs the pipeline.
 * Created by sahil on 6/25/16.
 */
public class Pipeline {

    public static void main(final String[] args)
            throws PersistenceClient.PersistenceException, IOException, ExecutionException, InterruptedException {
        configureSlf4j();
        final Logger logger = LoggerFactory.getLogger(Pipeline.class);

        if (args.length < 3) {
            logger.info("Usage: ");
            logger.info("java -jar <name of jar> <db_name> <spec_file_absolute_path> <data_file_absolute_path>");
            System.exit(0);
        }

        //log parameters
        final String targetDatabaseName = args[0];
        logger.info("Target database (will be created if nonexistent): " + targetDatabaseName);
        final Path specFileDir = Paths.get(args[1]);
        logger.info("Spec File Dir: " + specFileDir);
        final Path dataFileDir = Paths.get(args[2]);
        logger.info("Data File Dir: " + dataFileDir);

        //collect specs and data to execute
        final List<SpecFile> specFiles = SpecFile.getSpecFiles(specFileDir);
        final Map<String, Set<DataFile>> dataFileMap = DataFile.getDataFiles(dataFileDir);

        final ExecutorService pipelineTaskThreadPool = Executors.newCachedThreadPool();
        final ExecutorService writeThreadPool = Executors.newCachedThreadPool();
        final ExecutorService dataFileTaskThreadPool = Executors.newCachedThreadPool();

        //spawn workers
        final List<PipelineTask> tasks = new LinkedList<>();
        for (final SpecFile specFile : specFiles) {
            logger.info("Spawning task for " + specFile);
            tasks.add(new PipelineTask(specFile,
                    dataFileMap.get(specFile.getSpecName()),
                    MySqlClient.newClient(targetDatabaseName),
                    dataFileTaskThreadPool,
                    writeThreadPool));
        }
        pipelineTaskThreadPool.invokeAll(tasks); //blocks until all tasks finished
        pipelineTaskThreadPool.shutdown();
    }

    private static void configureSlf4j()
            throws IOException {
        final Properties properties = new Properties();
        try (final InputStream inputStream =Pipeline.class.getResourceAsStream("log4j.properties")) {
            properties.load(inputStream); //stream will remain open afterwards, so must be closed manually
        }
        PropertyConfigurator.configure(properties);
    }

}
