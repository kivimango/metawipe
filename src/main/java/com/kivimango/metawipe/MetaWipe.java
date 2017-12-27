package com.kivimango.metawipe;

import com.kivimango.metawipe.service.ExifEraserService;
import com.kivimango.metawipe.service.ExifEraserServiceImpl;
import com.kivimango.metawipe.service.NotAFileException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Small command-line tool to clear the metadata/exif records of your photos to give back the control
 * over security and privacy.
 *
 * It is useful before uploading photos to cloud like Facebook, Google, etc if you don't want to share your GPS location
 * and other sensitive data with anyone.
 *
 * Usage:
 * For one file :
 * metawipe -f ~/home/user/photos/myphoto.jpg
 *
 * For a directory and its subdirectories :
 * metawipe -d ~/home/user/photos/
 *
 * NOTE: Exit codes are
 * 0: success
 * 1: invalid parameters
 * 2: I/O related errors
 * 4: image manipulation related errors
 *
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public final class MetaWipe {

    private final ArgumentResolver resolver = new ArgumentResolver();
    private final ExifEraserService eraser = new ExifEraserServiceImpl();
    private CommandLine params = null;

    private static final char FILE_ARG = 'f';
    private static final char DIR_ARG = 'd';
    private static final String HELP_ARG = "help";

    private static final String VERSION = "0.1";

    private void run(final String[] args) {
        parseCommandLineArguments(args);
        if(params != null) {
            if (params.hasOption(FILE_ARG) && (!params.hasOption(DIR_ARG) && !params.hasOption(HELP_ARG))) {
                tryEraseExifOrExitOnFailure(params.getOptionValue(FILE_ARG));
            } else if (params.hasOption(DIR_ARG) && !params.hasOption(FILE_ARG)) {
                tryEraseExifInDir(params.getOptionValue(DIR_ARG));
            } else if(params.hasOption(HELP_ARG) && (!params.hasOption(FILE_ARG) && !params.hasOption(DIR_ARG))) {
                resolver.displayUsage();
            } else {
                displayInfo();
            }
        } else resolver.displayUsage();
    }

    private void tryEraseExifInDir(final String pathValue) {
        if (!empty(pathValue)) {
            try {
                final Path dir = Paths.get(pathValue);
                eraser.directory(dir);
            } catch (NotDirectoryException nde) {
               exitWithErrorMessage("This is not a directory: " + pathValue, 2);
            } catch (FileNotFoundException fne) {
                exitWithErrorMessage("Directory not found : " + pathValue, 2);
            } catch (IOException e) {
                exitWithErrorMessage("An I/O error occurred: " + e.getMessage(), 2);
            }
        } else {
            exitWithErrorMessage("You must supply a path", 1);
        }
    }

    private void tryEraseExifOrExitOnFailure(final String pathValue) {
        if (!empty(pathValue)) {
            try {
                final Path path = Paths.get(pathValue);
                eraser.file(path);
            } catch (NotAFileException nfe) {
                exitWithErrorMessage("This is not a file! " + pathValue, 2);
            } catch (FileNotFoundException e) {
                exitWithErrorMessage("File not found! " + pathValue, 2);
            } catch (ImageReadException ire) {
                exitWithErrorMessage("Can't read image! " + ire.getMessage(), 4);
            } catch (ImageWriteException iwe) {
                exitWithErrorMessage("Can't write image! " + iwe.getMessage(), 4);
            } catch (IOException e) {
                exitWithErrorMessage("An I/O error occurred: " + e.getMessage(), 2);
            }
        } else {
            exitWithErrorMessage("You must supply a path !", 1);
        }
    }

    private void parseCommandLineArguments(String[] args) {
        try {
            params = resolver.parseCommandLineArguments(args);
        } catch (ParseException e) {
            exitWithErrorMessage("Invalid parameters !", 1);
        }
    }

    private boolean empty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void exitWithErrorMessage(String msg, int exitCode) {
        System.out.println(msg);
        resolver.displayUsage();
        System.exit(exitCode);
    }

    private void displayInfo() {
        System.out.println("metawipe (Version: " + VERSION +")");
        System.out.println("Small command-line tool to clear the metadata/exif records of your photos to give back the control over security and privacy.");
        System.out.println("It is useful to clear metadata before uploading your photos to cloud like Facebook, Google, etc if you don't want to share your GPS location");
        System.out.println("and other sensitive data with anyone.");
        System.out.println("Type <metawipe -help> to display the help.");
        System.out.println("Visit https://github.com/kivimango/metawipe for more info.");
    }

    public static void main(String[] args)  {
        MetaWipe app = new MetaWipe();
        app.run(args);
    }

}
