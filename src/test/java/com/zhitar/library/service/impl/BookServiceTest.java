package com.zhitar.library.service.impl;

import com.zhitar.library.dao.attributedao.AttributeDao;
import com.zhitar.library.dao.authordao.AuthorDao;
import com.zhitar.library.dao.auxiliarydao.BookAttributeDao;
import com.zhitar.library.dao.auxiliarydao.BookAuthorDao;
import com.zhitar.library.dao.bookdao.BookDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.domain.Book;
import com.zhitar.library.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;

import static com.zhitar.library.TestData.USER;
import static com.zhitar.library.TestData.getUserBooks;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private AuthorDao authorDao;
    @Mock
    private BookDao bookDao;
    @Mock
    private AttributeDao attributeDao;
    @Mock
    private UserDao userDao;
    @Mock
    private BookAuthorDao bookAuthorDao;
    @Mock
    private BookAttributeDao bookAttributeDao;

    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        bookService = new BookServiceImpl(authorDao, bookDao, attributeDao, userDao, bookAuthorDao, bookAttributeDao);
    }

    @Test
    public void findByAuthor() {
    }

    @Test
    public void findByNameWithAuthor() {
    }

    @Test
    public void findByAttribute() {
    }

    @Test
    public void takeBook() {

    }

    @Test
    public void count() {
        long countExpected = 15L;
        when(bookDao.count()).thenReturn(countExpected);
        long count = bookService.count();
        assertEquals(countExpected, count);
    }

    @Test
    public void findByUser() {
        Collection<Book> userBooks = getUserBooks();
        when(bookDao.findByUser(USER.getId())).thenReturn(userBooks);
        Collection<Book> books = bookService.findByUser(USER.getId());
        assertThat(books, notNullValue());
        assertThat(books, hasSize(2));
        assertThat(books, is(equalTo(userBooks)));
    }
}