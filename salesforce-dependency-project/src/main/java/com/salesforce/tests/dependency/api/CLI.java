package com.salesforce.tests.dependency.api;

/**
 * Command line interpreter contract
 */
public interface CLI {
    /**
     * Processes a command i.e.
     * - parses & interprets a command string and
     * - invokes respective action handler
     *
     * Returns whether to terminate the CLI instance
     * - true: terminate
     * - false: continue to execute
     *
     * @param line user input command line
     * @return shouldStop boolean flag to indicate whether to stop
     */
    boolean processCommandLine(final String line);
}
