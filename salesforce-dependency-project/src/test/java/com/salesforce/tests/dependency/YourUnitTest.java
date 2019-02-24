package com.salesforce.tests.dependency;

import java.io.IOException;

import org.junit.Test;

/**
 * Place holder for your unit tests
 */
public class YourUnitTest extends BaseTest {

    @Test
    public void testTransitiveInstallRemove() throws IOException {

        final String[] input = {
                "DEPEND HDUI VOD\n",
                "DEPEND VOD TVBUS\n",
                "DEPEND TVBUS NPK\n",
                "INSTALL HPK\n",
                "INSTALL HDUI\n",
                "LIST\n",
                "REMOVE VOD\n",
                "REMOVE NPK\n",
                "REMOVE HDUI\n",
                "LIST\n",
                "END\n"
        };

        final String expectedOutput =
                        "DEPEND HDUI VOD\n" +
                        "DEPEND VOD TVBUS\n" +
                        "DEPEND TVBUS NPK\n" +
                        "INSTALL HPK\n" +
                        "Installing HPK\n" +
                        "INSTALL HDUI\n" +
                        "Installing NPK\n" +
                        "Installing TVBUS\n" +
                        "Installing VOD\n" +
                        "Installing HDUI\n" +
                        "LIST\n" +
                        "HPK\n" +
                        "NPK\n" +
                        "TVBUS\n" +
                        "VOD\n" +
                        "HDUI\n" +
                        "REMOVE VOD\n" +
                        "VOD is still needed\n" +
                        "REMOVE NPK\n" +
                        "NPK is still needed\n" +
                        "REMOVE HDUI\n" +
                        "Removing HDUI\n" +
                        "Removing VOD\n" +
                        "Removing TVBUS\n" +
                        "Removing NPK\n" +
                        "LIST\n" +
                        "HPK\n" +
                        "END\n";

        runTest(expectedOutput, input);
    }
}
