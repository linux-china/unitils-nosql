package org.unitils.nosql.cassandra;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import junit.framework.TestCase;

/**
 * astyanax client test
 *
 * @author linux_china
 */
public class AstyanaxClientTest extends TestCase {
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

    /**
     * setup logic
     *
     * @throws Exception exception
     */
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

    /**
     * test to get Row
     *
     * @throws Exception exception
     */
    public void testGetRow() throws Exception {
        OperationResult<ColumnList<String>> result = keyspace.prepareQuery(CF_FRUIT)
                .getKey("1").execute();
        ColumnList<String> columns = result.getResult();
        // Lookup columns in response by name
        String name = columns.getColumnByName("name").getStringValue();
    }

    /**
     * test to insert row
     */
    public void testInsertRow() throws Exception {
        // Inserting data
        MutationBatch m = keyspace.prepareMutationBatch();
        m.withRow(CF_FRUIT, "1")
                .putColumn("name", "Apple", null)
                .putColumn("price", 10.8, null);
        m.withRow(CF_FRUIT, "2")
                .putColumn("name", "Orange", null)
                .putColumn("price", 5.4, null);
        try {
            OperationResult<Void> result = m.execute();
        } catch (ConnectionException e) {
        }
    }
}
