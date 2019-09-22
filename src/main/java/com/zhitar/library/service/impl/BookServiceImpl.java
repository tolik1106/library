package com.zhitar.library.service.impl;

import com.zhitar.library.annotation.Connectivity;
import com.zhitar.library.annotation.Transaction;
import com.zhitar.library.dao.attributedao.AttributeDao;
import com.zhitar.library.dao.authordao.AuthorDao;
import com.zhitar.library.dao.auxiliarydao.BookAttributeDao;
import com.zhitar.library.dao.auxiliarydao.BookAuthorDao;
import com.zhitar.library.dao.bookdao.BookDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.domain.*;
import com.zhitar.library.exception.NotFoundException;
import com.zhitar.library.service.BookService;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Connectivity
public class BookServiceImpl implements BookService {

    private static final Logger LOG = Logger.getLogger(BookServiceImpl.class);

    private AuthorDao authorDAO;
    private BookDao bookDAO;
    private AttributeDao attributeDAO;
    private UserDao userDAO;
    private BookAuthorDao bookAuthorDAO;
    private BookAttributeDao bookAttributeDAO;


    public BookServiceImpl(AuthorDao authorDAO, BookDao bookDAO, AttributeDao attributeDAO, UserDao userDAO, BookAuthorDao bookAuthorDAO, BookAttributeDao bookAttributeDAO) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
        this.attributeDAO = attributeDAO;
        this.userDAO = userDAO;
        this.bookAuthorDAO = bookAuthorDAO;
        this.bookAttributeDAO = bookAttributeDAO;
    }

    @Transaction(readOnly = true)
    @Override
    public Collection<Book> findByAuthor(String authorName) {
        LOG.info("findByAuthor: " + authorName);
        Author author = authorDAO.findByName(authorName);
        if (author == null) {
            LOG.error("Author with name '" + authorName + "' not found");
            throw new NotFoundException("message.notfound.error");
        }

        LOG.debug("find books for author: " + authorName);
        List<BookAuthor> bookAuthors = bookAuthorDAO.findByAuthorId(author.getId());
        if (bookAuthors.isEmpty()) {
            LOG.error("Author with name '" + authorName + "' not found");
            throw new NotFoundException("message.notfound.error");
        }
        return bookDAO.findById(bookAuthors.stream().map(BookAuthor::getBookId).collect(Collectors.toList()));
    }

    @Transaction(readOnly = true)
    @Override
    public Collection<Book> findByNameWithAuthor(String name) {
        LOG.info("findByNameWithAuthor: " + name);
        Collection<Book> books = bookDAO.findByNameWithAuthor(name);
        if (books.isEmpty()) {
            LOG.error("Book with name '" + name + "' not found");
            throw new NotFoundException("message.notfound.error");
        }
        return books;
    }

    @Transaction(readOnly = true)
    @Override
    public Collection<Book> findByAttribute(String attribute) {
        LOG.info("findByAttribute: " + attribute);
        Attribute bookAttribute = attributeDAO.findByAttribute(attribute);
        if (bookAttribute == null) {
            LOG.error("Attribute '" + attribute + "' not found");
            throw new NotFoundException("message.notfound.error");
        }

        List<BookAttribute> bookAttributes = bookAttributeDAO.findByAttributeId(bookAttribute.getId());
        if (bookAttributes.isEmpty()) {
            LOG.error("Attribute '" + attribute + "' not found");
            throw new NotFoundException("message.notfound.error");
        }
        return bookDAO.findById(bookAttributes.stream().map(BookAttribute::getBookId).collect(Collectors.toList()));
    }

    @Transaction
    @Override
    public Book takeBook(Integer bookId, Integer userId) {
        LOG.info("takeBook with id: " + bookId + " by user with id: " + userId);
        User user = userDAO.findById(userId);
        if (user == null) {
            LOG.error("User with id '" + userId + "' not found");
            throw new NotFoundException("User with id '" + userId + "' not found");
        }

        Book book = bookDAO.findById(bookId);
        if (book == null) {
            LOG.error("Book with id '" + bookId + "' not found");
            throw new NotFoundException("message.notfound.error");
        }
        if (book.getOwnedDate() != null) {
            LOG.error("This book was already taken");
            throw new NotFoundException("This book was already taken");
        }
        book.setOwner(user);
        book.setOwnedDate(new Date());
        book.setOrdered(true);

        return bookDAO.update(book);
    }

    @Transaction(readOnly = true)
    @Override
    public Collection<Book> findAll() {
        LOG.info("findAll");
        return bookDAO.findAllWithAuthor();
    }

    @Transaction(readOnly = true)
    @Override
    public Collection<Book> findAll(int page) {
        long count = bookDAO.count();
        if ((page < 0) || (page > ((count - 1) / 10))) {
            throw new IllegalArgumentException("Wrong page number");
        }
        Collection<Book> books = bookDAO.findAll(page);

        for (Book book : books) {
            List<BookAuthor> bookAuthors = bookAuthorDAO.findByBookId(book.getId());
            List<Author> authors = authorDAO.findById(bookAuthors.stream().map(BookAuthor::getAuthorId).collect(Collectors.toList()));
            book.setAuthors(authors);
        }
        return books;
    }

    @Override
    public long count() {
        return bookDAO.count();
    }

    @Transaction(readOnly = true)
    @Override
    public Collection<Book> findByUser(Integer userId) {
        LOG.info("findByUser with id: " + userId);
        return bookDAO.findByUser(userId);
    }

    @Transaction
    @Override
    public Book cancelOrder(Integer bookId, Integer userId) {
        LOG.info("cancelOrder for book " + bookId + " and user: " + userId);
        User user = userDAO.findById(userId);
        if (user == null) {
            LOG.error("User with id '" + userId + "' not found");
            throw new NotFoundException("User with id '" + userId + "' not found");
        }
        Book book = bookDAO.findById(bookId);
        if (book == null) {
            LOG.error("Book with id '" + bookId + "' not found");
            throw new NotFoundException("message.notfound.error");
        }
        if (!book.getOrdered()) {
            LOG.error("This book isn't ordered");
            throw new NotFoundException("This book isn't ordered");
        }
        book.setOwner(null);
        book.setOwnedDate(null);
        book.setOrdered(false);
        return bookDAO.update(book);
    }
}
