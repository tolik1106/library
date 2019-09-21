package com.zhitar.library.exception;

public class TransactionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TransactionException(Throwable cause) {
        super(cause);
    }
}