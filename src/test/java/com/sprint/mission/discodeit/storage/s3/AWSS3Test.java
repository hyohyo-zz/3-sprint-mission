package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "discodeit.storage.type=s3",
    "discodeit.storage.s3.access-key=dummy",
    "discodeit.storage.s3.secret-key=dummy",
    "discodeit.storage.s3.region=dummy-region",
    "discodeit.storage.s3.bucket=dummy-bucket"
})
public class AWSS3Test {

    private final Logger log = LoggerFactory.getLogger(AWSS3Test.class);

    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;

    private S3Client s3Client;
    private S3Presigner presigner;

    private String key = "test/test1.png";

    @BeforeAll
    void loadProperties() throws IOException {
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
    @Order(1)
    void upload() throws IOException {
        // Given
        Path imagePath = Paths.get("src/test/resources/test1.png");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        // When
        PutObjectRequest uploadRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType("image/png")
            .build();
        s3Client.putObject(uploadRequest, RequestBody.fromBytes(imageBytes));

        // Then
        log.info("이미지 업로드 완료: {}", key);
    }

    @Test
    void download() throws IOException {
        // Given
        String fileName = "testImage";
        Path imagePath = Paths.get("src/test/resources/test1.png");
        byte[] originalBytes = Files.readAllBytes(imagePath);

        // When
        byte[] downloadedBytes;
        try (InputStream inputStream = s3Client.getObject(GetObjectRequest.builder()
            .bucket(bucket).key(key).build())) {
            downloadedBytes = inputStream.readAllBytes();
        }

        String url = presigner.presignGetObject(p -> p
            .getObjectRequest(GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentDisposition("attachment; filename=\"" + fileName + "\"")
                .build())
            .signatureDuration(Duration.ofMinutes(10))
        ).url().toString();

        // Then
        assertEquals(originalBytes.length, downloadedBytes.length);
        log.info("자동 다운로드 링크: {}", url);
        log.info("다운로드된 이미지 크기: {}", downloadedBytes.length);
    }

    @Test
    void generatePresignedUrl() {
        // When
        String url = presigner.presignGetObject(p -> p
            .getObjectRequest(GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build())
            .signatureDuration(Duration.ofMinutes(10))
        ).url().toString();

        // Then
        assertNotNull(url);
        log.info("생성된 Presigned URL:\n{}", url);
    }
}
