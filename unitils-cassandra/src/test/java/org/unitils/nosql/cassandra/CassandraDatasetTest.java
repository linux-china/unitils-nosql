package org.unitils.nosql.cassandra;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import org.unitils.UnitilsJUnit3;
import org.unitils.nosql.cassandra.annotation.CassandraDataSet;

/**
 * Cassandra dataset test case
 *
 * @author linux_china
 */
@CassandraDataSet({"/cassandra.xml"})
public class CassandraDatasetTest extends UnitilsJUnit3 {
    /**
     * keyspace
     */
    private Keyspace keyspace;
    /**
     * fruit column family
     */
    private ColumnFamily<String, String> CF_FRUIT = new ColumnFamily<String, String>(
            "fruits",              // Column Family Name
            StringSerializer.get(),   // Key Serializer
            StringSerializer.get());  // Column Serializer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
                .forKeyspace("unitils")
                .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
                        .setDiscoveryType(NodeDiscoveryType.NONE)
                )
                .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
                        .setPort(9160)
                        .setMaxConnsPerHost(1)
                        .setSeeds("127.0.0.1:9160")
                )
                .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                .buildKeyspace(ThriftFamilyFactory.getInstance());

        context.start();
        this.keyspace = context.getEntity();
    }

    public void testGetFruit() throws Exception {
        OperationResult<ColumnList<String>> result = keyspace.prepareQuery(CF_FRUIT)
                .getKey("1").execute();
        ColumnList<String> columns = result.getResult();
        // Lookup columns in response by name
        String name = columns.getColumnByName("name").getStringValue();
        Double price = columns.getColumnByName("price").getDoubleValue();
        System.out.println(name);
        System.out.println(price);
    }
}
