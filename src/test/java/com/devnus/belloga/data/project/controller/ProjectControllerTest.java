package com.devnus.belloga.data.project.controller;

import com.devnus.belloga.data.project.dto.RequestProject;
import com.devnus.belloga.data.raw.domain.DataType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = "test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@EmbeddedKafka(
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092"
        },
        ports = { 9092 })
class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerProjectTest() throws Exception {

        //given
        String enterpriseId = "test-enterprise";
        RequestProject.RegisterProject requestRegisterProject = RequestProject.RegisterProject.builder().dataType(DataType.OCR).name("test_project").build();
        MockMultipartFile registerProject = new MockMultipartFile("project", "project", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(requestRegisterProject).getBytes(StandardCharsets.UTF_8));
        //Mock zip 파일
        MockMultipartFile mockMultipartFile = new MockMultipartFile("upload", "upload_file.zip", "application/zip", "test_data".getBytes());

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload("/api/project/v1/project")
                        .file(mockMultipartFile)
                        .file(registerProject)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("enterprise-id", enterpriseId) // 라벨링 수행하는 유저의 식별아이디, api gateway에서 받아온다.
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("register-project",
                        requestParts(partWithName("upload").description("첨부 이미지"), partWithName("project").description("프로젝트 정보")),
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

    @Test
    void approveProjectTest() throws Exception {

        //given
        String adminId = "test-admin";
        Map<String, String> input = new HashMap<>();
        input.put("projectId", "1");

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/project/v1/project/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header("admin-id", adminId) // 프로젝트를 승인하는 관리자의 id, api gateway에서 받아온다.
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("approve-project",
                        requestFields(
                                fieldWithPath("projectId").description("승인하려는 프로젝트 id")
                        ),
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