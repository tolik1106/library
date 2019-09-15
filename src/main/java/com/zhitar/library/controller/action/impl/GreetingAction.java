package com.zhitar.library.controller.action.impl;

import com.zhitar.library.controller.action.Action;
import com.zhitar.library.util.PropertiesUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GreetingAction implements Action {

    private static final Logger LOG = Logger.getLogger(GreetingAction.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("execute method");
        return PropertiesUtil.getValue("app.greeting.page");
    }
}
