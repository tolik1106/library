package com.zhitar.library.service.impl;

import com.zhitar.library.annotation.Transaction;
import com.zhitar.library.dao.attributedao.AttributeDao;
import com.zhitar.library.dao.authordao.AuthorDao;
import com.zhitar.library.dao.auxiliarydao.BookAttributeDAO;
import com.zhitar.library.dao.auxiliarydao.BookAuthorDAO;
import com.zhitar.library.dao.bookdao.BookDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.domain.*;
import com.zhitar.library.exception.NotFoundException;
import com.zhitar.library.service.AdminService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AdminServiceImpl implements AdminService {

    private static final Logger LOG = Logger.getLogger(AdminServiceImpl.class);

    private AuthorDao authorDAO;
    private BookDao bookDAO;
    private AttributeDao attributeDAO;
    private UserDao userDAO;
    private BookAuthorDAO bookAuthorDAO;
    private BookAttributeDAO bookAttributeDAO;

    public AdminServiceImpl(AuthorDao authorDAO, BookDao bookDAO, AttributeDao attributeDAO, UserDao userDAO, BookAuthorDAO bookAuthorDAO, BookAttributeDAO bookAttributeDAO) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
        this.attributeDAO = attributeDAO;
        this.userDAO = userDAO;
        this.bookAuthorDAO = bookAuthorDAO;
        this.bookAttributeDAO = bookAttributeDAO;
    }

    @Transaction
    @Override
    public boolean deleteBookById(Integer id) {
        LOG.info("deleteBookById book with id: " + id);
        return bookDAO.delete(id);
    }

    @Transaction(readOnly = true)
    @Override
    public Book findBookById(Integer id) {
        LOG.info("findBookById: " + id);
        LOG.debug("find single book");
        Book book = bookDAO.findById(id);
        if (book == null) {
            LOG.error("Book with id " + id + " not found");
            throw new NotFoundException("Book with id " + id + " not found");
        }

        LOG.debug("find authors for book: " + book);
        List<BookAuthor> bookAuthors = bookAuthorDAO.findByBookId(id);

        List<Author> authors = authorDAO.findById(bookAuthors.stream().map(BookAuthor::getAuthorId).collect(Collectors.toList()));
        book.setAuthors(authors);

        LOG.debug("find attributes for book: " + book);
        List<BookAttribute> bookAttributes = bookAttributeDAO.findByBookId(id);
        List<Attribute> attributes = attributeDAO.findById(bookAttributes.stream().map(BookAttribute::getAttributeId).collect(Collectors.toList()));
        book.setAttributes(attributes);
        return book;
    }

    @Transaction
    @Override
    public Book save(Book book) {
        LOG.info("save " + book);
        LOG.debug("save single book");
        Book savedBook = bookDAO.save(book);

        LOG.debug("Get already exist authors");
        List<Author> toInsert = new ArrayList<>();
        List<BookAuthor> bookAuthors = new ArrayList<>();

        fillAuthorsToSave(book, savedBook, toInsert, bookAuthors);

        LOG.debug("Save not existing authors: " + toInsert);
        List<Author> savedAuthors = authorDAO.save(toInsert);

        for (Author savedAuthor : savedAuthors) {
            bookAuthors.add(new BookAuthor(savedBook.getId(), savedAuthor.getId()));
        }
        LOG.debug("Save join table " + bookAuthors);
        bookAuthorDAO.save(bookAuthors);

        LOG.debug("Get already exist attributes");
        List<Attribute> inserts = new ArrayList<>();
        List<BookAttribute> bookAttributes = new ArrayList<>();
        fillAttributesToSave(book, savedBook, inserts, bookAttributes);

        LOG.debug("Save not existing attributes: " + inserts);
        List<Attribute> savedAttr = attributeDAO.save(inserts);

        for (Attribute attribute : savedAttr) {
            bookAttributes.add(new BookAttribute(savedBook.getId(), attribute.getId()));
        }
        LOG.debug("Save join table " + bookAttributes);
        bookAttributeDAO.save(bookAttributes);
        return book;
    }

    private void fillAuthorsToSave(Book book, Book savedBook, List<Author> toInsert, List<BookAuthor> bookAuthors) {
        for (Author author : book.getAuthors()) {
            Author authorFromDb = authorDAO.findByName(author.getName());
            if (authorFromDb == null) {
                toInsert.add(author);
            } else {
                bookAuthors.add(new BookAuthor(savedBook.getId(), authorFromDb.getId()));
            }
        }
    }

    private void fillAttributesToSave(Book book, Book updatedBook, List<Attribute> attrToInsert, List<BookAttribute> bookAttributes) {
        for (Attribute attribute : book.getAttributes()) {
            Attribute attrFromDb = attributeDAO.findByAttribute(attribute.getName());
            if (attrFromDb == null) {
                attrToInsert.add(attribute);
            } else {
                bookAttributes.add(new BookAttribute(updatedBook.getId(), attrFromDb.getId()));
            }
        }
    }

    @Transaction
    @Override
    public Book update(Book book) {
        LOG.info("update " + book);
        LOG.debug("Check book to update");
        Book bookFromDb = bookDAO.findById(book.getId());
        if (bookFromDb == null) {
            LOG.error("Book with id " + book.getId() + " doesn't exist");
            throw new NotFoundException("Book with id " + book.getId() + " doesn't exist");
        }
        book.setOwnedDate(bookFromDb.getOwnedDate());
        LOG.debug("update single book");
        Book updatedBook = bookDAO.update(book);


        LOG.debug("clear book_author table for this book");
        bookAuthorDAO.delete(updatedBook.getId());

        LOG.debug("update authors");
        List<Author> toInsert = new ArrayList<>();
        List<BookAuthor> bookAuthors = new ArrayList<>();

        fillAuthorsToSave(book, updatedBook, toInsert, bookAuthors);

        LOG.debug("Save not existing authors: " + toInsert);
        List<Author> savedAuthors = authorDAO.save(toInsert);
        for (Author savedAuthor : savedAuthors) {
            bookAuthors.add(new BookAuthor(updatedBook.getId(), savedAuthor.getId()));
        }

        LOG.debug("Save join table " + bookAuthors);
        bookAuthorDAO.save(bookAuthors);

        LOG.debug("clear book_attributes for this book");
        bookAttributeDAO.delete(updatedBook.getId());

        LOG.debug("update attributes");
        List<Attribute> attrToInsert = new ArrayList<>();
        List<BookAttribute> bookAttributes = new ArrayList<>();

        fillAttributesToSave(book, updatedBook, attrToInsert, bookAttributes);

        LOG.debug("Save not existing attributes: " + attrToInsert);
        List<Attribute> savedAttributes = attributeDAO.save(attrToInsert);
        for (Attribute savedAttribute : savedAttributes) {
            bookAttributes.add(new BookAttribute(updatedBook.getId(), savedAttribute.getId()));
        }

        LOG.debug("Save join table " + bookAttributes);
        bookAttributeDAO.save(bookAttributes);
        return updatedBook;
    }


    @Transaction(readOnly = true)
    @Override
    public Collection<User> findUsersWithBook() {
        LOG.info("findUsersWithBook");
        return userDAO.findAllWithBooks();
    }

    @Transaction
    @Override
    public Book returnBook(Integer bookId, Integer userId) {
        LOG.info("returnBook with id " + bookId + " by user " + userId);
        User user = userDAO.findById(userId);
        if (user == null) {
            LOG.error("User with id " + userId + " not found");
            throw new NotFoundException("User with id" + userId + " not found");
        }

        Book book = bookDAO.findById(bookId);
        if (book == null) {
            LOG.error("Book with id " + bookId + " not found");
            throw new NotFoundException("Book with id " + bookId + " not found");
        }

        book.setOwner(null);
        book.setOwnedDate(null);
        return bookDAO.update(book);
    }
}
