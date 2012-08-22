package org.unitils.nosql.redis;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.unitils.core.Module;
import org.unitils.core.TestListener;
import org.unitils.nosql.redis.annotation.RedisDataSet;
import org.unitils.util.AnnotationUtils;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

/**
 * Unitils Redis Module
 *
 * @author linux_china
 */
@SuppressWarnings("unchecked")
public class RedisModule implements Module {
    /**
     * jedis
     */
    private Jedis jedis = null;

    /**
     * Initializes the module with the given configuration settings.
     *
     * @param configuration The config, not null
     */
    public void init(Properties configuration) {
        String host = configuration.getProperty("redis.host");
        String port = configuration.getProperty("redis.port");
        if (port == null) {
            port = "6379";
        }
        jedis = new Jedis(host, Integer.valueOf(port));
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
        return new RedisListener();
    }

    /**
     * Test listener that is called while the test framework is running tests
     */
    protected class RedisListener extends TestListener {
        /**
         * Invoked before any of the test in a test class are run.
         * This can be overridden to for example add test-class initialization.
         *
         * @param testObject The test class, not null
         */
        @Override
        public void afterCreateTestObject(Object testObject) {
            RedisDataSet redisDataSet = AnnotationUtils.getClassLevelAnnotation(RedisDataSet.class, testObject.getClass());
            if (redisDataSet != null) {
                fillValuesToRedis(redisDataSet.value());
            }
        }

        @Override
        public void beforeTestSetUp(Object testObject, Method testMethod) {
            RedisDataSet redisDataSet = testMethod.getAnnotation(RedisDataSet.class);
            if (redisDataSet != null) {
                fillValuesToRedis(redisDataSet.value());
            }
        }

        /**
         * fill values to redis
         *
         * @param locations locations
         */
        @SuppressWarnings({"ConstantConditions"})
        private void fillValuesToRedis(String[] locations) {
            if (locations != null && locations.length > 0) {
                for (String location : locations) {
                    try {
                        SAXBuilder builder = new SAXBuilder();
                        Document document = builder.build(this.getClass().getResourceAsStream(location));
                        List<Element> keyElements = document.getRootElement().getChildren("key");
                        for (Element keyElement : keyElements) {
                            String key = keyElement.getAttributeValue("name");
                            String value = keyElement.getTextTrim();
                            if (key != null && value != null) {
                                jedis.set(key, value);
                            }
                        }
                    } catch (Exception ignore) {

                    }
                }
            }
        }

    }
}
