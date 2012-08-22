package org.unitils.nosql.redis;

import junit.framework.TestCase;
import redis.clients.jedis.Jedis;

/**
 * jedis client test
 *
 * @author linux_china
 */
public class JedisClientTest extends TestCase {
    /**
     * test to save and get
     *
     * @throws Exception exception
     */
    public void testSaveAndGet() throws Exception {
        Jedis jedis = new Jedis("localhost");
        jedis.set("nick", "linux_china");
        System.out.println(jedis.get("nick"));
    }
}
