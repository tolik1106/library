package com.zhitar.library;

import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.Role;
import com.zhitar.library.domain.User;
import com.zhitar.library.domain.UserRole;

import java.util.*;

public class TestData {

    public static final User ADMIN = new User(1, "tolik1106", "tolik1106@gmail.com", "0950160701");
    public static final User USER = new User(2, "marina", "marina@i.ua", "0956842354");

    public static final List<Role> ROLES = new ArrayList<Role>() {
        {
            add(new Role(1, "ADMIN"));
            add(new Role(2, "USER"));
        }
    };

    public static final Collection<Book> BOOKS = new ArrayList<Book>() {
        {
            add(new Book(1, "Harry Potter and the Philosopher's Stone", null, 1, 1));
            addAll(getUserBooks());
            add(new Book(4, "Harry Potter and the Goblet of Fire", null, 1, 1));
            addAll(getAdminBooks());
        }
    };

    public static List<Role> getAllRoles() {
        return ROLES;
    }

    public static List<UserRole> getUserRoles() {
        return new ArrayList<UserRole>() {
            {
                add(new UserRole(2, 1));
            }
        };
    }

    public static User userToSave() {
        User tolik = new User(null, "tolik", "user@user.com", "0951234567");
        tolik.setRoles(Collections.singleton(new Role(null, "USER")));
        return tolik;
    }

    public static Collection<Book> getAdminBooks() {
        Book book1 = new Book(5, "Harry Potter and the Order of the Phoenix", new Date(), 1, 2);
        Book book2 = new Book(6, "Harry Potter and the Half-Blood Prince", new Date(), 1, 2);
        book1.setOwner(ADMIN);
        book2.setOwner(ADMIN);
        Collection<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        return books;
    }

    public static Collection<Book> getUserBooks() {
        Book book1 = new Book(2, "Harry Potter and the Chamber of Secrets", new Date(), 1, 1);
        Book book2 = new Book(3, "Harry Potter and the Prisoner of Azkaban", new Date(), 1, 1);
        book1.setOwner(USER);
        book2.setOwner(USER);
        Collection<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        return books;
    }
}
