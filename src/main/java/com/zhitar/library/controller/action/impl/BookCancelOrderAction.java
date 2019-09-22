package com.zhitar.library.controller.action.impl;

import com.zhitar.library.Container;
import com.zhitar.library.controller.action.Action;
import com.zhitar.library.domain.User;
import com.zhitar.library.service.BookService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BookCancelOrderAction implements Action {

    private static final Logger LOG = Logger.getLogger(BookCancelOrderAction.class);

    private BookService bookService = Container.getInstance().getBean(BookService.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String bookStr = request.getParameter("id");
        LOG.info("execute with bookId: " + bookStr);
        Integer bookId = Integer.valueOf(bookStr);
        User user = (User) request.getSession().getAttribute("user");
        bookService.cancelOrder(bookId, user.getId());
        return "redirect:books";
    }
}