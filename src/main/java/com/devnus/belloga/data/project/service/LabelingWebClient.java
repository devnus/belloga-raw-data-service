package com.devnus.belloga.data.project.service;

import com.devnus.belloga.data.common.dto.CommonResponse;
import com.devnus.belloga.data.common.exception.error.NotFoundProjectException;
import com.devnus.belloga.data.project.dto.ResponseLabeling;
import com.devnus.belloga.data.raw.domain.DataType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class LabelingWebClient {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    /**
     * 동기 통신을 통해 dataType, projectId 로 라벨링 진행도를 확인한다
     */
    public ResponseLabeling.ProgressRate getProgressRate(DataType dataType, Long projectId) {
        CommonResponse commonResponse = webClient
                .get()
                .uri("/api/data/v1/target/{dataType}/projects/{projectId}", dataType, projectId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(res -> {
                    if(res.statusCode().value() != HttpStatus.OK.value()){
                        return reactor.core.publisher.Mono.error(new NotFoundProjectException());
                    }
                    return res.bodyToMono(CommonResponse.class);

                })
                .block();

        //LinkedHashMap으로 역직렬화 된 commonResponse의 response 값을 ResponseLabeling.ProgressRate 형변환
        return objectMapper.convertValue(commonResponse.getResponse(), new TypeReference<ResponseLabeling.ProgressRate>() {});
    }
}
