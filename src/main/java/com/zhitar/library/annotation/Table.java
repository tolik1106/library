package com.zhitar.library.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation if you want to specify
 * table name for your entity
 * @see com.zhitar.library.util.TableNameResolver
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * The name of the table.
     */
    String value();
}
