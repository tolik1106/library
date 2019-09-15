package com.zhitar.library.controller.action.impl;

import com.zhitar.library.Container;
import com.zhitar.library.controller.action.AbstractAction;
import com.zhitar.library.controller.action.BookRequestEntityResolver;
import com.zhitar.library.domain.Book;
import com.zhitar.library.service.AdminService;
import com.zhitar.library.util.PropertiesUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BookSaveAction extends AbstractAction {

    private static final Logger LOG = Logger.getLogger(BookSaveAction.class);

    private AdminService adminService = Container.getInstance().getBean(AdminService.class);

    @Override
    protected String doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("doGet method");
        request.setAttribute("book", new Book());
        return PropertiesUtil.getValue("app.book.page");
    }

    @Override
    protected String doPost(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("doPost method");
        BookRequestEntityResolver bookResolver = new BookRequestEntityResolver();
        Book book = bookResolver.resolveEntity(request);
        LOG.debug("Resolved book " + book);
        boolean hasErrors = bookResolver.check(book, request);
        LOG.debug("Book was checked with errors: " + hasErrors);
        if (hasErrors) {
            request.setAttribute("book", book);
            return PropertiesUtil.getValue("app.book.page");
        }

        adminService.save(book);
        return "redirect:books";
    }
}
