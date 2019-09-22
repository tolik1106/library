package com.zhitar.library.connection;

import com.zhitar.library.util.PropertiesUtil;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Simple implementation of the standard JDBC {@link DataSource} interface.
 * uses {@link java.sql.DriverManager}, and
 * returning a new {@link java.sql.Connection} from every {@code getConnection} call.
 */
public class SimpleDataSource implements DataSource {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SimpleDataSource.class);

    /**
     * Create a new SimpleDataSource with parameters that uses
     * in mysql.properties file
     */
    public SimpleDataSource() {
        dbUrl = PropertiesUtil.getValue("mysql.url");
        LOG.debug("dbUrl: " + dbUrl);
        dbUser = PropertiesUtil.getValue("mysql.username");
        LOG.debug("dbUser: " + dbUser);
        dbPassword = PropertiesUtil.getValue("mysql.password");
        LOG.debug("dbPassword: " + dbPassword);
    }

    /**
     * Create a new SimpleDataSource with standard DriverManager parameters.
     * @param url the JDBC URL to use for accessing the DriverManager
     * @param username the JDBC username to use for accessing the DriverManager
     * @param password the JDBC password to use for accessing the DriverManager
     * @see java.sql.DriverManager#getConnection(String, String, String)
     */
    public SimpleDataSource(String url, String username, String password) {
        this.dbUrl = url;
        this.dbUser = username;
        this.dbPassword = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        throw new SQLException(getClass().getName() + " cannot be unwrapped");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException("getLogWriter");
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException("setLogWriter");
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException("setLoginTimeout");
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }
}
