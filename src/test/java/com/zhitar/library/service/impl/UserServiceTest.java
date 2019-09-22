
package com.zhitar.library.service.impl;

import com.zhitar.library.dao.auxiliarydao.UserRoleDao;
import com.zhitar.library.dao.roledao.RoleDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.domain.User;
import com.zhitar.library.domain.UserRole;
import com.zhitar.library.exception.DaoException;
import com.zhitar.library.exception.NotFoundException;
import com.zhitar.library.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.zhitar.library.TestData.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private RoleDao roleDao;
    @Mock
    private UserRoleDao userRoleDao;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl(userDao, roleDao, userRoleDao);
    }

    @Test
    public void findByEmailTest() throws Exception {
        String email = "marina@i.ua";
        when(userDao.findByEmail(email)).thenReturn(USER);
        User byEmail = userService.findByEmail("marina@i.ua");
        assertEquals(byEmail, USER);
        assertThat(byEmail, hasProperty("email", is(email)));
    }

    @Test(expected = NotFoundException.class)
    public void findByEmailNotFoundTest() throws Exception {
        String wrongEmail = "mar@i.ua";
        when(userDao.findByEmail(wrongEmail)).thenReturn(null);
        userService.findByEmail(wrongEmail);
    }

    @Test
    public void saveTest() {
        User user = userToSave();
        User saved = userToSave();
        saved.setId(1);
        when(userDao.save(user)).thenReturn(saved);
        when(roleDao.findAll()).thenReturn(getAllRoles());
        List<UserRole> userRoles = getUserRoles();
        when(userRoleDao.save(userRoles)).thenReturn(userRoles);
        User savedUser = userService.save(user);
        assertEquals(saved, savedUser);
    }

    @Test(expected = DaoException.class)
    public void saveDuplicateEmailTest() {
        User user = userToSave();
        when(userDao.save(user)).thenThrow(DaoException.class);
        userService.save(user);
    }

}