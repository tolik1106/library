package com.zhitar.library.annotation;

import java.lang.annotation.*;

/**
 * Use this annotation if you want to specify
 * table name for your entity
 * @see com.zhitar.library.util.TableNameResolver
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * The name of the table.
     */
    String value();
}
