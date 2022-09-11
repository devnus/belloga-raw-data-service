package com.devnus.belloga.data.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class ResponseS3 {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class S3File {
        @NotNull
        private String fileName;
        @NotNull
        private String fileUrl;
    }
}
