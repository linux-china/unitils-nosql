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
    /**
     * The file name of the data set. If left empty, the default filename will
     * be used: first 'classname'.'testMethodname'.xml will be tried, if that file does not exist,
     * 'classname'.xml is tried. If that file also does not exist, an exception is thrown.
     *
     * @return the fileName, empty for default
     */
    String[] value() default {};
}
