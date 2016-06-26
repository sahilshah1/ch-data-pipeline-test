package com.ch;

import com.ch.input.DataFile;
import com.ch.input.SpecFile;
import com.ch.persistence.MySqlClient;
import com.ch.persistence.PersistenceClient;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by sahil on 6/25/16.
 */
public class Pipeline {

    private static final String SPEC_FILE_DIR_ARG = "-specFileDir";
    private static final String DATA_FILE_DIR_ARG = "-dataFileDir";

    private static final String SPECS_DIR = "/Users/sahil/clover/specs/testformat1.csv";
    private static final String DATA_DIR = "/Users/sahil/clover/data/testformat1_2015-06-28.txt";


    public static void main(final String[] args)
            throws PersistenceClient.PersistenceException, IOException, ExecutionException, InterruptedException {

        final Path specFileDir = Paths.get(args[0]);
        final Path dataFileDir = Paths.get(args[1]);

        final List<SpecFile> specFiles = SpecFile.getSpecFiles(specFileDir);
        final Map<String, Set<DataFile>> dataFileMap = DataFile.getDataFiles(dataFileDir);

        final ExecutorService threadPool = Executors.newCachedThreadPool();

        final List<PipelineTask> tasks = new LinkedList<>();
        for (final SpecFile specFile : specFiles) {
            tasks.add(new PipelineTask(specFile,
                    dataFileMap.get(specFile.getSpecName()),
                    MySqlClient.newClient("test")));
        }
        threadPool.invokeAll(tasks); //blocks until all tasks finished
        threadPool.shutdown();
    }

}
