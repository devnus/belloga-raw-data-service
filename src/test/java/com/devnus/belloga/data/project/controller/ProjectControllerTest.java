package com.devnus.belloga.data.project.controller;

import com.devnus.belloga.data.project.dto.RequestProject;
import com.devnus.belloga.data.project.dto.ResponseProject;
import com.devnus.belloga.data.project.service.ProjectService;
import com.devnus.belloga.data.raw.domain.DataType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.given;

@AutoConfigureRestDocs
@ActiveProfiles("test")
@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProjectService projectService;

    @Test
    @DisplayName("???????????? ?????? API ?????? ?????????")
    void registerProjectTest() throws Exception {

        //given
        Map<String, String> input = new HashMap<>();
        input.put("name", "test_name");
        input.put("dataType", "OCR");
        input.put("description", "test_description");
        String enterpriseId = "enterprise-account-id";
        when(projectService.saveProject(Mockito.any(), Mockito.any())).thenReturn(ResponseProject.registerProject.builder().projectId(1L).build());

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/project/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header("enterprise-id", enterpriseId) // ????????? ???????????? ????????? ???????????????, api gateway?????? ????????????.
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("register-project",
                        requestFields(
                                fieldWithPath("name").description("??????????????? ???????????? ??????"),
                                fieldWithPath("dataType").description("???????????? ???????????? ??????"),
                                fieldWithPath("description").description("??????????????? ???????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.projectId").description("????????? ???????????? ID"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @DisplayName("???????????? ?????? API ?????? ?????????")
    void approveProjectTest() throws Exception {

        //given
        String adminId = "test-admin";
        Map<String, String> input = new HashMap<>();
        input.put("projectId", "1");
        input.put("agree", "true");
        given(projectService.agreeProject(Mockito.any(RequestProject.ApproveProject.class))).willReturn(true);

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/project/v1/project/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header("admin-id", adminId) // ??????????????? ???????????? ???????????? id, api gateway?????? ????????????.
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("approve-project",
                        requestFields(
                                fieldWithPath("projectId").description("??????????????? ???????????? id"),
                                fieldWithPath("agree").description("???????????? ?????? ?????? ?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response").description("????????? ??? ??????????????? ?????? boolean"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @DisplayName("pre-signed url ?????? API ?????? ?????????")
    void requestPreSignedUrlTest() throws Exception {

        // given
        String enterpriseId = "enterprise-account-id";
        Long projectId = 1L;
        given(projectService.getPreSignedUrl(Mockito.any(),Mockito.any())).willReturn(ResponseProject.getUrl.builder().Url("pre_sign_url").build());

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
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.url").description("pre-signed-url"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ));

    }

    @Test
    @DisplayName("???????????? ???????????? ?????? API ?????? ?????????")
    void getProjectTest() throws Exception {

        // given
        String adminId = "test-admin";
        List<ResponseProject.getProject> list = new ArrayList<>();
        list.add(ResponseProject.getProject.builder()
                .dataType(DataType.OCR)
                .projectId(1L)
                .description("description")
                .enterpriseName("enterpriseName")
                .isAgreed(false)
                .name("name")
                .zipUrl("zipUrl")
                .zipUUID("zipUUID")
                .build());
        list.add(ResponseProject.getProject.builder()
                .dataType(DataType.OCR)
                .projectId(2L)
                .description("description")
                .enterpriseName("enterpriseName")
                .isAgreed(false)
                .name("name")
                .zipUrl("zipUrl")
                .zipUUID("zipUUID")
                .build());
        Page<ResponseProject.getProject> pages = new PageImpl<>(list);
        given(this.projectService.findProjects(Mockito.any(Pageable.class))).willReturn(pages);

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
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.content.[].projectId").description("???????????? id"),
                                fieldWithPath("response.content.[].name").description("???????????? ??????"),
                                fieldWithPath("response.content.[].isAgreed").description("???????????? ?????? ??????"),
                                fieldWithPath("response.content.[].enterpriseName").description("???????????? ?????? ?????? ????????? ??????"),
                                fieldWithPath("response.content.[].zipUUID").description("???????????? ?????? zip uuid"),
                                fieldWithPath("response.content.[].zipUrl").description("???????????? ?????? zip url"),
                                fieldWithPath("response.content.[].dataType").description("???????????? ????????? ??????"),
                                fieldWithPath("response.content.[].description").description("???????????? ????????? ??????"),
                                fieldWithPath("response.content.[].createDate").description("???????????? ?????????"),

                                fieldWithPath("response.pageable").description("????????? ?????? ??????"),
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

                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ));

    }

    @Test
    @DisplayName("????????? ???????????? ?????? API ?????? ?????????")
    void getMyProjectTest() throws Exception {

        // given
        String enterpriseId = "enterprise-account-id";
        List<ResponseProject.getMyProject> list = new ArrayList<>();
        list.add(ResponseProject.getMyProject.builder()
                .dataType(DataType.OCR)
                .projectId(1L)
                .description("description")
                .progressRate(1.0)
                .isAgreed(false)
                .name("name")
                .zipUrl("zipUrl")
                .zipUUID("zipUUID")
                .build());
        list.add(ResponseProject.getMyProject.builder()
                .dataType(DataType.OCR)
                .projectId(2L)
                .description("description")
                .progressRate(1.0)
                .isAgreed(false)
                .name("name")
                .zipUrl("zipUrl")
                .zipUUID("zipUUID")
                .build());
        Page<ResponseProject.getMyProject> pages = new PageImpl<>(list);
        given(this.projectService.findProjectsByEnterpriseId(Mockito.any(),Mockito.any())).willReturn(pages);

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
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.content.[].projectId").description("???????????? id"),
                                fieldWithPath("response.content.[].name").description("???????????? ??????"),
                                fieldWithPath("response.content.[].isAgreed").description("???????????? ?????? ??????"),
                                fieldWithPath("response.content.[].zipUUID").description("???????????? ?????? zip uuid"),
                                fieldWithPath("response.content.[].zipUrl").description("???????????? ?????? zip url"),
                                fieldWithPath("response.content.[].dataType").description("???????????? ????????? ??????"),
                                fieldWithPath("response.content.[].createdDate").description("??????????????? ???????????? ??????"),
                                fieldWithPath("response.content.[].description").description("???????????? ??????"),
                                fieldWithPath("response.content.[].progressRate").description("?????? ??????????????? ????????? ?????????"),

                                fieldWithPath("response.pageable").description("????????? ?????? ??????"),
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

                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ));

    }

    @Test
    @DisplayName("????????? ???????????? ID??? ?????? API ?????? ?????????")
    void getMyProjectByProjectIdTest() throws Exception {

        // given
        String enterpriseId = "enterprise-account-id";
        Long projectId = 1L;
        when(projectService.findProjectByProjectId(Mockito.any(), Mockito.any())).thenReturn(ResponseProject.getMyProject.builder()
                .projectId(1L)
                .progressRate(1.0)
                .dataType(DataType.OCR)
                .description("description")
                .isAgreed(true)
                .name("name")
                .zipUrl("zipUrl")
                .zipUUID("zipUUID").build());

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
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),

                                fieldWithPath("response.projectId").description("???????????? id"),
                                fieldWithPath("response.name").description("???????????? ??????"),
                                fieldWithPath("response.isAgreed").description("???????????? ?????? ??????"),
                                fieldWithPath("response.zipUUID").description("???????????? ?????? zip uuid"),
                                fieldWithPath("response.zipUrl").description("???????????? ?????? zip url"),
                                fieldWithPath("response.dataType").description("???????????? ????????? ??????"),
                                fieldWithPath("response.createdDate").description("??????????????? ???????????? ??????"),
                                fieldWithPath("response.description").description("???????????? ??????"),
                                fieldWithPath("response.progressRate").description("?????? ??????????????? ????????? ?????????"),

                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ));

    }
}