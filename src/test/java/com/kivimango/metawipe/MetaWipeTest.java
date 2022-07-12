package com.kivimango.metawipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.util.Objects;

import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public final class MetaWipeTest {

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
    public void testFileArgumentShouldDisplayUsageOnEmittedPath() {
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> {
            assertThat(output.getLog(), containsString("You must supply a path !"));
            assertThat(output.getLog(), containsString("usage"));
        });
        MetaWipe.main(new String[] {"-f", ""});
    }

    @Test
    public void testFileArgumentOnNonExistentFileShouldDisplayUsage() {
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
        String testDir = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getFile();
        MetaWipe.main(new String[] {"-f", testDir});
    }

    @Test
    public void testShouldDisplayInfoOnFileAndDirArguments() {
        MetaWipe.main(new String[] {"-f", "-d"});
        assertThat(output.getLog(), containsString("metawipe"));
        assertThat(output.getLog(), containsString("Version"));
    }

    @Test
    public void testDirArgumentErrorMessageShouldDisplayOnEmittedPath() {
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(output.getLog(), containsString("You must supply a path")));
        MetaWipe.main(new String[] {"-d", ""});
    }

    @Test
    public void testDirArgumentShouldDisplayUsageOnFile() {
        exit.expectSystemExitWithStatus(2);
        exit.checkAssertionAfterwards(() -> {
            assertThat(output.getLog(), containsString("This is not a directory"));
            assertThat(output.getLog(), containsString("usage"));
        });
        String testFile = Objects.requireNonNull(this.getClass().getClassLoader().getResource("not-supported.txt")).getFile();
        MetaWipe.main(new String[] {"-d", testFile});
    }

    @Test
    public void testShouldDisplayInfoOnDirAndFileArguments() {
        MetaWipe.main(new String[] {"-d", "-f"});
        assertThat(output.getLog(), containsString("metawipe"));
        assertThat(output.getLog(), containsString("Version"));
    }

    @Test
    public void testUsageShouldDisplayOnHelpArgument() {
        MetaWipe.main(new String[] {"-help"});
        assertThat(output.getLog(), containsString("usage"));
    }

}
