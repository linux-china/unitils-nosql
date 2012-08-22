Unitils NoSQL Modules
====================================
Unitils NoSQL Modules are module collections to assitant NoSQL unit test in Unitils.

### Setup

        CREATE KEYSPACE unitils WITH strategy_class = SimpleStrategy AND strategy_options:replication_factor = 1;

### Redis Module
Please create unitils-local.properties, and add redis to unitils.modules. Code as following:

     unitils.modules=database,dbunit,hibernate,mock,easymock,inject,spring,jpa,io,redis

     unitils.module.redis.className=org.unitils.nosql.redis.RedisModule
     unitils.module.redis.runAfter=
     unitils.module.redis.enabled=true
Add Redis configuration in unitils-local.properties as following:

     redis.host=127.0.0.1
     redis.port=6379
Then we will create a xml file, such as src/test/resources/redis.xml with following code:

     <?xml version="1.0" encoding="utf-8" ?>
     <store>
         <key name="nick">Jacky Chan</key>
     </store>
Finally add @RedisDataSet in your unit test class just like this:

     @RedisDataSet({"/redis.xml"})
     public class RedisDataSetTest extends UnitilsJUnit3 {
Before test started, values from Redis dataset xml will be stored to server, and you can use these data.

### Cassandra Module
Cassandras support, such as column family.
