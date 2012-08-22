package org.unitils.nosql.redis.annotation;

import java.lang.annotation.*;

/**
 * redis dataset annotation
 *
 * @author linux_china
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisDataSet {
}
