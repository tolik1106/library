package com.zhitar.library;

import com.zhitar.library.annotation.TransactionProxyCreator;
import com.zhitar.library.dao.attributedao.AttributeDao;
import com.zhitar.library.dao.authordao.AuthorDao;
import com.zhitar.library.dao.auxiliarydao.BookAttributeDao;
import com.zhitar.library.dao.auxiliarydao.BookAuthorDao;
import com.zhitar.library.dao.auxiliarydao.UserRoleDao;
import com.zhitar.library.dao.bookdao.BookDao;
import com.zhitar.library.dao.daofactory.AbstractDAOFactory;
import com.zhitar.library.dao.daofactory.MySQLDaoFactory;
import com.zhitar.library.dao.roledao.RoleDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.service.impl.AdminServiceImpl;
import com.zhitar.library.service.impl.BookServiceImpl;
import com.zhitar.library.service.impl.UserServiceImpl;
import com.zhitar.library.validation.ValidationService;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Container {

    private static Container INSTANCE;

    private static final Logger LOG = Logger.getLogger(Container.class.getName());

    private Map<String, Object> container = new HashMap<>();

    private Container() {
        LOG.trace("Construct " + this.getClass().getSimpleName());
        AbstractDAOFactory daoFactory = new MySQLDaoFactory();
        UserDao userDAO = daoFactory.getUserDAO();
        RoleDao roleDAO = daoFactory.getRoleDAO();
        BookDao bookDAO = daoFactory.getBookDAO();
        AuthorDao authorDAO = daoFactory.getAuthorDAO();
        AttributeDao attributeDAO = daoFactory.getAttributeDAO();
        BookAttributeDao bookAttributeDAO = daoFactory.getBookAttributeDAO();
        BookAuthorDao bookAuthorDAO = daoFactory.getBookAuthorDAO();
        UserRoleDao userRoleDAO = daoFactory.getUserRoleDAO();

        UserServiceImpl userService = new UserServiceImpl(userDAO, roleDAO, userRoleDAO);

        BookServiceImpl bookService = new BookServiceImpl(authorDAO, bookDAO, attributeDAO, userDAO, bookAuthorDAO, bookAttributeDAO);
        AdminServiceImpl adminService = new AdminServiceImpl(authorDAO, bookDAO, attributeDAO, userDAO, bookAuthorDAO, bookAttributeDAO);

        ValidationService validationService = new ValidationService();

        put("daoFactory", daoFactory);
        put("userDAO", userDAO);
        put("roleDAO", roleDAO);
        put("bookDAO", bookDAO);
        put("authorDAO", authorDAO);
        put("attributeDAO", attributeDAO);
        put("bookAttributeDAO", bookAttributeDAO);
        put("bookAuthorDAO", bookAuthorDAO);
        put("userRoleDAO", userRoleDAO);
        put("userService", userService);
        put("bookService", bookService);
        put("adminService", adminService);
        put("validationService", validationService);
    }

    public static Container getInstance() {
        LOG.trace("Execute getInstance method");
        if (INSTANCE == null) {
            synchronized (Container.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Container();
                }
            }
        }
        return INSTANCE;
    }

    public Object getBean(String name) {
        LOG.info("Execute getBean method with name " + name);
        Object result = container.get(name);
        if (result == null) {
            LOG.warn("Bean with name " + name + " not found in container!");
        }
        return result;
    }

    public <T> T getBean(Class<T> clazz) {
        return container.values().stream().filter(value -> clazz.isAssignableFrom(value.getClass())).map(value -> (T) value).findFirst().orElse(null);
    }

    private Object put(String name, Object value) {
        value = getProxy(value);
        return container.put(name, value);
    }

    private <T> T getProxy(T bean) {
        if (bean != null) {
            bean = TransactionProxyCreator.createProxy(bean);
        }
        return bean;
    }
}
