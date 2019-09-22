package com.zhitar.library.annotation;

import com.zhitar.library.exception.TransactionException;
import com.zhitar.library.sql.TransactionHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * This class creates proxy object for {@link Connectivity} annotated
 * classes to use with database connection
 */
public class TransactionProxyCreator {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T service) {
        if (service.getClass().getAnnotation(Connectivity.class) != null) {
            return (T) Proxy.newProxyInstance(
                    service.getClass().getClassLoader(),
                    service.getClass().getInterfaces(),
                    new TransactionInvocationHandler(service)
            );
        }
        return service;
    }

    private static class TransactionInvocationHandler implements InvocationHandler {

        private final Object target;

        TransactionInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method realMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
            Transaction annotation = realMethod.getAnnotation(Transaction.class);
            int type = TransactionHandler.WITHOUT_TRANSACTION;

            if (annotation != null) {
                boolean readOnly = annotation.readOnly();
                if (readOnly) {
                    type = TransactionHandler.READ_ONLY;
                } else {
                    type = TransactionHandler.IN_TRANSACTION;
                }
            }
            return TransactionHandler.execute(() -> {
                try {
                    return realMethod.invoke(target, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new TransactionException(e);
                }
            }, type);
        }
    }
}
