package com.kivimango.metawipe.service;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public final class ExifEraserServiceImpl implements ExifEraserService {

    private final ExifRewriter rewriter = new ExifRewriter();
    private final List<String> supportedFormats = Arrays.asList("jpg", "jpeg", "tiff");

    @Override
    public void directory(final Path dir) throws IOException {
        if(!Files.exists(dir)) throw new FileNotFoundException();
        if(!Files.isDirectory(dir)) {
            throw new NotDirectoryException(dir.toString());
        } else {
            Files.walkFileTree(dir, new RecursiveDirectoryWalker(this));
        }
    }

    /**
     * Before deleting exif data of the image, we have to make a copy of it.
     * Otherwise, Imaging library will throw an EOF exception if we want to read and write to the same file.
     * After successful deletion, the copy gets renamed to the original file, and the original file will be overridden.
     */

    @Override
    public boolean file(final Path file) throws IOException, ImageWriteException, ImageReadException, NotAFileException {
        if(!Files.exists(file)) throw new FileNotFoundException();
        if(Files.isDirectory(file)) throw new NotAFileException();
        if(supportedFormats.contains(FileNameResolver.getExtension(file))) {
            final Path copiedFile = makeCopy(file);
            deleteExifMetaData(file, copiedFile);
            Files.move(copiedFile, file, REPLACE_EXISTING);
        }
        return checkExifDeleted(file);
    }

    private void deleteExifMetaData(final Path f, final Path copy) throws IOException, ImageWriteException, ImageReadException {
        try (FileOutputStream fos = new FileOutputStream(copy.toFile()); OutputStream os = new BufferedOutputStream(fos)) {
            try{
                rewriter.removeExifMetadata(f.toFile(), os);
                /* During deleting, exceptions may occur.
                In this case, we have to delete the copy of the original file */
            } catch (ImageReadException e) {
                Files.deleteIfExists(copy);
                throw new ImageReadException(e.getMessage(), e);
            } catch (ImageWriteException e) {
                Files.deleteIfExists(copy);
                throw new ImageWriteException(e.getMessage(), e);
            }
        }
    }

    Path makeCopy(final Path original) throws IOException {
        final String tempFileName = FileNameResolver.getFilePath(original) + File.separator +
                FileNameResolver.getFileNameWithoutExtension(original) + "_copy." + FileNameResolver.getExtension(original);
        final Path copiedFilePath = Paths.get(tempFileName);
        if(Files.exists(copiedFilePath)) { Files.deleteIfExists(copiedFilePath); }
        Files.copy(original, copiedFilePath);
        return copiedFilePath;
    }

    boolean checkExifDeleted(final Path f) throws IOException, ImageReadException {
        // Sometimes metadata is null even if the exif records deleted
        final ImageMetadata metadata = Imaging.getMetadata(f.toFile());
        return metadata == null || metadata.toString().contains("No Exif metadata.");
    }

}
