package com.ch.input;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by sahil on 6/25/16.
 */
public class DataFileTest {

    @Test
    public void testCollectDataFiles()
            throws IOException {
        final Path pathToData = Paths.get(DataFileTest.class.getResource("specFileCollectorTest_2016-06-25.txt").getPath());
        final Path pathToDataDir = pathToData.getParent();

        final Map<String, Set<DataFile>> dataPaths = DataFile.getDataFiles(pathToDataDir);

        assertEquals(1, dataPaths.get("specFileCollectorTest").size());
        assertTrue(dataPaths.get("specFileCollectorTest").contains(new DataFile(pathToData)));
    }
}
