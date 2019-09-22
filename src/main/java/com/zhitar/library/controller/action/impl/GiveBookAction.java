package com.zhitar.library.controller.action.impl;

import com.zhitar.library.Container;
import com.zhitar.library.controller.action.Action;
import com.zhitar.library.service.AdminService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GiveBookAction implements Action {

    private static final Logger LOG = Logger.getLogger(GiveBookAction.class);

    private AdminService adminService = Container.getInstance().getBean(AdminService.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String userStr = request.getParameter("userId");
        String bookStr = request.getParameter("bookId");
        LOG.info("execute with userId: " + userStr + " and bookId: " + bookStr);
        Integer userId = Integer.valueOf(userStr);
        Integer bookId = Integer.valueOf(bookStr);
        adminService.giveBook(userId, bookId);
        return "redirect:admin/readers";
    }
}