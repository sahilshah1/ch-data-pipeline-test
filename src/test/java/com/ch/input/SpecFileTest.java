package com.ch.input;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sahil on 6/25/16.
 */
public class SpecFileTest {

    @Test
    public void testCollectSpecFiles()
            throws IOException {
        final Path pathToSpec = Paths.get(SpecFileTest.class.getResource("specFileCollectorTest.csv").getPath());
        final Path pathToInvalidSpec = Paths.get(SpecFileTest.class.getResource("NOT_VALID.notcsv").getPath());
        final Path pathToSpecDir = pathToSpec.getParent();

        final List<SpecFile> specPaths = SpecFile.getSpecFiles(pathToSpecDir);

        assertEquals(1, specPaths.size());
        assertEquals(new SpecFile(pathToSpec), specPaths.get(0));
    }

}
