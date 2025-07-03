package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

class S3BinaryContentStorageTest {
    private S3Properties s3Properties;

    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;

    private S3Client s3Client;
    private S3Presigner presigner;

    @BeforeEach
    void setUp() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(".env"));

        accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        region = props.getProperty("AWS_S3_REGION");
        bucket = props.getProperty("AWS_S3_BUCKET");

        s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )).build();

        presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )).build();
    }

    @Test
    void put() {
        
    }

    @Test
    void get() {
    }

    @Test
    void download() {
    }
}