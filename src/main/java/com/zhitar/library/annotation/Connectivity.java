package com.zhitar.library.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation tells {@link com.zhitar.library.Container}
 * to create proxy object to use with database connection
 * @see TransactionProxyCreator#createProxy(Object)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Connectivity {
}