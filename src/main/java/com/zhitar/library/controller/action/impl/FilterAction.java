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

public class FilterAction implements Action {

    private static final Logger LOG = Logger.getLogger(FilterAction.class);

    private BookService bookService = (BookService) Container.getInstance().getBean("bookService");

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("execute method");
        Collection<Book> books = null;
        String name = request.getParameter("name");
        if (name != null) {
            LOG.debug("Request with name: " + name);
            books = bookService.findByNameWithAuthor(name);
            LOG.debug("found books: " + books);
            request.setAttribute("name", name);
        } else {
            String author = request.getParameter("author");
            if (author != null) {
                LOG.debug("Request with author: " + author);
                books = bookService.findByAuthor(author);
                LOG.debug("found books: " + books);
                request.setAttribute("author", author);
            } else {
                String attribute = request.getParameter("attribute");
                if (attribute != null) {
                    LOG.debug("Request with attribute: " + attribute);
                    request.setAttribute("attribute", attribute);
                    books = bookService.findByAttribute(attribute);
                    LOG.debug("found books: " + books);
                } else {
                    throw new IllegalArgumentException("Bad parameter for search");
                }
            }
        }
        User currentUser = (User) request.getSession().getAttribute("user");
        Collection<Book> currentUserBooks = bookService.findByUser(currentUser.getId());
        LOG.debug("Books for user " + currentUser + ": " + currentUserBooks);
        request.setAttribute("books", books);
        request.setAttribute("currentUserBooks", currentUserBooks);

        return PropertiesUtil.getValue("app.bookList.page");
    }
}
