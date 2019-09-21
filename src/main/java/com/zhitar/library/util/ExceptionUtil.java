package com.zhitar.library.util;

public class ExceptionUtil {

    private ExceptionUtil() {}

    public static Throwable getRootCause(Throwable ex) {
        while (ex.getCause() != null) {
            ex = ex.getCause();
        }
        return ex;
    }
}