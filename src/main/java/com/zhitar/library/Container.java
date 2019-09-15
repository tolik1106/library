package com.zhitar.library;

import com.zhitar.library.dao.attributedao.AttributeDao;
import com.zhitar.library.dao.authordao.AuthorDao;
import com.zhitar.library.dao.auxiliarydao.BookAttributeDAO;
import com.zhitar.library.dao.auxiliarydao.BookAuthorDAO;
import com.zhitar.library.dao.auxiliarydao.UserRoleDao;
import com.zhitar.library.dao.bookdao.BookDao;
import com.zhitar.library.dao.daofactory.AbstractDAOFactory;
import com.zhitar.library.dao.daofactory.MySQLDaoFactory;
import com.zhitar.library.dao.roledao.RoleDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.service.AdminService;
import com.zhitar.library.service.BookService;
import com.zhitar.library.service.UserService;
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
        BookAttributeDAO bookAttributeDAO = daoFactory.getBookAttributeDAO();
        BookAuthorDAO bookAuthorDAO = daoFactory.getBookAuthorDAO();
        UserRoleDao userRoleDAO = daoFactory.getUserRoleDAO();

        UserService userService = new UserService(userDAO, roleDAO, userRoleDAO);

        BookService bookService = new BookService(authorDAO, bookDAO, attributeDAO, userDAO, bookAuthorDAO, bookAttributeDAO);
        AdminService adminService = new AdminService(authorDAO, bookDAO, attributeDAO, userDAO, bookAuthorDAO, bookAttributeDAO);

        ValidationService validationService = new ValidationService();

        container.put("daoFactory", daoFactory);
        container.put("userDAO", userDAO);
        container.put("roleDAO", roleDAO);
        container.put("bookDAO", bookDAO);
        container.put("authorDAO", authorDAO);
        container.put("attributeDAO", attributeDAO);
        container.put("bookAttributeDAO", bookAttributeDAO);
        container.put("bookAuthorDAO", bookAuthorDAO);
        container.put("userRoleDAO", userRoleDAO);
        container.put("userService", userService);
        container.put("bookService", bookService);
        container.put("adminService", adminService);
        container.put("validationService", validationService);
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
        return container.values().stream().filter(value -> clazz.isAssignableFrom(value.getClass())).map(value -> (T)value).findFirst().orElse(null);
    }
}
