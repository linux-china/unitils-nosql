package org.unitils.nosql.redis;

import org.unitils.UnitilsJUnit3;
import org.unitils.nosql.redis.annotation.RedisDataSet;
import redis.clients.jedis.Jedis;

/**
 * redis dataset test case
 *
 * @author linux_china
 */
@RedisDataSet({"/redis.xml"})
public class RedisDataSetTest extends UnitilsJUnit3 {
    /**
     * test to find value
     *
     * @throws Exception
     */
    public void testFind() throws Exception {
        Jedis jedis = new Jedis("localhost");
        String value = jedis.get("nick");
        assertNotNull("Failed to get data from Redis", value);
    }
}
