package com.zhitar.library.dao.daofactory;

import com.zhitar.library.dao.attributedao.AttributeDao;
import com.zhitar.library.dao.authordao.AuthorDao;
import com.zhitar.library.dao.auxiliarydao.BookAttributeDao;
import com.zhitar.library.dao.auxiliarydao.BookAuthorDao;
import com.zhitar.library.dao.auxiliarydao.UserRoleDao;
import com.zhitar.library.dao.bookdao.BookDao;
import com.zhitar.library.dao.roledao.RoleDao;
import com.zhitar.library.dao.userdao.UserDao;

/**
 * Abstract factory for each dao
 */
public abstract class AbstractDAOFactory {

    public abstract UserDao getUserDAO();
    public abstract RoleDao getRoleDAO();
    public abstract AttributeDao getAttributeDAO();
    public abstract AuthorDao getAuthorDAO();
    public abstract BookDao getBookDAO();
    public abstract BookAttributeDao getBookAttributeDAO();
    public abstract BookAuthorDao getBookAuthorDAO();
    public abstract UserRoleDao getUserRoleDAO();
}
