package com.ch.input;

import com.ch.Pipeline;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        final List<Path> specPaths = SpecFile.getSpecFiles(pathToSpecDir);

        assertEquals(1, specPaths.size());
        assertTrue(specPaths.contains(pathToSpec));
        assertFalse(specPaths.contains(pathToInvalidSpec));
    }

}
