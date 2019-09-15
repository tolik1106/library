package com.zhitar.library.sql;

import com.zhitar.library.exception.DataSourceMissedException;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionHandler {

    private static final Logger LOG = Logger.getLogger(TransactionHandler.class.getName());

    private static DataSource dataSource;

    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    static {
        LOG.trace("Define dataSource JNDI");
        try {
            Context ctx = new InitialContext();
            LOG.trace("Instantiate Context " + ctx.getClass().getName());

            LOG.trace("Lookup dataSource by name java:/comp/env/jdbc/library");
            dataSource = (DataSource)ctx.lookup("java:/comp/env/jdbc/library");
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

    public static <T> T transactionExecute(SqlTransaction<T> transaction) {
        LOG.trace("Execute transactionExecute method");
        try (Connection connection = dataSource.getConnection()) {
            LOG.trace("Connect to database successful");
            try {
                connectionHolder.set(connection);
                LOG.trace("Begin transaction");
                connection.setAutoCommit(false);
                LOG.trace("Execute transaction");
                T result = transaction.execute(connection);
                LOG.trace("Commit transaction");
                connection.commit();
                LOG.trace("Transaction is successful with result " + result);
                return result;
            } catch (SQLException e) {
                LOG.error("An error occurred during transaction", e);
                connection.rollback();
                LOG.error("Rollback transaction");
                throw new RuntimeException();
            } finally {
                if (connection != null) {
                    LOG.trace("Closing connection");
                    connection.setAutoCommit(false);
                    connectionHolder.remove();
                    connection.close();
                    LOG.trace("Connection closed");
                }
            }
        } catch (SQLException e) {
            LOG.error("An error occurred during connection establishing", e);
            throw new RuntimeException();
        }
    }

    public static <T> T readOnlyExecute(SqlTransaction<T> transaction) {
        LOG.trace("Execute readOnlyExecute method");
        try (Connection connection = dataSource.getConnection()) {
            LOG.trace("Connect to database successful");
            try {
                connectionHolder.set(connection);
                LOG.trace("Set read only true");
                connection.setReadOnly(true);
                LOG.trace("Execute transaction");
                T res = transaction.execute(connection);
                LOG.trace("Transaction is successful with result " + res);
                connection.setReadOnly(false);
                return res;
            } catch (SQLException e) {
                LOG.error("An error occurred during transaction", e);
                throw new RuntimeException();
            } finally {
                if (connection != null) {
                    LOG.trace("Closing connection");
                    connection.setReadOnly(false);
                    connectionHolder.remove();
                    connection.close();
                    LOG.trace("Connection closed");
                }
            }
        } catch (SQLException e) {
            LOG.error("An error occurred during connection establishing", e);
            throw new RuntimeException();
        }
    }

    public static Connection getConnection() {
        return connectionHolder.get();
    }
}
