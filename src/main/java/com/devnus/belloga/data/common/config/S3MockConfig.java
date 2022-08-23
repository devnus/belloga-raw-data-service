package com.devnus.belloga.data.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class S3MockConfig {
    @Value("${cloud.aws.region.static}")
    String region;

    /**
     * S3Mock서버로 사용될 S3Mock을 bean으로 등록
     * 서버로 사용될 포트 지정
     */
    @Bean(name = "s3Mock")
    public S3Mock s3Mock() {
        return new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
    }

    /**
     * 실제 Config와 같이 켜질 경우를 대비하여 @Primary 지정
     */
    @Primary
    @Bean(name = "amazonS3Client", destroyMethod = "shutdown")
    public AmazonS3Client amazonS3Client(){
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", region);
        AmazonS3Client client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
        return client;
    }
}
