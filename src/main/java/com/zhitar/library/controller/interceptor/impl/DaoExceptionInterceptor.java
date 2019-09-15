package com.zhitar.library.controller.interceptor.impl;

import com.zhitar.library.controller.interceptor.ControllerExceptionInterceptor;
import com.zhitar.library.exception.DaoException;
import com.zhitar.library.util.PropertiesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DaoExceptionInterceptor implements ControllerExceptionInterceptor {
    @Override
    public boolean canHandle(Exception ex) {
        return ex.getClass() == DaoException.class;
    }

    @Override
    public String afterException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        if (ex.getMessage().contains("unique_user_email")) {
            request.setAttribute("duplicateEmailError", "message.duplicateemail.error");
            request.setAttribute("duplicateEmail", request.getParameter("email"));
            request.setAttribute("register", true);
        }
        return PropertiesUtil.getValue("app.login.page");
    }
}
