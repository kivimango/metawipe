package com.kivimango.metawipe.service;

import org.junit.Test;
import java.io.File;
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
        File f = new File(path);
        String expected = "/home/user/downloads";
        assertEquals(expected, FileNameResolver.getFilePath(f));
    }

    @Test
    public void testGetFilePathWithRootDirectoryShouldReturnEmptyString() {
        String fsRoot = "/";
        File f = new File(fsRoot);
        String expected = "";
        assertEquals(expected, FileNameResolver.getFilePath(f));
    }

    @Test
    public void testGetFilePathWithDirectoryShouldReturnEmptyString() {
        String directory = "/home/";
        File f = new File(directory);
        String expected = "";
        assertEquals(expected, FileNameResolver.getFilePath(f));
    }

    @Test
    public void testGetFileNameWithoutExtension() {
        File f = new File(path);
        String expected = "picture";
        assertEquals(expected, FileNameResolver.getFileNameWithoutExtension(f));
    }

    @Test
    public void testGetExtensionShouldReturnOnlyTheFileExtensionPeriodExcluded() {
        File f = new File(path);
        String expected = "jpg";
        assertEquals(expected, FileNameResolver.getExtension(f));
    }

    @Test
    public void testGetExtensionMultipleDotsInFileNameShouldReturnOnlyTheFileExtensionPeriodExcluded() {
        String complicatedPath = "/home/user/picture.jpg.bak";
        File f = new File(complicatedPath);
        String expected = "bak";
        assertEquals(expected, FileNameResolver.getExtension(f));
    }
}
