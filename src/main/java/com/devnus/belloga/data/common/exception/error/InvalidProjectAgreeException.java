package com.devnus.belloga.data.common.exception.error;

public class InvalidProjectAgreeException extends RuntimeException{
    public InvalidProjectAgreeException() {
        super();
    }
    public InvalidProjectAgreeException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidProjectAgreeException(String message) {
        super(message);
    }
    public InvalidProjectAgreeException(Throwable cause) {
        super(cause);
    }
}