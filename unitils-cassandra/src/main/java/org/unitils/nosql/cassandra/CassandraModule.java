package org.unitils.nosql.cassandra;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.unitils.core.Module;
import org.unitils.core.TestListener;
import org.unitils.nosql.cassandra.annotation.CassandraDataSet;
import org.unitils.util.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Unitils Cassandra module
 *
 * @author linux_china
 */
public class CassandraModule implements Module {
    /**
     * keyspace
     */
    private Keyspace keyspace;

    /**
     * Initializes the module with the given configuration settings.
     *
     * @param configuration The config, not null
     */
    public void init(Properties configuration) {
        String host = configuration.getProperty("cassandra.host");
        Integer port = Integer.valueOf(configuration.getProperty("cassandra.port", "9160"));
        String keyspace = configuration.getProperty("cassandra.keyspace");
        AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
                .forKeyspace(keyspace)
                .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
                        .setDiscoveryType(NodeDiscoveryType.NONE)
                )
                .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("UnitilsConnectionPool")
                        .setPort(port)
                        .setMaxConnsPerHost(1)
                        .setSeeds(host + ":" + port)
                )
                .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                .buildKeyspace(ThriftFamilyFactory.getInstance());

        context.start();
        this.keyspace = context.getEntity();
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
        return new CassandraListener();
    }

    /**
     * Test listener that is called while the test framework is running tests
     */
    @SuppressWarnings("unchecked")
    protected class CassandraListener extends TestListener {
        /**
         * Invoked before any of the test in a test class are run.
         * This can be overridden to for example add test-class initialization.
         *
         * @param testObject The test class, not null
         */
        public void afterCreateTestObject(Object testObject) {
            CassandraDataSet redisDataSet = AnnotationUtils.getClassLevelAnnotation(CassandraDataSet.class, testObject.getClass());
            if (redisDataSet != null) {
                fillValuesToRedis(redisDataSet.value());
            }
        }

        /**
         * Invoked before the test but after the test setup (eg @Before) is run.
         * This can be overridden to for example further initialize the test-fixture using values that were set during
         * the test setup.
         *
         * @param testObject The test instance, not null
         * @param testMethod The test method, not null
         */
        public void beforeTestMethod(Object testObject, Method testMethod) {
            super.beforeTestMethod(testObject, testMethod);
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
                        List<Element> columnFamilyElements = document.getRootElement().getChildren();
                        // Inserting data
                        MutationBatch m = keyspace.prepareMutationBatch();
                        for (Element columnFamilyElement : columnFamilyElements) {
                            String columnFamilyName = columnFamilyElement.getName();
                            ColumnListMutation mutation = m.withRow(getColumnFamily(columnFamilyName), columnFamilyElement.getAttributeValue("id"));
                            List<Attribute> attributes = columnFamilyElement.getAttributes();
                            for (Attribute attribute : attributes) {
                                String name = attribute.getName();
                                String value = attribute.getValue();
                                if (!name.equals("id") && value != null && !value.isEmpty()) {
                                    mutation = mutation.putColumn(name, value, null);
                                }
                            }
                        }
                        try {
                            OperationResult<Void> result = m.execute();
                        } catch (ConnectionException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception ignore) {

                    }
                }
            }
        }

        /**
         * get column family
         *
         * @param name column family name
         * @return column family object
         */
        private ColumnFamily getColumnFamily(String name) {
            return new ColumnFamily<String, String>(
                    name,              // Column Family Name
                    StringSerializer.get(),   // Key Serializer
                    StringSerializer.get());  // Column Serializer;
        }
    }
}
