package com.devnus.belloga.data.raw.event;

import com.devnus.belloga.data.raw.dto.EventRawData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RawDataProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value(value = "${app.topic.raw.raw-data-upload}")
    private String RAW_DATA_UPLOAD;

    /**
     * 데이터 전처리 부분으로, raw data 업로드 정보를 보낸다
     */
    public void uploadRawData(EventRawData.uploadRawData event) {
        kafkaTemplate.send(RAW_DATA_UPLOAD, event);
    }
}
