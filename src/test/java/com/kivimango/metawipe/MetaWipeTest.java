package com.kivimango.metawipe;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public class MetaWipeTest {

    private ByteArrayOutputStream output;
    private PrintStream out;

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Before
    public void setupStream() {
        output = new ByteArrayOutputStream();
        out = System.out;
        System.setOut(new PrintStream(output));
    }

    @After
    public void cleanupStream() {
        output.reset();System.setOut(out);
    }

    @Test
    public void testUsageShouldDisplayOnInvalidArgument() {
        exit.expectSystemExitWithStatus(1);
        String invalidArgument = "-q";
        MetaWipe.main(new String[] {invalidArgument});
        assertThat(output.toString(), containsString("Invalid parameters"));
        assertThat(output.toString(), containsString("usage"));
    }

    @Test
    public void testErrorMessageShouldDisplayOnEmittedPath() {
        //exit.expectSystemExitWithStatus(1);
        exit.expectSystemExit();
        String emittedPathForFileArg = "-f";
        MetaWipe.main(new String[] {emittedPathForFileArg, " "});
        assertThat(output.toString(), containsString("supply"));
    }

    @Test
    public void testUsageShouldDisplayOnHelpArgument() {
        String helpArg = "-help";
        MetaWipe.main(new String[] {helpArg});
        assertThat(output.toString(), containsString("usage"));
    }

}
