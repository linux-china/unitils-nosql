package org.unitils.nosql.cassandra.annotation;

import java.lang.annotation.*;

/**
 * Cassandra dataset annotation
 *
 * @author linux_china
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface CassandraDataSet {
}
