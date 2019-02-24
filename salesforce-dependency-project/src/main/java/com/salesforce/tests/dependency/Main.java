package com.salesforce.tests.dependency;

import com.salesforce.tests.dependency.api.CLI;
import com.salesforce.tests.dependency.api.CLIFactory;

import java.util.Scanner;

/**
 * The entry point for the Test program
 */
public class Main {

    public static void main(String[] args) {

        // command interpreter
        final CLI cli = CLIFactory.getDefaultCLI();

        // read input from stdin
        final Scanner scan = new Scanner(System.in);

        boolean quit = false;
        while (!quit && scan.hasNextLine()) {

            // read next line
            String line = scan.nextLine();

            // no action for empty input
            if (line == null || line.length() == 0) {
                continue;
            }

            // invoke command line interpreter
            // CLI returns true when END is encountered
            quit = cli.processCommandLine(line);
        }

        scan.close();
    }
}
