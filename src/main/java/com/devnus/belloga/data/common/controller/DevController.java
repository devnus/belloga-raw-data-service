package com.devnus.belloga.data.common.controller;

import com.devnus.belloga.data.common.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * 로컬에서 s3를 테스트하기 위함
 */
@RequiredArgsConstructor
@Controller
public class DevController {
    //aws s3 업로드 테스트용
    private final S3Uploader s3Uploader;

    @PostMapping("/dev/upload")
    @ResponseBody
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static"); // s3 의 static 폴더 안에 파일을 넣겠다
    }
}
