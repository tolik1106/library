package com.zhitar.library.exception;

public class DataSourceMissedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "DataSource cannot be null";
    public DataSourceMissedException() {
        super(DEFAULT_MESSAGE);
    }

    public DataSourceMissedException(String message) {
        super(message);
    }
}
