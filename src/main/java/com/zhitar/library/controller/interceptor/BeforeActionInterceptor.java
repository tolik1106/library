package com.zhitar.library.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of this interface should pass attributes
 * to HttpServletRequest or HttpServletResponse before
 * the {@link com.zhitar.library.controller.action.Action#execute(HttpServletRequest, HttpServletResponse)}
 * method will invoke
 */
public interface BeforeActionInterceptor {
    /**
     * this method will invoke first in {@link com.zhitar.library.controller.MainController#service(HttpServletRequest, HttpServletResponse)}
     * method for each classes that implements this interface.
     * @param request <code>HttpServletRequest</code>
     * @param response <code>HttpServletResponse</code>
     */
    void beforeAction(HttpServletRequest request, HttpServletResponse response);
}
