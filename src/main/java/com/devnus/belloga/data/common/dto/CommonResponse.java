package com.devnus.belloga.data.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    @Builder.Default
    private Date dateTime = new Date();
    private boolean success;
    private Object response;
    private Object error;
}
