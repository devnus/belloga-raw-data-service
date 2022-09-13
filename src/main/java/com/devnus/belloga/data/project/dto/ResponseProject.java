package com.devnus.belloga.data.project.dto;

import com.devnus.belloga.data.raw.domain.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    }
}
