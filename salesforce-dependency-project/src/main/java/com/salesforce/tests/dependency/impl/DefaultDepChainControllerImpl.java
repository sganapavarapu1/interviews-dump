package com.salesforce.tests.dependency.impl;

import com.salesforce.tests.dependency.api.DependencyRuleBookFactory;
import com.salesforce.tests.dependency.api.IDependencyChainController;
import com.salesforce.tests.dependency.api.IDependencyRuleBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Default impl for runtime dependency chain controller.
 * Provides implementations for actions (commands).
 * This where current (active) dependencies are represented / managed.
 * An instance of active rules is contained in here.
 */
public class DefaultDepChainControllerImpl implements IDependencyChainController {
    /**
     * default constructor
     */
    public DefaultDepChainControllerImpl() {
        // data structures for installed components' sane relations
        mAllInstalledComponents = new ArrayList<>();
        mIsExclusivelyInstalled = new ArrayList<>();
        mRequiredUpstreamComponents = new HashMap<>();
        // active rule book
        mComponentDependencyRules = DependencyRuleBookFactory.getComponentDependencyRules();
    }

    // -- CLI command/action handlers

    /**
     * action handler for DEPEND command
     * @param component downstream component
     * @param dependsOn of upsteam services that the component depends on
     */
    public void onDependCmd(final String component, final List<String> dependsOn) {
        mComponentDependencyRules.updateUpstreamDependencies(component, dependsOn);
    }

    /**
     * action handler for INSTALL command
     * @param component to be installed
     */
    public void onInstallCmd(final String component) {
        // check whether it is already installed
        if (isComponentAlreadyInstalled(component)) {
            System.out.println(component + " is already installed");
            return;
        }
        // not yet installed -
        deepInstall(component, false); // transitive is false because this is direct install
    }

    /**
     * action handler for LIST command
     */
    public void onListCmd() {
        // simply print installed services list
        for (final String component: mAllInstalledComponents) {
            System.out.println(component);
        }
    }

    /**
     * action handler for REMOVE command
     * @param component to be removed
     */
    public void onRemoveCmd(final String component) {
        // check whether it is installed at all
        if (!isComponentAlreadyInstalled(component)) {
            // nope, not installed
            System.out.println(component + " is not installed");
            return;
        }
        // yes, it exists
        // check whether this is one of the components that's part of required components tracker map (keys only)
        Set<String> requiredComponents = mRequiredUpstreamComponents.get(component);
        if (requiredComponents != null && requiredComponents.size() > 0) {
            // yes, it is i.e. it cannot be removed
            System.out.println(component + " is still needed");
            return;
        }
        // no dependents exits, yay
        deepRemove(component, false); // transitive is false because this is direct remove
    }

    /*
     * rule book instance that's built from DEPEND commands
     */
    private final IDependencyRuleBook mComponentDependencyRules;

    /*
     * All installed service components, be it by user or automatically.
     * This list is updated during INSTALL and REMOVE command execution.
     *
     * This list is the output for LIST command.
     * This is a list (not set) because, order needs to be preserved for LIST command.
     */
    private final List<String> mAllInstalledComponents;

    /*
     * Indicates whether a component that's in above 'mAllInstalledComponents' is
     * User installed component or not.
     *
     * true indicates corresponding component at same index in 'mAllInstalledComponents'
     * is user installed, false indicates it is auto-installed.
     */
    private final List<Boolean> mIsExclusivelyInstalled;

    /*
     * This map contains all the 'required' upstream components as keys and
     * their values (as a list of components) represent downstream components for which
     * these upstream components are required.
     *
     * This will help in determining whether an automatically installed service component
     * is no longer needed or whether a component can be uninstalled at all (for it may
     * have dependents on it that are currently installed).
     *
     * This will not track transitive dependents, only the first level dependents
     * that's good enough for reliably installing and removing because the install/remove
     * implementation is recursive and takes transitive dependents/dependsOn into account
     */
    private final Map<String, Set<String>> mRequiredUpstreamComponents;

    /*
     * Handles transitive dependencies while installing components
     * i.e. recursively checks for required transitive dependencies and installs them as well
     *
     * @param component the component to be installed
     * @param boolean whether this is a transitive install request i.e. not user requested install
     * 				  true for transitive install, false for user requested install
     */
    private void deepInstall(final String component, final boolean isTransitive) {

        // during install: upstream dependencies need to exist prior to installing downstream dependencies
        // i.e. install any upstream dependency if it is not already installed
        // so, fetch upstream dependencies info (from rule-book)
        final Set<String> upstreamDependencies = mComponentDependencyRules.getUpstreamDependencies(component);

        // check if any exist at all
        if (upstreamDependencies != null) {
            // yes, they do :) - either some or all are already installed, verify =>

            // for each of the upstream dependency
            // 		check whether it is already installed -
            //		if not,
            //		 1) install it; flag it as transitively/automatically installed (i.e. not explicitly installed)
            //      2) while doing so, install its upstream dependencies ( if any were to be missing ) i.e.
            //           - invoke this deepInstall recursively (however, set isTrasitive param to true because,
            //                         it is not invoked due to direct use of Install commmand)
            //      3)   also, update the required components tracker for this upstream dependency
            //                  (i.e. flag it as one of the required components)
            //
            for (final String upstreamInstance : upstreamDependencies) {
                // check whether upstreamInstance is already installed
                if (!isComponentAlreadyInstalled(upstreamInstance)) {
                    // upstreamInstance is not installed -
                    // recursively install it (i.e. install this upstream component's upstream dependencies, as needed)
                    deepInstall(upstreamInstance, true); // this is transitive (not user requested) install
                }

                // update the required components tracker of this upstreamInstance with the component as dependent
                Set<String> installedDownstreamDependents = mRequiredUpstreamComponents.get(upstreamInstance);
                if (installedDownstreamDependents == null) {
                    installedDownstreamDependents = new HashSet<>();
                }
                // add the component as a dependent on this upstream component
                installedDownstreamDependents.add(component);
                mRequiredUpstreamComponents.put(upstreamInstance, installedDownstreamDependents);
            }
        }

        add2ComponentChain(component, !isTransitive); // negate because, isTransitive is reverse of isUserInstalled
    }

    /*
     * Handles transitive dependencies while removing components
     * i.e. recursively checks for un-needed transitive dependencies and removes them as well
     *
     * @param component the component to be removed
     * @param boolean whether this is a transitive remove request i.e. not user requested remove
     * 				  true for transitive remove, false for user requested remove
     */
    private void deepRemove(final String component, final boolean isTransitive) {

        // no need to wait for upstream dependencies removal because, this is not like installing
        // during removal, downstream dependencies are removed first - yay
        // so, remove the component requested
        remove4mComponentChain(component, !isTransitive); // negate because, isTransitive is reverse of isUserInstalled

        // check whether any upstream dependsOn entries exist for this component
        final Set<String> requiredUpstreamComponents = mComponentDependencyRules.getUpstreamDependencies(component);
        if (requiredUpstreamComponents == null) {
            return; // none exist, so job done for this component
        }

        // upstream dependencies exist in required component tracker - for each of them:
        // 	remove downstream component from the required components tracker's downstream components list
        // 	if that upstream component has no more entries in required component tracker's downstream components list,
        //		remove it from required component tracker
        //		uninstall the upstream depends on instance (recursively)
        //
        for (final String upstreamInstance: requiredUpstreamComponents) {
            // below set will never be null as it is instantiated for this upstreamInstance during INSTALL
            final Set<String> installedDownstreamDependents = mRequiredUpstreamComponents.get(upstreamInstance);
            installedDownstreamDependents.remove(component);

            // check whether this upstreamInstance has any installed downstream dependents left at all
            if (installedDownstreamDependents.size() == 0) {
                // no downstream dependencies exist
                // remove it from required components tracker
                mRequiredUpstreamComponents.remove(upstreamInstance);
                // if this upstream instance was auto-installed, it should be uninstalled i.e.
                // IMP: do not remove if it is (explicitly) installed using INSTALL cmd
                if (!isUserInstalledComponent(upstreamInstance)) {
                    deepRemove(upstreamInstance, true); // this is transitive (not user requested) remove
                }
            }
        }
    }

    /*
     * method that actually installs components into the corresponding data structures
     */
    private void add2ComponentChain(final String component, final boolean isUserInstalled) {
        System.out.println("Installing " + component);
        mAllInstalledComponents.add(component); // update actual installed chain
        mIsExclusivelyInstalled.add(isUserInstalled); // also, track whether it is user installed or not
        isSane();
    }

    /*
     * method that actually removes components from the corresponding data structures
     */
    private void remove4mComponentChain(final String component, final boolean isUserInstalled) {
        final int index = mAllInstalledComponents.indexOf(component);
        if (isUserInstalled == mIsExclusivelyInstalled.get(index)) {
            System.out.println("Removing " + component);
            mIsExclusivelyInstalled.remove(index); // remove the user installed status
            mAllInstalledComponents.remove(index); // remove the component itself
        }
        isSane();
    }

    /*
     * @return boolean true when a component is installed, else false (i.e. not-installed)
     */
    private boolean isComponentAlreadyInstalled(final String component) {
        return mAllInstalledComponents.contains(component);
    }

    /*
     * @return boolean true when a component is installed by user, else false (i.e. auto-installed)
     */
    private boolean isUserInstalledComponent(final String component) {
        final int index = mAllInstalledComponents.indexOf(component);
        if (index != -1) {
            return mIsExclusivelyInstalled.get(index);
        }
        return false;
    }

    /*
     * @return boolean true when installed components list size matches corresponding flag list size, else false
     */
    private void isSane() {
        if (mAllInstalledComponents.size() != mIsExclusivelyInstalled.size()) {
            throw new RuntimeException("MISMATCH: Count of installed components != Count of component install type flags");
        }
    }
}
