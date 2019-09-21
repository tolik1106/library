package com.zhitar.library.connection;

import com.zhitar.library.exception.DataSourceMissedException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class JndiDataSource implements DataSource {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(JndiDataSource.class.getName());

    private DataSource dataSource;

    public JndiDataSource() {
        LOG.trace("Define dataSource JNDI");
        try {
            Context ctx = new InitialContext();
            LOG.trace("Instantiate Context " + ctx.getClass().getName());

            LOG.trace("Lookup dataSource by name java:/comp/env/jdbc/library");
            dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/library");
            if (dataSource == null) {
                LOG.error("Couldn't find dataSource in context");
                throw new DataSourceMissedException("Data source not found!");
            }
            LOG.info("Defined dataSource successfully " + dataSource.getClass().getName());
        } catch (Exception e) {
            LOG.error("An error occurred during defining dataSource", e);
            throw new DataSourceMissedException("Couldn't define dataSource");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return dataSource.getConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return dataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return dataSource.isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return dataSource.getParentLogger();
    }
}
