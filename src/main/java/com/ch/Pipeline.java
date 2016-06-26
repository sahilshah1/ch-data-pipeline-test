package com.ch;

import com.ch.persistence.MySqlClient;
import com.ch.persistence.PersistenceClient;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sahil on 6/25/16.
 */
public class Pipeline {

    private static final String SPECS_DIR = "/Users/sahil/clover/specs/testformat1.csv";
    private static final String DATA_DIR = "/Users/sahil/clover/data/testformat1_2015-06-28.txt";


    public static void main(final String[] args)
            throws PersistenceClient.PersistenceException {
        final PipelineTask task = new PipelineTask(Paths.get(SPECS_DIR),
                Collections.singleton(Paths.get(DATA_DIR)),
                MySqlClient.newClient("test"));
        task.call();
    }

    public List<Path> getSpecs(final Path path)
            throws IOException {
        final List<Path> paths = new LinkedList<>();
        Files.walkFileTree(path, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                    throws IOException {
                if (file.getFileName().toString().matches(".*\\.csv")) {
                    paths.add(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(final Path file, final IOException exc)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        return paths;
    }

    public List<Path> getDataFiles(final Path dataPath)
            throws IOException {
        final List<Path> paths = new LinkedList<>();
        Files.walkFileTree(dataPath, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                    throws IOException {
                paths.add(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(final Path file, final IOException exc)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        return paths;
    }
}
