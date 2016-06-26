package com.ch.input;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

/**
 * Object that represents a spec.
 * Created by sahil on 6/25/16.
 */
public class SpecFile {
    private final Path path;

    public SpecFile(final Path path) {
        if (!isValidSpecFile(path)) {
            throw new IllegalArgumentException("Not a valid spec file of format .*.csv" + path.toString());
        }
        this.path = path;
    }

    public Path getPath() {
        return this.path;
    }

    public String getSpecName() {
        return StringUtils.substringBefore(this.path.getFileName().toString(), ".csv");
    }

    public static boolean isValidSpecFile(final Path path) {
        return path.getFileName().toString().matches(".*\\.csv");
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof SpecFile && this.path.equals(((SpecFile) o).path);
    }

    @Override
    public int hashCode() {
        return this.path.hashCode();
    }

    @Override
    public String toString() {
        return this.path.toAbsolutePath().toString();
    }

    public static List<SpecFile> getSpecFiles(final Path specFileDir)
            throws IOException {
        final List<SpecFile> paths = new LinkedList<>();
        Files.walkFileTree(specFileDir, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                    throws IOException {
                if (isValidSpecFile(file)) {
                    paths.add(new SpecFile(file));
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
