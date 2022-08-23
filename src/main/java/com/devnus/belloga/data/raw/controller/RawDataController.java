package com.devnus.belloga.data.raw.controller;

import com.devnus.belloga.data.common.aop.annotation.GetAccountIdentification;
import com.devnus.belloga.data.common.aop.annotation.UserRole;
import com.devnus.belloga.data.common.dto.CommonResponse;
import com.devnus.belloga.data.raw.domain.DataType;
import com.devnus.belloga.data.raw.service.RawDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * raw 데이터 업로드 관련 컨트롤러
 */
@RestController
@RequestMapping("/api/raw-data")
@RequiredArgsConstructor
public class RawDataController {

    private final RawDataService rawDataService;

    @PostMapping("/v1/upload")
    @ResponseBody
    public ResponseEntity<CommonResponse> uploadRawData(@GetAccountIdentification(role = UserRole.ENTERPRISE) String enterpriseId, @RequestPart("upload") MultipartFile multipartFile) {

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(rawDataService.saveRawData(enterpriseId, DataType.OCR, multipartFile))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
