package com.zhitar.library.controller.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents single endpoint for http request
 */
public interface Action {

    /**
     * This method contains endpoint's logic.
     * @param request <code>HttpServletRequest</code>
     * @param response <code>HttpServletResponse</code>
     * @return string name of view page
     */
    String execute(HttpServletRequest request, HttpServletResponse response);
}
