package com.devnus.belloga.data.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseLabeling {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProgressRate {
        private Double progressRate;
    }
}
