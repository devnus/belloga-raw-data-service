package com.devnus.belloga.data.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    S3_UPLOAD_FAILED_EXCEPTION(HttpStatus.FAILED_DEPENDENCY, "RAW_DATA_001", "S3_UPLOAD_FAILED"),
    INVALID_EXTENSION_EXCEPTION(HttpStatus.BAD_REQUEST, "PROJECT_001", "INVALID_EXTENSION"),
    NOT_FOUND_PROJECT_EXCEPTION(HttpStatus.BAD_REQUEST, "PROJECT_002", "NOT_FOUND_PROJECT"),
    INVALID_ACCOUNT_ID_EXCEPTION(HttpStatus.NOT_FOUND, "USER_002", "INVALID_ACCOUNT_ID");

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message){
        this.status = status;
        this.message = message;
        this.code = code;
    }

}
