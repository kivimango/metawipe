package com.kivimango.metawipe.service;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public final class ExifEraserServiceImpl implements ExifEraserService {

    private final ExifRewriter rewriter = new ExifRewriter();

    /**
     * Before deleting exif data of the image, we have to make a copy of it.
     * Otherwise Imaging library will throw an EOF exception if we want to read and write to the same file.
     * After successful deletion, the copy gets renamed to the original file, and the original file will be overridden.
     */

    @Override
    public final boolean file(final File file) throws IOException, ImageWriteException, ImageReadException, NotAFileException {
        if(!file.exists()) throw new FileNotFoundException();
        if(!file.isFile()) throw new NotAFileException();
        final File copiedFile = new File(makeCopy(file));
        deleteExifMetaData(file, copiedFile);
        if(!copiedFile.renameTo(file)) Files.deleteIfExists(copiedFile.toPath());
        return checkExifDeleted(file);
    }

    @Override
    public final void directory(final File dir) throws IOException, ImageWriteException, NotAFileException, ImageReadException {
        if(!dir.exists()) throw new FileNotFoundException();
        if(!dir.isDirectory()) {
            throw new NotDirectoryException(dir.getAbsolutePath());
        } else {
            final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.{jpg,jpeg,tiff}");
            Files.walkFileTree(dir.toPath(), new FileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if(matcher.matches(file)) {
                        try {
                            file(file.toFile());
                        } catch (ImageWriteException | ImageReadException ie) {
                            System.out.println("Error: " + ie);
                        } catch (NotAFileException ignore) {

                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    if(exc != null) {
                        System.out.println("Error: " + exc);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if(exc != null) {
                        System.out.println("Error: " + exc);
                    }
                   return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void deleteExifMetaData(File f, File copy) throws IOException, ImageWriteException, ImageReadException {
        try (FileOutputStream fos = new FileOutputStream(copy); OutputStream os = new BufferedOutputStream(fos)) {
            rewriter.removeExifMetadata(f, os);
        }
    }

    String makeCopy(File original) throws IOException {
        String tempFileName = FileNameResolver.getFilePath(original) + File.separator +
                FileNameResolver.getFileNameWithoutExtension(original) + "_copy." + FileNameResolver.getExtension(original);
        Path originalFilePath = Paths.get(original.toString());
        Path copiedFilePath = Paths.get(tempFileName);
        if(Files.exists(copiedFilePath)) { Files.deleteIfExists(copiedFilePath); }
        Files.copy(originalFilePath, copiedFilePath);
        return copiedFilePath.toString();
    }

    boolean checkExifDeleted(File f) throws IOException, ImageReadException {
        // TODO: better delete checking
        final IImageMetadata metadata = Imaging.getMetadata(f);
        return metadata == null || metadata.toString().contains("No Exif metadata.");
    }

}
