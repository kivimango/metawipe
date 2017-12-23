package com.kivimango.metawipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public class MetaWipeTest {

    @Rule
    public final SystemOutRule output = new SystemOutRule().enableLog().mute();

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testUsageShouldDisplayOnInvalidArgument() {
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> {
            assertThat(output.getLog(), containsString("Invalid parameters"));
            assertThat(output.getLog(), containsString("usage"));
        });
        MetaWipe.main(new String[] {"-q", "124", "-invalidArgument"});
    }

    @Test
    public void testErrorMessageShouldDisplayOnEmittedPath() {
        exit.expectSystemExitWithStatus(2);
        exit.checkAssertionAfterwards(() -> assertThat(output.getLog(), containsString("File not found")));
        MetaWipe.main(new String[] {"-f", ""});
    }

    @Test
    public void testFileArgumentNullShouldDisplayUsage() {
        exit.expectSystemExitWithStatus(2);
        exit.checkAssertionAfterwards(() -> {
            assertThat(output.getLog(), containsString("File not found"));
            assertThat(output.getLog(), containsString("usage"));
        });
        MetaWipe.main(new String[] {"-f", "/home/user/aNonExistentFile.whatever"});
    }

    @Test
    public void testFileArgumentShouldDisplayUsageOnDirectory() {
        exit.expectSystemExitWithStatus(2);
        exit.checkAssertionAfterwards(() -> {
            assertThat(output.getLog(), containsString("This is not a file"));
            assertThat(output.getLog(), containsString("usage"));
        });
        String testDir = this.getClass().getClassLoader().getResource("").getFile();
        MetaWipe.main(new String[] {"-f", testDir});
    }

    @Test
    public void testShouldDisplayUsageOnFileAndDirArguments() {
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> {
            assertThat(output.getLog(), containsString("Invalid parameters"));
            assertThat(output.getLog(), containsString("usage"));
        });
        MetaWipe.main(new String[] {"-f", "-d"});
    }

    @Test
    public void testErrorMessageShouldDisplayOnDirArgument() {
        exit.expectSystemExitWithStatus(2);
        exit.checkAssertionAfterwards(() -> assertThat(output.getLog(), containsString("Directory not found")));
        MetaWipe.main(new String[] {"-d", ""});
    }

    @Test
    public void testDirArgumentShouldDisplayUsageOnFile() {
        exit.expectSystemExitWithStatus(2);
        exit.checkAssertionAfterwards(() -> {
            assertThat(output.getLog(), containsString("This is not a directory"));
            assertThat(output.getLog(), containsString("usage"));
        });
        String testFile = this.getClass().getClassLoader().getResource("not-supported.txt").getFile();
        MetaWipe.main(new String[] {"-d", testFile});
    }

    @Test
    public void testShouldDisplayUsageOnDirAndFileArguments() {
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> {
            assertThat(output.getLog(), containsString("Invalid parameters"));
            assertThat(output.getLog(), containsString("usage"));
        });
        MetaWipe.main(new String[] {"-d", "-f"});
    }

    @Test
    public void testUsageShouldDisplayOnHelpArgument() {
        MetaWipe.main(new String[] {"-help"});
        assertThat(output.getLog(), containsString("usage"));
    }

}
