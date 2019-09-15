package com.zhitar.library.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BeforeActionInterceptor {
    void beforeAction(HttpServletRequest request, HttpServletResponse response);
}
