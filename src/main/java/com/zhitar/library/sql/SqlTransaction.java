package com.zhitar.library.sql;

import java.sql.SQLException;

/**
 * Functional interface for transaction execution
 * @param <T> return param
 * @see TransactionHandler
 */
@FunctionalInterface
public interface SqlTransaction<T> {
    T execute() throws SQLException;
}
