package com.zhitar.library.controller.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractAction implements Action {

    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        switch (request.getMethod()) {
            case GET_METHOD:
                return doGet(request, response);
            case POST_METHOD:
                return doPost(request, response);
            default:
                throw new RuntimeException("Method " + request.getMethod() + " not supported by " + this.getClass().getName());
        }
    }

    protected abstract String doGet(HttpServletRequest request, HttpServletResponse response);

    protected abstract String doPost(HttpServletRequest request, HttpServletResponse response);
}
