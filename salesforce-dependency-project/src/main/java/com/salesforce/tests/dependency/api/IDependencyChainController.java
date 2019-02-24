package com.salesforce.tests.dependency.api;

import java.util.List;

/**
 * Contract for runtime dependency chain controller.
 * Provides implementations for actions (commands).
 */
public interface IDependencyChainController {
    /**
     * action handler for DEPEND command
     * @param component downstream component
     * @param dependsOn of upstream services that the component depends on
     */
    void onDependCmd(final String component, final List<String> dependsOn);

    /**
     * action handler for INSTALL command
     * @param component to be installed
     */
    void onInstallCmd(final String component);

    /**
     * action handler for LIST command
     */
    void onListCmd();

    /**
     * action handler for REMOVE command
     * @param component to be removed
     */
    void onRemoveCmd(final String component);
}
