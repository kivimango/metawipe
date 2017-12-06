package com.kivimango.metawipe.service;

import java.io.File;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 * */

final class FileNameResolver {

    static String getFilePath(File file) {
        String absolutePath = file.getAbsolutePath();
        return absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
    }

    // TODO : duplicate code

    static String getFileNameWithoutExtension(File file) {
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }

    static String getExtension(File file) {
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(pos + 1);
        }
        return fileName;
    }

}
