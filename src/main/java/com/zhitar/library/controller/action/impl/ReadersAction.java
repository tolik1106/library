package com.zhitar.library.controller.action.impl;

import com.zhitar.library.Container;
import com.zhitar.library.controller.action.AbstractAction;
import com.zhitar.library.domain.User;
import com.zhitar.library.service.AdminService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

public class ReadersAction extends AbstractAction {

    private static final Logger LOG = Logger.getLogger(ReadersAction.class);

    private AdminService adminService = (AdminService) Container.getInstance().getBean("adminService");

    @Override
    protected String doGet(HttpServletRequest request, HttpServletResponse response) {
        Collection<User> usersWithBook = adminService.findUsersWithBook();
        LOG.info("doGet method. Found users: " + usersWithBook);
        request.setAttribute("users", usersWithBook);
        return "readers";
    }

    @Override
    protected String doPost(HttpServletRequest request, HttpServletResponse response) {
        String userStr = request.getParameter("userId");
        String bookStr = request.getParameter("bookId");
        LOG.info("doPost with userId: " + userStr + " and bookId: " + bookStr);
        Integer userId = Integer.valueOf(userStr);
        Integer bookId = Integer.valueOf(bookStr);
        adminService.returnBook(bookId, userId);
        return "redirect:admin/readers";
    }
}
