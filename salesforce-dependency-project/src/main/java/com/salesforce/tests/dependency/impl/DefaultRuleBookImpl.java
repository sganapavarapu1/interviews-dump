package com.salesforce.tests.dependency.impl;

import com.salesforce.tests.dependency.api.IDependencyRuleBook;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class manages the dictionary of dependencies rules.
 * This dictionary is a result of DEPEND commands.
 */
public class DefaultRuleBookImpl implements IDependencyRuleBook {
    /**
     * default constructor
     */
    public DefaultRuleBookImpl() {
        mUpstreamDependsOn = new HashMap<>();
        mDownstreamDependents = new HashMap<>();
    }

    /**
     * Returns all the first level upstream dependencies of the component
     * Does not return transitive upstream dependencies
     *
     * @param component
     * @return upstreamDependsOn Set<String> of first level upstream dependencies
     */
    public Set<String> getUpstreamDependencies(final String component) {
        return mUpstreamDependsOn.get(component);
    }

    /**
     * Updates first level upstream dependencies for a component
     *
     * @param component
     * @param upstreamDependencies list of first level upstream dependencies of the component
     */
    public void updateUpstreamDependencies(final String component, final List<String> upstreamDependencies) {
        for (final String anUpstreamDependency : upstreamDependencies) {
            if (isUpstreamOf(component, anUpstreamDependency)) {
                // avoid cyclic dependency possibility
                System.out.println(anUpstreamDependency + " depends on " + component + ", ignoring command");
                continue;
            }

            // -- update upstream (mUpstreamDependsOn) and downstream (mDownstreamDependents) lookup maps
            addUpstreamDependsOn(component, anUpstreamDependency);
            addDownstreamDependent(anUpstreamDependency, component);
        }
    }

    /*
     * Up stream dependencies map
     * Key: component
     * Value: set of all other components that 'the' component 'depends on'
     */
    private final Map<String, Set<String>> mUpstreamDependsOn;

    /*
     * Down stream dependencies map
     * Key: component
     * Value: set of all other components that are 'dependent' on 'the' component
     */
    private final Map<String, Set<String>> mDownstreamDependents;

    /*
     * creates a new upstream depends-on relation
     */
    private void addUpstreamDependsOn(final String component, final String upstreamDependsOn) {
        Set<String> dependsOnComponentsList = mUpstreamDependsOn.get(component);
        if (dependsOnComponentsList == null) {
            dependsOnComponentsList = new HashSet<>();
        }
        dependsOnComponentsList.add(upstreamDependsOn);
        mUpstreamDependsOn.put(component, dependsOnComponentsList);
    }

    /*
     * creates a new downstream dependent relation
     */
    private void addDownstreamDependent(final String component, final String downstreamDependent) {
        Set<String> dependentComponentsList = mDownstreamDependents.get(component);
        if (dependentComponentsList == null) {
            dependentComponentsList = new HashSet<>();
        }
        dependentComponentsList.add(downstreamDependent);
        mDownstreamDependents.put(component, dependentComponentsList);

    }

    /*
     * Check whether a component is already an upstream dependency for another component
     *
     * this method represents truth check for:
     * "is componentA an upstream component of componentB"
     *
     * @param componentA upstream component that is being checked whether it is upstream for the other component
     * @param componentB downstream component that is being determined whether it depends on the upstreamComponent
     * @return boolean true if componentB dependsOn componentA else false
     */
    private boolean isUpstreamOf(final String componentA, final String componentB) {
        final Set<String> dependents = mDownstreamDependents.get(componentA);
        return (dependents != null && dependents.contains(componentB));
    }
}
