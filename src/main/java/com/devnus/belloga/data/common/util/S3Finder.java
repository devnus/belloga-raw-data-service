package com.devnus.belloga.data.common.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.devnus.belloga.data.common.dto.ResponseS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * S3에서 파일 정보를 가져온다
 */
@RequiredArgsConstructor
@Component
public class S3Finder {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.unzip-bucket}")
    private String bucket;

    /**
     * 파일 경로를 통해 url 추출
     */
    public String getUrl(String path){
        return amazonS3Client.getUrl(bucket, path).toString();
    }

    /**
     * 파일 디렉터리(path)에 속한 파일 목록 반환
     */
    public List<ResponseS3.S3File> findFiles(String dirName, String zipUUID){

        List<ResponseS3.S3File> files = new ArrayList<>();
        String path = dirName + "/" + zipUUID;

        ObjectListing objectListing = amazonS3Client.listObjects(new ListObjectsRequest()
                .withBucketName(bucket)
                .withPrefix(path));

        for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
            files.add(ResponseS3.S3File.builder()
                    .fileName(summary.getKey())
                    .fileUrl(getUrl(summary.getKey())).build());
        }

        return files;
    }
}
