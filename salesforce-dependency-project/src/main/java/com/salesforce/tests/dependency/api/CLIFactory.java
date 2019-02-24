package com.salesforce.tests.dependency.api;

import com.salesforce.tests.dependency.impl.DefaultCLIImpl;

/**
 * Factory for CLI (command line interpreter) instance
 */
public class CLIFactory {
    /**
     * @return an instance of CLI
     */
    public static CLI getDefaultCLI() {
        return new DefaultCLIImpl();
    }
}
