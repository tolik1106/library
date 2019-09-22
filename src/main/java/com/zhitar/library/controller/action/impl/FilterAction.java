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
import javax.servlet.http.HttpSession;
import java.util.Collection;

import static com.zhitar.library.util.BookActionUtil.checkIsDebtor;

public class FilterAction implements Action {

    private static final Logger LOG = Logger.getLogger(FilterAction.class);
    private static final String NAME = "name";
    private static final String AUTHOR = "author";
    private static final String ATTRIBUTE = "attribute";

    private BookService bookService = (BookService) Container.getInstance().getBean("bookService");

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("execute method");
        Collection<Book> books = null;
        String name = request.getParameter(NAME);
        if (name != null) {
            removeAttributes(request, AUTHOR, ATTRIBUTE);
            request.getSession().setAttribute(NAME, name);
            LOG.debug("Request with name: " + name);
            books = bookService.findByNameWithAuthor(name);
            LOG.debug("found books: " + books);
            return processRequest(request, books);
        }

        String author = request.getParameter(AUTHOR);
        if (author != null) {
            removeAttributes(request, NAME, ATTRIBUTE);
            request.getSession().setAttribute(AUTHOR, author);
            LOG.debug("Request with author: " + author);
            books = bookService.findByAuthor(author);
            LOG.debug("found books: " + books);
            return processRequest(request, books);
        }

        String attribute = request.getParameter(ATTRIBUTE);
        if (attribute != null) {
            removeAttributes(request, NAME, AUTHOR);
            request.getSession().setAttribute(ATTRIBUTE, attribute);
            LOG.debug("Request with attribute: " + attribute);
            books = bookService.findByAttribute(attribute);
            LOG.debug("found books: " + books);
            return processRequest(request, books);
        }
        if (request.getQueryString().startsWith("lang=")) {
            return "redirect:books";
        }
        throw new IllegalArgumentException("Bad parameter for search");
    }

    private String processRequest(HttpServletRequest request, Collection<Book> books) {
        User currentUser = (User) request.getSession().getAttribute("user");
        Collection<Book> currentUserBooks = bookService.findByUser(currentUser.getId());
        LOG.debug("Books for user " + currentUser + ": " + currentUserBooks);
        request.setAttribute("books", books);
        request.setAttribute("currentUserBooks", currentUserBooks);

        checkIsDebtor(request, currentUserBooks);

        return PropertiesUtil.getValue("app.bookList.page");
    }

    private void removeAttributes(HttpServletRequest request, String... attributes) {
        HttpSession session = request.getSession();
        for (String attribute : attributes) {
            session.removeAttribute(attribute);
        }
    }
}