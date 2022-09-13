package com.devnus.belloga.data.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseUser {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnterpriseInfo {
        private String phoneNumber;
        private String email;
        private String name;
        private String organization;
    }
}
