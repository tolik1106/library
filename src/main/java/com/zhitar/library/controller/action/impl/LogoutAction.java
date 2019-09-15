package com.zhitar.library.controller.action.impl;

import com.zhitar.library.controller.action.Action;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutAction implements Action {

    private static final Logger LOG = Logger.getLogger(LoginAction.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("execute method. Invalidate session.");
        request.getSession().invalidate();
        return "redirect:login";
    }
}
