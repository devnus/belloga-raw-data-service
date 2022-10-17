package com.devnus.belloga.data.project.dto;

import com.devnus.belloga.data.raw.domain.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ResponseProject {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getProject {
        private Long projectId;
        private String name;
        private Boolean isAgreed;
        private String enterpriseName;
        private String zipUUID;
        private String zipUrl;
        private DataType dataType;
        private String description;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getMyProject {
        private Long projectId;
        private String name;
        private Boolean isAgreed;
        private String zipUUID;
        private String zipUrl;
        private DataType dataType;
        private LocalDateTime createdDate;
        private String description;
    }
}
