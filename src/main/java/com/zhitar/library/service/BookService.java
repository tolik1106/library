package com.zhitar.library.service;

import com.zhitar.library.dao.attributedao.AttributeDao;
import com.zhitar.library.dao.authordao.AuthorDao;
import com.zhitar.library.dao.auxiliarydao.BookAttributeDAO;
import com.zhitar.library.dao.auxiliarydao.BookAuthorDAO;
import com.zhitar.library.dao.bookdao.BookDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.domain.*;
import com.zhitar.library.exception.NotFoundException;
import com.zhitar.library.sql.TransactionHandler;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {

    private static final Logger LOG = Logger.getLogger(BookService.class);

    private AuthorDao authorDAO;
    private BookDao bookDAO;
    private AttributeDao attributeDAO;
    private UserDao userDAO;
    private BookAuthorDAO bookAuthorDAO;
    private BookAttributeDAO bookAttributeDAO;


    public BookService(AuthorDao authorDAO, BookDao bookDAO, AttributeDao attributeDAO, UserDao userDAO, BookAuthorDAO bookAuthorDAO, BookAttributeDAO bookAttributeDAO) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
        this.attributeDAO = attributeDAO;
        this.userDAO = userDAO;
        this.bookAuthorDAO = bookAuthorDAO;
        this.bookAttributeDAO = bookAttributeDAO;
    }

    public Collection<Book> findByAuthor(String authorName) {
        LOG.info("findByAuthor: " + authorName);
        return TransactionHandler.readOnlyExecute(conn -> {
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
        });
    }

    public Collection<Book> findByNameWithAuthor(String name) {
        LOG.info("findByNameWithAuthor: " + name);
        return TransactionHandler.readOnlyExecute(conn -> {
            Collection<Book> books = bookDAO.findByNameWithAuthor(name);
            if (books.isEmpty()) {
                LOG.error("Book with name '" + name + "' not found");
                throw new NotFoundException("message.notfound.error");
            }
            return books;
        });
    }

    public Collection<Book> findByAttribute(String attribute) {
        LOG.info("findByAttribute: " + attribute);
        return TransactionHandler.readOnlyExecute(conn -> {
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
        });
    }

    public Book takeBook(Integer bookId, Integer userId) {
        LOG.info("takeBook with id: " + bookId + " by user with id: " + userId);
        return TransactionHandler.transactionExecute(conn -> {
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

            return bookDAO.update(book);
        });
    }

    public Collection<Book> findAll() {
        LOG.info("findAll");
        return TransactionHandler.readOnlyExecute(conn ->
            bookDAO.findAllWithAuthor()
        );
    }

    public Collection<Book> findByUser(Integer userId) {
        LOG.info("findByUser with id: " + userId);
        return TransactionHandler.readOnlyExecute(conn ->
                bookDAO.findByUser(userId)
        );
    }
}
