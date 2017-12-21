package com.kivimango.metawipe;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

final class ArgumentResolver {

    private static final String FILE_FLAG = "f";
    private static final String DIR_FLAG = "d";
    private static final String HELP_FLAG = "help";

    private final Options flagOptions = new Options();
    private final CommandLineParser parser = new DefaultParser();

    ArgumentResolver() {
        makeOptions();
    }

    private void makeOptions() {
        Option fileOption = Option.builder(FILE_FLAG).optionalArg(true).argName("file").hasArg().desc("Clears metadata of the given photo").build();
        Option dirOption = Option.builder(DIR_FLAG).optionalArg(true).argName("dir").hasArg().desc("Clears every photo in a directory and its subdirectories recursively").build();
        Option helpOption = Option.builder(HELP_FLAG).optionalArg(true).argName("help").hasArg(false).desc("Displays this help").build();
        flagOptions.addOption(fileOption);
        flagOptions.addOption(dirOption);
        flagOptions.addOption(helpOption);
    }

    final CommandLine parseCommandLineArguments(String[] args) throws ParseException {
        return parser.parse(flagOptions, args);
    }

    final void displayUsage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "metawipe", flagOptions);
    }

}
