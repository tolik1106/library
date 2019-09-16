package com.zhitar.library.sql;

import java.sql.SQLException;

public interface SqlTransaction<T> {
    T execute() throws SQLException;
}
