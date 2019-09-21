package com.zhitar.library.controller.interceptor;

public interface ControllerExceptionInterceptor extends ExceptionInterceptor {
    boolean canHandle(Throwable ex);
}
