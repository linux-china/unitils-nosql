package org.unitils.nosql.redis;

import org.unitils.core.Module;
import org.unitils.core.TestListener;

import java.util.Properties;

/**
 * Unitils Redis Module
 *
 * @author linux_china
 */
public class RedisModule implements Module {
    /**
     * Initializes the module with the given configuration settings.
     *
     * @param configuration The config, not null
     */
    public void init(Properties configuration) {

    }

    /**
     * Gives the module the opportunity to performs initialization that
     * can only work after all other modules have been initialized
     */
    public void afterInit() {

    }

    /**
     * Creates the test listener for this module.
     *
     * @return The test listener, not null
     */
    public TestListener getTestListener() {
        return null;
    }
}
