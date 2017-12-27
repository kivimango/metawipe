package com.kivimango.metawipe.service;

import java.io.File;
import java.nio.file.Path;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 * */

final class FileNameResolver {

    static String getFilePath(final Path file) {
        String absolutePath = file.toAbsolutePath().toString();
        return absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
    }

    // TODO : duplicate code

    static String getFileNameWithoutExtension(final Path file) {
        String fileName = file.getFileName().toString();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }

    static String getExtension(final Path file) {
        String fileName = file.getFileName().toString();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(pos + 1);
        }
        return fileName;
    }

}
