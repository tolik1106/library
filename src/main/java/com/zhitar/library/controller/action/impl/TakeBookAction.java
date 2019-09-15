package com.zhitar.library.controller.action.impl;

import com.zhitar.library.Container;
import com.zhitar.library.controller.action.Action;
import com.zhitar.library.domain.User;
import com.zhitar.library.service.BookService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TakeBookAction implements Action {

    private static final Logger LOG = Logger.getLogger(TakeBookAction.class);

    private BookService bookService = (BookService) Container.getInstance().getBean("bookService");

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User currentUser = (User) request.getSession().getAttribute("user");
        LOG.info("execute method");
        String uri = request.getRequestURI();
        Integer bookId = Integer.valueOf(uri.substring(uri.lastIndexOf('/') + 1));
        LOG.debug("book id: " + bookId);
        bookService.takeBook(bookId, currentUser.getId());
        return "redirect:books";
    }
}
