package com.devnus.belloga.data.common.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.devnus.belloga.data.common.exception.error.S3UploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


/**
 * aws S3에 업로드할 때 사용되는 유틸도구이다.
 */
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * upload 메서드를 통해 uploadFile을 S3 dirName 경로에 저장한다.
     * @param uploadFile
     * @param dirName
     * @return 업로드된 파일의 S3 경로
     */
    public String upload(MultipartFile uploadFile, String dirName) {
        String fileType = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."));
        String randomName = UUID.randomUUID().toString() + fileType; // 파일 중복되지 않게 고유식별자 생성

        String fileName = dirName + "/" + randomName;
        String uploadImageUrl = putS3(uploadFile, fileName);
        return uploadImageUrl;
    }

    /**
     * key는 경로이다. 해당 경로의 파일을 제거한다.
     * @param key
     */
    public void deleteFileFromS3(String key) {
        //key는 경로, 파일이름 풀로 ex) static/test.txt
        deleteFile(key);
    }

    private String putS3(MultipartFile uploadFile, String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(uploadFile.getContentType());
        metadata.setContentLength(uploadFile.getSize());
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile.getInputStream(), metadata).withCannedAcl(CannedAccessControlList.PublicRead));

        }catch (IOException e) {
            throw new S3UploadException(e);
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void deleteFile(String key) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);
        try {
            amazonS3Client.deleteObject(deleteObjectRequest);
        }catch(AmazonServiceException e) {
            e.printStackTrace();
        }catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

}

