package com.example.contabos3api;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:object-storage.yml")
public class AWSConfig implements InitializingBean {

    @Value("${access-key}")
    private String accessKey;

    @Value("${secret-key}")
    private String secretKey;

    @Value("${url}")
    private String url;

    @Value("${region}")
    private String region;

    private AWSCredentials credentials;

    @Override
    public void afterPropertiesSet() {
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public AmazonS3 getS3Client(){
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(url, region))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
