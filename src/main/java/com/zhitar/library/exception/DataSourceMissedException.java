package com.zhitar.library.exception;

/**
 * Data source creation exception
 */
public class DataSourceMissedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MESSAGE = "DataSource cannot be null";

    public DataSourceMissedException() {
        super(DEFAULT_MESSAGE);
    }

    public DataSourceMissedException(String message) {
        super(message);
    }
}
