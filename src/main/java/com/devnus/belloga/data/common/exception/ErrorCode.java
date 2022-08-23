package com.devnus.belloga.data.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    S3_UPLOAD_FAILED_EXCEPTION(HttpStatus.FAILED_DEPENDENCY, "RAW_DATA_001", "S3_UPLOAD_FAILED"),
    INVALID_EXTENSION_EXCEPTION(HttpStatus.BAD_REQUEST, "RAW_DATA_002", "INVALID_EXTENSION");
    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message){
        this.status = status;
        this.message = message;
        this.code = code;
    }

}
