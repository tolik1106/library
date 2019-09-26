package com.zhitar.library.annotation;

import java.lang.annotation.*;

/**
 * This annotation tells {@link com.zhitar.library.Container}
 * to create proxy object to use with database connection
 * @see TransactionProxyCreator#createProxy(Object)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Connectivity {
}