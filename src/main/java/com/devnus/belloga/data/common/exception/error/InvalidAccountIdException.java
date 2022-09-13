package com.devnus.belloga.data.common.exception.error;

public class InvalidAccountIdException extends RuntimeException{
    public InvalidAccountIdException() {
        super();
    }
    public InvalidAccountIdException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidAccountIdException(String message) {
        super(message);
    }
    public InvalidAccountIdException(Throwable cause) {
        super(cause);
    }
}
