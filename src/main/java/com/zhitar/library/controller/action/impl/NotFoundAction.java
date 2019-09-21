package com.zhitar.library.controller.action.impl;

import com.zhitar.library.controller.action.Action;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotFoundAction implements Action {

    private static final Logger LOG = Logger.getLogger(NotFoundAction.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("execute method with servlet paht: " + request.getServletPath() + ". Send to error page");
        String exception = (String) request.getSession().getAttribute("exceptionMessage");
        if (exception == null) {
            request.getSession().setAttribute("cause", "Sorry, this page isn't available.");
        } else {
            request.getSession().setAttribute("cause", exception);
            request.getSession().removeAttribute("exceptionMessage");
        }
        return "error";
    }
}
