package com.zhitar.library.annotation;

import java.lang.annotation.*;

/**
 * Describes a transaction attribute on an individual method
 * Use this annotation on class which have
 * {@link Connectivity} annotation
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transaction {
    /**
     * Set this value to <code>true</code> to set read only flag
     * for database connection
     */
    boolean readOnly() default false;
}