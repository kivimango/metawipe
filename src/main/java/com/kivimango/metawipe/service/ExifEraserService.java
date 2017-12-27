package com.kivimango.metawipe.service;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public interface ExifEraserService {

    /**
     * Deletes EXIF data from supported files found in the directory and its subdirectories recursively.
     * @param dir Reference of the directory
     */

    void directory(final Path dir) throws IOException;

    /**
     * Deletes EXIF data from one particular file.
     * @param file Reference of the file.
     * @return true on success deleting
     */

    boolean file(final Path file) throws IOException, ImageWriteException, ImageReadException, NotAFileException;
}
