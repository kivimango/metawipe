package com.kivimango.metawipe.service;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public final class ExifEraserServiceImplTest {

    private final String currentdir = this.getClass().getClassLoader().getResource("").getFile();
    private final ExifEraserServiceImpl service = new ExifEraserServiceImpl();
    private final File original = new File(getClass().getClassLoader().getResource("picture.jpg").getFile());
    private File testFile;

    /**
     * Making a copy of a test file with existing exif data to able to run the unit tests anytime
     * (unit tests deletes exif data).
     * Test are performed on the copied file, then bets removed after the test
     */

    @Before
    public void setup() throws IOException {
        testFile = new File(service.makeCopy(original));
        Files.deleteIfExists(testFile.toPath());
        Files.copy(Paths.get(original.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));
    }

    @After
    public void teardown() throws IOException {
        Files.deleteIfExists(testFile.toPath());
        testFile = null;
    }

    @Test
    public void testMakeCopyShouldCopyFileAndRename() throws IOException {
        File copiedFile = new File(service.makeCopy(original));
        assertTrue(copiedFile.exists());
        assertThat(copiedFile.getName(), containsString("_copy"));
        // clean up after test
        Files.delete(copiedFile.toPath());
    }

    @Test
    public void testFile() throws ImageWriteException, NotAFileException, ImageReadException, IOException {
        assertTrue(service.file(testFile));
    }

    @Test(expected = NotAFileException.class)
    public void testNotAFileShouldThrowExceptionOnDirectories() throws ImageWriteException, NotAFileException, ImageReadException, IOException {
       service.file(new File("/"));
    }

    @Test(expected = ImageReadException.class)
    public void testNotSupportedFileShouldThrowException() throws ImageWriteException, NotAFileException, ImageReadException, IOException {
        File textFile = new File(this.getClass().getClassLoader().getResource("not-supported.txt").getFile());
        service.file(textFile);
    }

    @Test(expected = ImageReadException.class)
    public void testFakeImageFileShouldThrowException() throws ImageWriteException, NotAFileException, ImageReadException, IOException {
        File fakeJpeg = new File(this.getClass().getClassLoader().getResource("fake-image.jpg").getFile());
        service.file(fakeJpeg);
    }

    @Test
    public void testDirShouldRecursivelyDeleteExifDataInSupportedFiles() throws ImageWriteException, NotAFileException, ImageReadException, IOException {
        Path pathToTestDir = Paths.get(currentdir + "//test-dir-examples//test-dirrrrr//");
        // Delete temporary files from the previous unit test
        if(Files.exists(pathToTestDir)) {
            deleteDirectoryWithFilesRecursively(pathToTestDir);
        }

        // Creating a temporary dir to test against
        Files.createDirectory(pathToTestDir);

        File testFile1 = new File(currentdir + "//test-dir-examples//769474.jpg");
        File testFile2 = new File(currentdir + "//test-dir-examples//foto_exif.jpeg");
        File testFile3 = new File(currentdir + "/test-dir-examples//original2.jpg");

        // Copying test files into the temp dir
        CopyOption[] options = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES};
        Path copyOfTestFile1 = Files.copy(testFile1.toPath(), Paths.get(pathToTestDir.toString() + "//769474.jpg"), options);
        Path copyOfTestFile2 = Files.copy(testFile2.toPath(), Paths.get(pathToTestDir.toString() + "//foto_exif.jpeg"), options);
        Path copyOfTestFile3 = Files.copy(testFile3.toPath(), Paths.get(pathToTestDir.toString() + "//original2.jpg"), options);

        service.directory(pathToTestDir.toFile());

        assertTrue(service.checkExifDeleted(copyOfTestFile1.toFile()));
        assertTrue(service.checkExifDeleted(copyOfTestFile2.toFile()));
        assertTrue(service.checkExifDeleted(copyOfTestFile3.toFile()));

        //clean up
        Files.deleteIfExists(copyOfTestFile1);
        Files.deleteIfExists(copyOfTestFile2);
        Files.deleteIfExists(copyOfTestFile3);
    }

    @Test(expected = NotDirectoryException.class)
    public void testDirectoryShouldThrowExceptionOnFile() throws ImageWriteException, NotAFileException, ImageReadException, IOException {
        service.directory(testFile);
    }

    private void deleteDirectoryWithFilesRecursively(Path dir) throws IOException {
        Files.walk(dir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

}
