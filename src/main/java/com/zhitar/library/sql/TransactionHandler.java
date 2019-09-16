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

    public static <T> T transactionExecute(SqlTransaction<T> transaction) {
        LOG.trace("Execute transactionExecute method");
        try (Connection connection = dataSource.getConnection()) {
            LOG.trace("Connect to database successful");
            try {
                connectionHolder.set(connection);
                LOG.trace("Begin transaction");
                connection.setAutoCommit(false);
                LOG.trace("Execute transaction");
                T result = transaction.execute();
                LOG.trace("Commit transaction");
                connection.commit();
                LOG.trace("Transaction is successful with result " + result);
                return result;
            } catch (SQLException e) {
                LOG.error("An error occurred during transaction", e);
                connection.rollback();
                LOG.error("Rollback transaction");
                throw new RuntimeException(e);
            } finally {
                LOG.trace("Prepare connection to close");
                connection.setAutoCommit(false);
                connectionHolder.remove();
                LOG.trace("Connection prepared to close");
            }
        } catch (SQLException e) {
            LOG.error("An error occurred during connection establishing", e);
            throw new RuntimeException(e);
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
                T res = transaction.execute();
                LOG.trace("Transaction is successful with result " + res);
                connection.setReadOnly(false);
                return res;
            } catch (SQLException e) {
                LOG.error("An error occurred during transaction", e);
                throw new RuntimeException(e);
            } finally {
                LOG.trace("Prepare connection to close");
                connection.setReadOnly(false);
                connectionHolder.remove();
                LOG.trace("Connection prepared to close");
            }
        } catch (SQLException e) {
            LOG.error("An error occurred during connection establishing", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T execute(SqlTransaction<T> transaction) {
        LOG.trace("Execute without transaction");
        try (Connection connection = dataSource.getConnection()) {
            LOG.trace("Connect to database successful");
            try {
                connectionHolder.set(connection);
                return transaction.execute();
            } catch (SQLException e) {
                LOG.error("An error occurred during execution", e);
                throw new RuntimeException(e);
            } finally {
                LOG.trace("Prepare connection to close");
                connectionHolder.remove();
                LOG.trace("Connection prepared to close");
            }
        } catch (SQLException e) {
            LOG.error("An error occurred during connection establishing", e);
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connectionHolder.get();
    }
}
