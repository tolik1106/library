package com.zhitar.library.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a transaction attribute on an individual method
 * Use this annotation on class which have
 * {@link Connectivity} annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transaction {
    /**
     * Set this value to <code>true</code> to set read only flag
     * for database connection
     */
    boolean readOnly() default false;
}