package com.devnus.belloga.data.raw.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.devnus.belloga.data.common.config.S3MockConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.findify.s3mock.S3Mock;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = "test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
//@EmbeddedKafka(
//        brokerProperties = {
//                "listeners=PLAINTEXT://localhost:9092"
//        },
//        ports = { 9092 })
@Import(S3MockConfig.class)
class RawDataControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private S3Mock s3Mock;
    @Autowired
    private static final String BUCKET_NAME = "belloga-dev-s3-raw-data-bucket";

    /**
     * S3Mock 서버 실행
     * test에서 사용할 Bucket 생성
     */
    @BeforeAll
    static void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3Client amazonS3Client) {
        s3Mock.start();
        amazonS3Client.createBucket(BUCKET_NAME);
    }

    /**
     *  S3Mock 서버 종료
     */
    @AfterAll
    static void shutDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3Client amazonS3Client) {
        amazonS3Client.shutdown();
        s3Mock.stop();
    }

    @Test
    void uploadRawDataTest() throws Exception {

        //given
        String enterpriseId = "test-enterprise";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "mock1.png", "image/png", "test data".getBytes());
        MockMultipartFile metadata = new MockMultipartFile("metadata", "",
                "application/json", "{ \"version\": \"1.0\"}".getBytes());

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload("/api/raw-data/v1/upload")
                        .file(mockMultipartFile)
                        .file(metadata)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("enterprise-id", enterpriseId) // 라벨링 수행하는 유저의 식별아이디, api gateway에서 받아온다.
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("upload-raw-data",
                        requestPartBody("metadata"),
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response").description("등록이 잘 되었는지에 대한 boolean"),
                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ))
                .andExpect(jsonPath("$.success", is(true)));
    }
}