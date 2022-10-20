package com.devnus.belloga.data.common.exception;

import com.devnus.belloga.data.common.dto.CommonResponse;
import com.devnus.belloga.data.common.dto.ErrorResponse;
import com.devnus.belloga.data.common.exception.error.*;
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

    /**
     * ProjectId 의 프로젝트가 없을 때
     */
    @ExceptionHandler(NotFoundProjectException.class)
    protected ResponseEntity<CommonResponse> handleInvalidAccountIdException(NotFoundProjectException ex) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_PROJECT_EXCEPTION;

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
     * 사용자 정보를 요청하는 accountId가 유효하지 않은 accountId 일때
     */
    @ExceptionHandler(InvalidAccountIdException.class)
    protected ResponseEntity<CommonResponse> handleInvalidAccountIdException(InvalidAccountIdException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_ACCOUNT_ID_EXCEPTION;

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
     * 프로젝트를 승인할 수 없을때
     */
    @ExceptionHandler(InvalidProjectAgreeException.class)
    protected ResponseEntity<CommonResponse> handleInvalidProjectAgreeException(InvalidProjectAgreeException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_PROJECT_AGREE_EXCEPTION;

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
