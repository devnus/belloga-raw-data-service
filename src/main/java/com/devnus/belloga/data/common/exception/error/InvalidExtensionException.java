package com.devnus.belloga.data.common.exception.error;

public class InvalidExtensionException extends RuntimeException{
    public InvalidExtensionException() {
        super();
    }
    public InvalidExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidExtensionException(String message) {
        super(message);
    }
    public InvalidExtensionException(Throwable cause) {
        super(cause);
    }
}
