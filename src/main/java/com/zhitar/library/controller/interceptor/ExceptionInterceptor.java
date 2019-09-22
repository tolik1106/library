package com.zhitar.library.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Exception interceptor for exception which will thrown
 * in {@link com.zhitar.library.controller.MainController#service(HttpServletRequest, HttpServletResponse)}
 * method.
 */
public interface ExceptionInterceptor {

    /**
     * Return string representation of view
     * @param request <code>HttpServletRequest</code>
     * @param response <code>HttpServletResponse</code>
     * @param e exception to be intercept
     * @return view name
     */
    String afterException(HttpServletRequest request, HttpServletResponse response, Throwable e);

}
