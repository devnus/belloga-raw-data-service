package com.devnus.belloga.data.project.dto;

import com.devnus.belloga.data.raw.domain.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class RequestProject {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterProject {
        @NotNull
        private String name;
        @NotNull
        private DataType dataType;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApproveProject {
        @NotNull
        private Long projectId;
    }
}
