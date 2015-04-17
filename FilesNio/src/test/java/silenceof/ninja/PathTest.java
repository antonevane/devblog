package silenceof.ninja;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * @author silenceof.ninja
 */
public class PathTest {

    @Test
    public void testCurentDirectory() {
        // Get absolute path for a current directory
        // New API
        Path userHomePath = Paths.get("").toAbsolutePath();
        // Old File API
        File file = new File("").getAbsoluteFile();

        assertEquals(file.toPath(), userHomePath);
    }

    @Test
    public void testPathConstructor() {
        // This returns "\" on Windows and "/" on Unix.
        String fileSeparator = System.getProperty("file.separator");

        // Old File API
        String pathToTests = "src" + fileSeparator + "test";
        File file = new File(pathToTests).getAbsoluteFile();

        // New Api approach
        Path testsPath = Paths.get("src", "test").toAbsolutePath();

        assertEquals(file.toPath(), testsPath);
    }
}
