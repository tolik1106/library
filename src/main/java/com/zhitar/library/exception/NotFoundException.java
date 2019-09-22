package com.zhitar.library.exception;

/**
 * Service layer exception if entity doesn't exits
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
