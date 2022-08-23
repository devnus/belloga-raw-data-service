package com.devnus.belloga.data.common.exception;

import com.devnus.belloga.data.common.dto.CommonResponse;
import com.devnus.belloga.data.common.dto.ErrorResponse;
import com.devnus.belloga.data.common.exception.error.InvalidExtensionException;
import com.devnus.belloga.data.common.exception.error.S3UploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 업로드 하는 압축파일의 확장자가 zip이 아닐때
     */
    @ExceptionHandler(InvalidExtensionException.class)
    protected ResponseEntity<CommonResponse> handleEncryptException(InvalidExtensionException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_EXTENSION_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * s3 업로드에 실패 했을때
     */
    @ExceptionHandler(S3UploadException.class)
    protected ResponseEntity<CommonResponse> handleInvalidAccountIdException(S3UploadException ex) {
        ErrorCode errorCode = ErrorCode.S3_UPLOAD_FAILED_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}
