package com.devnus.belloga.data.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
        Map<String, String> input = new HashMap<>();
        input.put("name", "test_name");
        input.put("dataType", "OCR");
        input.put("description", "test_description");

        String enterpriseId = "enterprise-account-id";

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload("/api/project/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header("enterprise-id", enterpriseId) // 라벨링 수행하는 유저의 식별아이디, api gateway에서 받아온다.
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("register-project",
                        requestFields(
                                fieldWithPath("name").description("생성하려는 프로젝트 이름"),
                                fieldWithPath("dataType").description("생성하는 프로젝트 타입"),
                                fieldWithPath("description").description("생성하려는 프로젝트 설명")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.projectId").description("생성한 프로젝트 ID"),
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

    @Test
    void requestPreSignedUrlTest() throws Exception {

        // given
        String enterpriseId = "enterprise-account-id";
        Long projectId = 1L;

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/project/v1/project/{projectId}/url",projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("enterprise-id", enterpriseId)
                )
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("request-pre-signed-url",
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.url").description("pre-signed-url"),
                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ));

    }

    @Test
    void getProjectTest() throws Exception {

        // given
        String adminId = "test-admin";

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/project/v1/project")
                .contentType(MediaType.APPLICATION_JSON)
                .header("admin-id", adminId)
        )
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("get-all-projects",
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.content.[].projectId").description("프로젝트 id"),
                                fieldWithPath("response.content.[].name").description("프로젝트 이름"),
                                fieldWithPath("response.content.[].isAgreed").description("프로젝트 승인 여부"),
                                fieldWithPath("response.content.[].enterpriseName").description("프로젝트 의뢰 기업 사용자 이름"),
                                fieldWithPath("response.content.[].zipUUID").description("프로젝트 제출 zip uuid"),
                                fieldWithPath("response.content.[].zipUrl").description("프로젝트 제출 zip url"),
                                fieldWithPath("response.content.[].dataType").description("프로젝트 데이터 타입"),
                                fieldWithPath("response.content.[].description").description("프로젝트 데이터 타입"),

                                fieldWithPath("response.pageable.sort.unsorted").description("페이징 처리 sort 정보"),
                                fieldWithPath("response.pageable.sort.sorted").description("페이징 처리 sort 정보"),
                                fieldWithPath("response.pageable.sort.empty").description("페이징 처리 sort 정보"),
                                fieldWithPath("response.pageable.pageNumber").description("page number"),
                                fieldWithPath("response.pageable.pageSize").description("page size"),
                                fieldWithPath("response.pageable.offset").description("page offset"),
                                fieldWithPath("response.pageable.paged").description("paged"),
                                fieldWithPath("response.pageable.unpaged").description("unpaged"),
                                fieldWithPath("response.totalPages").description("total pages"),
                                fieldWithPath("response.totalElements").description("total elements"),
                                fieldWithPath("response.last").description("last"),
                                fieldWithPath("response.numberOfElements").description("numberOfElements"),
                                fieldWithPath("response.size").description("size"),
                                fieldWithPath("response.sort.unsorted").description("unsorted"),
                                fieldWithPath("response.sort.sorted").description("sorted"),
                                fieldWithPath("response.sort.empty").description("empty"),
                                fieldWithPath("response.number").description("number"),
                                fieldWithPath("response.first").description("first"),
                                fieldWithPath("response.empty").description("empty"),

                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ));

    }

    @Test
    void getMyProjectTest() throws Exception {

        // given
        String enterpriseId = "enterprise-account-id";

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/project/v1/user/project")
                .contentType(MediaType.APPLICATION_JSON)
                .header("enterprise-id", enterpriseId)
        )
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("get-my-projects",
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.content.[].projectId").description("프로젝트 id"),
                                fieldWithPath("response.content.[].name").description("프로젝트 이름"),
                                fieldWithPath("response.content.[].isAgreed").description("프로젝트 승인 여부"),
                                fieldWithPath("response.content.[].zipUUID").description("프로젝트 제출 zip uuid"),
                                fieldWithPath("response.content.[].zipUrl").description("프로젝트 제출 zip url"),
                                fieldWithPath("response.content.[].dataType").description("프로젝트 데이터 타입"),
                                fieldWithPath("response.content.[].createdDate").description("프로젝트가 만들어진 시간"),
                                fieldWithPath("response.content.[].description").description("프로젝트 설명"),
                                fieldWithPath("response.content.[].progressRate").description("해당 프로젝트의 라벨링 진행도"),

                                fieldWithPath("response.pageable.sort.unsorted").description("페이징 처리 sort 정보"),
                                fieldWithPath("response.pageable.sort.sorted").description("페이징 처리 sort 정보"),
                                fieldWithPath("response.pageable.sort.empty").description("페이징 처리 sort 정보"),
                                fieldWithPath("response.pageable.pageNumber").description("page number"),
                                fieldWithPath("response.pageable.pageSize").description("page size"),
                                fieldWithPath("response.pageable.offset").description("page offset"),
                                fieldWithPath("response.pageable.paged").description("paged"),
                                fieldWithPath("response.pageable.unpaged").description("unpaged"),
                                fieldWithPath("response.totalPages").description("total pages"),
                                fieldWithPath("response.totalElements").description("total elements"),
                                fieldWithPath("response.last").description("last"),
                                fieldWithPath("response.numberOfElements").description("numberOfElements"),
                                fieldWithPath("response.size").description("size"),
                                fieldWithPath("response.sort.unsorted").description("unsorted"),
                                fieldWithPath("response.sort.sorted").description("sorted"),
                                fieldWithPath("response.sort.empty").description("empty"),
                                fieldWithPath("response.number").description("number"),
                                fieldWithPath("response.first").description("first"),
                                fieldWithPath("response.empty").description("empty"),

                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ));

    }

    @Test
    void getMyProjectByProjectIdTest() throws Exception {

        // given
        String enterpriseId = "enterprise-account-id";
        Long projectId = 1L;

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/project/v1/user/project/{projectId}",projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("enterprise-id", enterpriseId)
                )
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("get-my-project-by-id",
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.projectId").description("프로젝트 id"),
                                fieldWithPath("response.name").description("프로젝트 이름"),
                                fieldWithPath("response.isAgreed").description("프로젝트 승인 여부"),
                                fieldWithPath("response.zipUUID").description("프로젝트 제출 zip uuid"),
                                fieldWithPath("response.zipUrl").description("프로젝트 제출 zip url"),
                                fieldWithPath("response.dataType").description("프로젝트 데이터 타입"),
                                fieldWithPath("response.createdDate").description("프로젝트가 만들어진 시간"),
                                fieldWithPath("response.description").description("프로젝트 설명"),
                                fieldWithPath("response.progressRate").description("해당 프로젝트의 라벨링 진행도"),

                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ));

    }
}