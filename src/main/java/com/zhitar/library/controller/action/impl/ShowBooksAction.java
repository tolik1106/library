package com.zhitar.library.controller.action.impl;

import com.zhitar.library.Container;
import com.zhitar.library.controller.action.Action;
import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.User;
import com.zhitar.library.service.BookService;
import com.zhitar.library.util.PropertiesUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

public class ShowBooksAction implements Action {

    private static final Logger LOG = Logger.getLogger(ShowBooksAction.class);

    private BookService bookService = (BookService) Container.getInstance().getBean("bookService");

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Integer page = resolvePage(request);

        Collection<Book> books = bookService.findAll(page);
        LOG.info("execute method. Found books: " + books);
        User currentUser = (User) request.getSession().getAttribute("user");
        LOG.debug("get user from session: " + currentUser);
        Collection<Book> currentUserBooks = bookService.findByUser(currentUser.getId());
        LOG.debug("found user books: " + currentUserBooks);
        request.setAttribute("books", books);
        request.setAttribute("currentUserBooks", currentUserBooks);
        long count = bookService.count();
        request.setAttribute("count", count);
        request.setAttribute("currentPage", page);

        return PropertiesUtil.getValue("app.bookList.page");
    }

    private Integer resolvePage(HttpServletRequest request) {
        String pageStr = request.getParameter("page");
        Integer page = null;
        if (pageStr != null) {
            page = Integer.valueOf(pageStr);
            request.getSession().setAttribute("page", page);
        } else {
            page = (Integer) request.getSession().getAttribute("page");
            if (page == null) {
                page = 0;
                request.getSession().setAttribute("page", page);
            }
        }
        return page;
    }
}
