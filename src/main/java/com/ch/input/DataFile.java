package com.ch.input;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Represents a data file for a spec.
 * Created by sahil on 6/25/16.
 */
public class DataFile {
    private final Path path;

    public DataFile(final Path path) {
        if (!isValidDataFile(path)) {
            throw new IllegalArgumentException("Not a valid data file:" + path.toString());
        }
        this.path = path;
    }

    public String forSpecName() {
        return StringUtils.substringBefore(this.path.getFileName().toString(), "_");
    }

    public Path getPath() {
        return this.path;
    }

    private static boolean isValidDataFile(final Path path) {
        //matches fileformat1_2007-10-01.txt
        return path.getFileName().toString().matches(".*_\\d\\d\\d\\d-\\d\\d-\\d\\d\\.txt$");
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof DataFile && this.path.equals(((DataFile) o).path);
    }

    @Override
    public int hashCode() {
        return this.path.hashCode();
    }

    /**
     * Visits a directory to search for data files in the format
     * specname_YYYY-MM-DD.txt
     * @param dataDirPath path to begin searching from
     * @return Map of specname to the Set of data files with names that correspond to the specname
     * @throws IOException
     */
    public static Map<String, Set<DataFile>> getDataFiles(final Path dataDirPath)
            throws IOException {
        final Map<String, Set<DataFile>> paths = new HashMap<>();
        Files.walkFileTree(dataDirPath, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                    throws IOException {
                if (!isValidDataFile(file)) {
                    return FileVisitResult.CONTINUE;
                }


                final DataFile dataFile = new DataFile(file);
                if (paths.containsKey(dataFile.forSpecName())) {
                    paths.get(dataFile.forSpecName()).add(dataFile);
                }
                else {
                    final Set<DataFile> newFileSet = new HashSet<>();
                    newFileSet.add(dataFile);
                    paths.put(dataFile.forSpecName(), newFileSet);
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
}
