package com.zhitar.library.controller.action;

import com.zhitar.library.Container;
import com.zhitar.library.domain.Attribute;
import com.zhitar.library.domain.Author;
import com.zhitar.library.domain.Book;
import com.zhitar.library.validation.ValidationResult;
import com.zhitar.library.validation.ValidationService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookRequestEntityResolver implements RequestEntityResolver<Book>, RequestEntityChecker<Book> {

    private ValidationService validator = Container.getInstance().getBean(ValidationService.class);

    @Override
    public Book resolveEntity(HttpServletRequest request) {
        String id = request.getParameter("id");
        Integer bookId = id == null ? null : Integer.valueOf(id);
        String name = request.getParameter("name");
        String bookcase = request.getParameter("bookcase");
        String bookshelf = request.getParameter("bookshelf");
        String authors = request.getParameter("authors");
        String attributes = request.getParameter("attributes");
        List<Author> authorList = getList(authors, (val) -> new Author(null, val));
        List<Attribute> attributeList = getList(attributes, (val) -> new Attribute(null, val));

        Book book = new Book(bookId, name, null, Integer.valueOf(bookcase), Integer.valueOf(bookshelf));

        book.setAuthors(authorList);
        book.setAttributes(attributeList);
        return book;
    }

    private <T> List<T> getList(String values, Function<String, T> mapper) {
        String[] split = values.split(",");

        List<String> names = Arrays.stream(split).map(String::trim).collect(Collectors.toList());
        TreeSet<String> seen = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        names.removeIf(name -> !seen.add(name));

        return names.stream().map(mapper).collect(Collectors.toList());
    }

    public boolean check(Book book, HttpServletRequest request) {
        ValidationResult bookValidation = validator.validate(Book.class, book);

        boolean isError = false;
        if (bookValidation.hasErrors()) {
            isError = true;
            while (bookValidation.hasErrors()) {
                request.setAttribute(bookValidation.getField(), bookValidation.getMessage());
                bookValidation.next();
            }
        }
        for (Author author : book.getAuthors()) {
            ValidationResult authorValidation = validator.validate(Author.class, author);
            if (authorValidation.hasErrors()) {
                isError = true;
                request.setAttribute(authorValidation.getField(), authorValidation.getMessage());
            }
        }
        for (Attribute attribute : book.getAttributes()) {
            ValidationResult attributeValidation = validator.validate(Attribute.class, attribute);
            if (attributeValidation.hasErrors()) {
                isError = true;
                request.setAttribute(attributeValidation.getField(), attributeValidation.getMessage());
            }
        }
        return isError;
    }
}
