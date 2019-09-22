package com.zhitar.library.dao.daofactory;

import com.zhitar.library.dao.attributedao.AttributeDao;
import com.zhitar.library.dao.attributedao.impl.MySQLAttributeDao;
import com.zhitar.library.dao.authordao.AuthorDao;
import com.zhitar.library.dao.authordao.impl.MySQLAuthorDao;
import com.zhitar.library.dao.auxiliarydao.BookAttributeDao;
import com.zhitar.library.dao.auxiliarydao.BookAuthorDao;
import com.zhitar.library.dao.auxiliarydao.UserRoleDao;
import com.zhitar.library.dao.auxiliarydao.impl.MySQLBookAttributeDao;
import com.zhitar.library.dao.auxiliarydao.impl.MySQLBookAuthorDao;
import com.zhitar.library.dao.auxiliarydao.impl.MySQLUserRoleDao;
import com.zhitar.library.dao.bookdao.BookDao;
import com.zhitar.library.dao.bookdao.impl.MySQLBookDao;
import com.zhitar.library.dao.roledao.RoleDao;
import com.zhitar.library.dao.roledao.impl.MySQLRoleDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.dao.userdao.impl.MySQLUserDao;
import org.apache.log4j.Logger;

public class MySQLDaoFactory extends AbstractDAOFactory {

    private static final Logger LOG = Logger.getLogger(MySQLDaoFactory.class.getName());

    public MySQLDaoFactory() {
        LOG.trace("Creating an instance of " + this.getClass().getSimpleName());
    }

    @Override
    public UserDao getUserDAO() {
        LOG.trace("Execute getUserDAO method");
        return new MySQLUserDao();
    }

    @Override
    public RoleDao getRoleDAO() {
        LOG.trace("Execute getRoleDAO method");
        return new MySQLRoleDao();
    }

    @Override
    public AttributeDao getAttributeDAO() {
        LOG.trace("Execute getAttributeDAO method");
        return new MySQLAttributeDao();
    }

    @Override
    public AuthorDao getAuthorDAO() {
        LOG.trace("Execute getAuthorDAO method");
        return new MySQLAuthorDao();
    }

    @Override
    public BookDao getBookDAO() {
        LOG.trace("Execute getBookDAO method");
        return new MySQLBookDao();
    }

    @Override
    public BookAttributeDao getBookAttributeDAO() {
        LOG.trace("Execute getBookAttributeDAO method");
        return new MySQLBookAttributeDao();
    }

    @Override
    public BookAuthorDao getBookAuthorDAO() {
        LOG.trace("Execute getBookAuthorDAO method");
        return new MySQLBookAuthorDao();
    }

    @Override
    public UserRoleDao getUserRoleDAO() {
        LOG.trace("Execute getUserRoleDAO method");
        return new MySQLUserRoleDao();
    }

}
