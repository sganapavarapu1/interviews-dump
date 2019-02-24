package com.salesforce.tests.dependency.api;

import com.salesforce.tests.dependency.impl.DefaultDepChainControllerImpl;

/**
 * Factory for IDependencyChainController
 */
public class DependencyChainControllerFactory {
    /**
     * @return an instance of IDependencyChainController
     */
    public static IDependencyChainController getComponentDependencyChain() {
        return new DefaultDepChainControllerImpl();
    }
}
