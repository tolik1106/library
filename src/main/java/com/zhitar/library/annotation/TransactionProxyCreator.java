package com.zhitar.library.annotation;

import com.zhitar.library.sql.TransactionHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class TransactionProxyCreator {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T service) {
        Method[] methods = service.getClass().getMethods();
        for (Method method : methods) {
            if (method.getAnnotation(Transaction.class) != null) {
                return (T) Proxy.newProxyInstance(
                        service.getClass().getClassLoader(),
                        service.getClass().getInterfaces(),
                        new TransactionInvocationHandler(service)
                );
            }
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

            if (annotation != null) {
                boolean readOnly = annotation.readOnly();
                if (readOnly) {
                    return TransactionHandler.readOnlyExecute(() -> {
                        try {
                            return realMethod.invoke(target, args);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
                } else {
                    return TransactionHandler.transactionExecute(() -> {
                        try {
                            return realMethod.invoke(target, args);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
                }
            }
            return TransactionHandler.execute(() -> {
                try {
                    return realMethod.invoke(target, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            });
        }
    }
}
