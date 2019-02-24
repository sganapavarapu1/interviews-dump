package com.salesforce.tests.dependency.api;

import com.salesforce.tests.dependency.impl.DefaultRuleBookImpl;

/**
 * Factory for IDependencyRuleBook
 */
public class DependencyRuleBookFactory {
    /**
     * @return an instance of IDependencyRuleBook
     */
    public static IDependencyRuleBook getComponentDependencyRules() {
        return new DefaultRuleBookImpl();
    }
}
