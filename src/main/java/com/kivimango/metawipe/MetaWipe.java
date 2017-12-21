package com.kivimango.metawipe;

import com.kivimango.metawipe.service.ExifEraserService;
import com.kivimango.metawipe.service.ExifEraserServiceImpl;
import com.kivimango.metawipe.service.NotAFileException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import java.io.File;
import java.io.IOException;

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
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public final class MetaWipe {

    private final ArgumentResolver resolver = new ArgumentResolver();
    private final ExifEraserService eraser = new ExifEraserServiceImpl();
    private CommandLine params = null;

    public static void main(String[] args)  {
        MetaWipe app = new MetaWipe();
        app.run(args);
    }

    private void run(String[] args) {
        parseCommandLineArguments(args);
        if(params != null) {
            if (params.hasOption("f") && !params.hasOption("d")) {
                String path = params.getOptionValue("f");
                System.out.println(path);
                if (path != null) {
                    tryEraseExifOrExitOnFailure(path);
                } else {
                    exitWithErrorMessage("You must supply a file path", 1);
                }
            } else if (params.hasOption("d") && !params.hasOption("f")) {
                String path = params.getOptionValue("d");
                if (path == null) {
                    exitWithErrorMessage("You must supply a path", 1);
                }
                File dir = new File(path);
                try {
                    eraser.directory(dir);
                } catch (IOException e) {
                    exitWithErrorMessage("An I/O error occured! " + e.toString(), 4);
                } catch (ImageReadException nfe) {
                    exitWithErrorMessage("Can't write image! " + nfe.getMessage(), 4);
                } catch (ImageWriteException ire) {
                    exitWithErrorMessage("Can't read image! " + ire.getMessage(), 4);
                } catch (NotAFileException nfe) {
                    exitWithErrorMessage("This is not a file! " + path, 2);
                }
            } else if (params.hasOption("help")) resolver.displayUsage();
        } else resolver.displayUsage();
    }

    private void parseCommandLineArguments(String[] args) {
        try {
            params = resolver.parseCommandLineArguments(args);
        } catch (ParseException e) {
            exitWithErrorMessage(" Invalid parameters !", 1);
        }
    }

    private void tryEraseExifOrExitOnFailure(String path) {
        try {
            eraser.file(new File(path));
        } catch (IOException e) {
            exitWithErrorMessage("An I/O error occured! " + e.getMessage(), 4);
        } catch (ImageReadException ire) {
            exitWithErrorMessage("Can't read image! " + ire.getMessage(), 4);
        } catch (ImageWriteException iwe) {
            exitWithErrorMessage("Can't write image! " + iwe.getMessage(), 4);
        } catch (NotAFileException nfe) {
            exitWithErrorMessage("This is not a file! " + path, 2);
        }
    }

    private void exitWithErrorMessage(String msg, int exitCode) {
        System.out.println(msg);
        resolver.displayUsage();
        System.exit(exitCode);
    }

}
