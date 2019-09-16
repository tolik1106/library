package com.zhitar.library.controller.action.impl;

import com.zhitar.library.Container;
import com.zhitar.library.controller.action.Action;
import com.zhitar.library.service.AdminService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteAction implements Action {

    private static final Logger LOG = Logger.getLogger(DeleteAction.class);

    private AdminService adminService = (AdminService) Container.getInstance().getBean("adminService");

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();
        LOG.info("execute with servlet path " + path);
        Integer bookId = Integer.valueOf(path.substring(path.lastIndexOf('/') + 1));
        adminService.deleteBookById(bookId);
        return "redirect:books";
    }
}
