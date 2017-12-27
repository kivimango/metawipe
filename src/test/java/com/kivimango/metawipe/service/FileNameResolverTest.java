package com.kivimango.metawipe.service;

import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public final class FileNameResolverTest {

    private final String path = "/home/user/downloads/picture.jpg";

    @Test
    public void testGetFilePathShouldReturnOnlyTheAbloslutePathWithoutNameAndExtension() {
        Path f = Paths.get(path);
        String expected = "/home/user/downloads";
        assertEquals(expected, FileNameResolver.getFilePath(f));
    }

    @Test
    public void testGetFilePathWithRootDirectoryShouldReturnEmptyString() {
        String fsRoot = "/";
        Path f = Paths.get(fsRoot);
        String expected = "";
        assertEquals(expected, FileNameResolver.getFilePath(f));
    }

    @Test
    public void testGetFilePathWithDirectoryShouldReturnEmptyString() {
        String directory = "/home/";
        Path f = Paths.get(directory);
        String expected = "";
        assertEquals(expected, FileNameResolver.getFilePath(f));
    }

    @Test
    public void testGetFileNameWithoutExtension() {
        Path f = Paths.get(path);
        String expected = "picture";
        assertEquals(expected, FileNameResolver.getFileNameWithoutExtension(f));
    }

    @Test
    public void testGetExtensionShouldReturnOnlyTheFileExtensionPeriodExcluded() {
        Path f = Paths.get(path);
        String expected = "jpg";
        assertEquals(expected, FileNameResolver.getExtension(f));
    }

    @Test
    public void testGetExtensionMultipleDotsInFileNameShouldReturnOnlyTheFileExtensionPeriodExcluded() {
        String complicatedPath = "/home/user/picture.jpg.bak";
        Path f = Paths.get(complicatedPath);
        String expected = "bak";
        assertEquals(expected, FileNameResolver.getExtension(f));
    }
}
