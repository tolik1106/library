package com.zhitar.library.controller.interceptor;

/**
 * Implement this interface and add to list {@link ControllerInterceptorService#exceptionInterceptors}
 * if you want to intercept special exception
 */
public interface ControllerExceptionInterceptor extends ExceptionInterceptor {
    /**
     * Check if this exception can be intercepted
     * @param ex exception for intercept
     * @return <code>true</code> if this exception can be intercepted
     *          and <code>false</code> otherwise
     */
    boolean canHandle(Throwable ex);
}
