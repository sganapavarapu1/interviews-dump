package com.salesforce.tests.dependency.api;

import java.util.List;
import java.util.Set;

/* -------------------------------------------------------------------------------
// NOTE: - do not ignore -
// Clarification of upstream and downstream terms used in this project
//
// 1) the implied use of upstream & downstream terms in this project is similar
//    to water flowing as stream e.g. population down stream are dependent on
//    population up stream to release water (esp. where dams exist)
//    In context of this project,
//      components downstream are dependent on components upstream
//      e.g. DEPEND TELNET TCPIP NETCARD
//            - TELNET is down stream component to TCPIP & NETCARD
//            - TCPIP & NETCARD are up stream components to TELNET
//
// 2) when working with APIs, API clients i.e. RESTful services...
//    if a service A is calling service B:
//      A is upstream dependency of B, where as B is downstream dependency of A
//      this stems from the diagrammatic representation of API layers
//    This (#2) is NOT what is followed in this project
// ----------------------------------------------------------------------------- */

/**
 * Contract for the dictionary of dependencies rules.
 * This dictionary is a result of DEPEND commands.
 */
public interface IDependencyRuleBook {
    /**
     * Returns all the first level upstream dependencies of the component
     * Does not return transitive (upstream) dependencies
     *
     * IMP: Upstream dependencies implies the components on which the (param) component is dependent on.
     *
     * @param component
     * @return upstreamDependsOn Set<String> of first level upstream dependencies
     */
    Set<String> getUpstreamDependencies(final String component);

    /**
     * Updates first level upstream dependencies for a component
     *
     * @param component
     * @param upstreamDependencies list of first level upstream dependencies of the component
     */
    void updateUpstreamDependencies(final String component, final List<String> upstreamDependencies);
}
