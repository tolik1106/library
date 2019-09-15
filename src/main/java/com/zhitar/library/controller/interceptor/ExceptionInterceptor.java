package com.zhitar.library.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExceptionInterceptor {

    String afterException(HttpServletRequest request, HttpServletResponse response, Exception ex);

}
