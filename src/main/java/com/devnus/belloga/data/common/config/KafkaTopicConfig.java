package com.devnus.belloga.data.common.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    @Value(value = "${app.topic.raw.raw-data-upload}")
    private String RAW_DATA_UPLOAD;

    /**
     * 라벨링 수행 후 포인트 지급 토픽 생성
     * 같은 이름의 토픽이 등록되어 있다면 동작하지 않음
     * partition 개수, replica 개수는 논의 후 수정
     */
    @Bean
    public NewTopic createRawDataUploadTopic() {
        return TopicBuilder.name(RAW_DATA_UPLOAD)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
