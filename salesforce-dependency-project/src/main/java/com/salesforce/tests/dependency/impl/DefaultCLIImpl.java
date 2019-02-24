package com.salesforce.tests.dependency.impl;

import com.salesforce.tests.dependency.api.CLI;
import com.salesforce.tests.dependency.api.DependencyChainControllerFactory;
import com.salesforce.tests.dependency.api.IDependencyChainController;

import java.util.Arrays;
import java.util.List;

/**
 * (Default) Command line interpreter implementation
 */
public class DefaultCLIImpl implements CLI {

    /**
     * Enumeration of allowed valid commands
     */
    enum COMMAND {
        END,
        DEPEND,
        INSTALL,
        LIST,
        REMOVE;
    }

    private final IDependencyChainController mDependencyChain;

    public DefaultCLIImpl() {
        mDependencyChain = DependencyChainControllerFactory.getComponentDependencyChain();
    }

    /**
     * Parses a command string, interprets it and invokes respective action handler.
     *
     * Returns whether to terminate the CLI instance
     * - true: terminate
     * - false: continue to execute
     *
     * @param cmdStr user input command line (String)
     * @return shouldStop flag indicating whether to stop (boolean)
     */
    public boolean processCommandLine(final String cmdStr) {

        System.out.println(cmdStr); // echo the line

        boolean shouldStop = false;

        final String[] words = cmdStr.split("\\s+");
        final COMMAND commandWord = COMMAND.valueOf(words[0]);

        switch (commandWord) {

            case END:
                // marks end of the input commands
                shouldStop = true;
                break;

            case DEPEND: // Syntax: DEPEND item1 item2 [item3...]
            {
                final String component = words[1]; // item1
                // item2 [item3...]
                final List<String> dependenciesOnly = Arrays.asList(words).subList(2, words.length);
                // DEPEND command handler
                mDependencyChain.onDependCmd(component, dependenciesOnly);
            }
            break;


            case INSTALL: // Syntax: INSTALL item1
            {
                final String component = words[1]; // item1
                // INSTALL command handler
                mDependencyChain.onInstallCmd(component);
            }
            break;

            case LIST: // Syntax: LIST
                // LIST command handler
                mDependencyChain.onListCmd();
                break;

            case REMOVE: // Syntax: REMOVE item1
            {
                final String component = words[1]; // item1
                // REMOVE command handler
                mDependencyChain.onRemoveCmd(component);
            }
            break;

            default:
                // ignore invalid commands because there no requirement to display any message
                break;
        }

        return shouldStop;
    }
}
