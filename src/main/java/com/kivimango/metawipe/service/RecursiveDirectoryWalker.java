package com.kivimango.metawipe.service;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

class RecursiveDirectoryWalker implements FileVisitor<Path> {

    private final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.{jpg,jpeg,tiff}");
    private ExifEraserService eraser;

    RecursiveDirectoryWalker(ExifEraserService eraser) {
        this.eraser = eraser;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Stream<Path> list = Files.list(dir);
        if (list != null) {
            list.filter(matcher::matches)
                    .forEach((Path f) -> {
                        try {
                            eraser.file(f.toFile());
                        } catch (ImageWriteException | ImageReadException | IOException ie) {
                            System.out.println("Error: " + ie);
                        } catch (NotAFileException ignore) {
                        }
                    });
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(matcher.matches(file)) {
            try {
                eraser.file(file.toFile());
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
}
