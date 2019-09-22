package com.zhitar.library.exception;

/**
 * Transaction execution exception for proxy bean
 */
public class TransactionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TransactionException(Throwable cause) {
        super(cause);
    }
}