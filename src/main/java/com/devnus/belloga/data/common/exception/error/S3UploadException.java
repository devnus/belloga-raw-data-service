package com.devnus.belloga.data.common.exception.error;

public class S3UploadException extends RuntimeException{
    public S3UploadException() {
        super();
    }
    public S3UploadException(String message, Throwable cause) {
        super(message, cause);
    }
    public S3UploadException(String message) {
        super(message);
    }
    public S3UploadException(Throwable cause) {
        super(cause);
    }
}
