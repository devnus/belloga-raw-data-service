package com.devnus.belloga.data.raw.dto;

import com.devnus.belloga.data.raw.domain.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EventRawData {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class uploadRawData {
        private String enterpriseId;
        private Long rawDataId;
        private String imageUrl;
        private DataType dataType;
    }
}
