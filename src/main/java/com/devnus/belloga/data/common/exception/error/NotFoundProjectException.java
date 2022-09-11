package com.devnus.belloga.data.common.exception.error;

public class NotFoundProjectException extends RuntimeException{
    public NotFoundProjectException() {
        super();
    }
    public NotFoundProjectException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundProjectException(String message) {
        super(message);
    }
    public NotFoundProjectException(Throwable cause) {
        super(cause);
    }
}
