Unitils NoSQL Modules
====================================
Unitils NoSQL Modules are module collections to assitant NoSQL unit test in Unitils.

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
Please create unitils-local.properties, and add cassandra to unitils.modules. Code as following:

     unitils.modules=database,dbunit,hibernate,mock,easymock,inject,spring,jpa,io,cassandra

     unitils.module.cassandra.className=org.unitils.nosql.cassandra.CassandraModule
     unitils.module.cassandra.runAfter=
     unitils.module.cassandra.enabled=true
Add cassandra configuration in unitils-local.properties as following:

     cassandra.host=127.0.0.1
     cassandra.port=9160
     cassandra.keyspace=unitils
Then we will create a xml file, such as src/test/resources/cassandra.xml with following code:

     <?xml version="1.0" encoding="UTF-8" ?>
     <keyspace>
         <fruits id="1" name="Apple" price="double(9.88)"/>
     </keyspace>
fruits is column family name, and id is the primary key, and you can not change the id name, and it is a reserved word.
**If you add validation_class for column, such as price is double type, please add type cast, such as double(9.88).**
Finally add @RedisDataSet in your unit test class just like this:

     @CassandraDataSet({"/cassandra.xml"})
     public class CassandraDatasetTest extends UnitilsJUnit3 {
Before test started, dataset xml will be stored into column family, and you can use these data.
