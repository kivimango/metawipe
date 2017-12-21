package com.kivimango.metawipe.service;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import java.io.File;
import java.io.IOException;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public interface ExifEraserService {

    /**
     * Deletes EXIF data from one particular file.
     * @param file Reference of the file.
     * @return true on success deleting
     */

    boolean file(final File file) throws IOException, ImageWriteException, ImageReadException, NotAFileException;

    /**
     * Deletes EXIF data from supported files found in the directory and its subdirectories recursively.
     * @param dir Reference of the directory
     */

    void directory(final File dir) throws IOException, ImageWriteException, NotAFileException, ImageReadException;
}
