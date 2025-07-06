package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class S3BinaryContentStorageTest {

    private final Logger log = LoggerFactory.getLogger(S3BinaryContentStorageTest.class);

    private S3BinaryContentStorage storage;
    private UUID binaryContentId = UUID.randomUUID();
    private byte[] bytes;

    @BeforeAll
    void setUp() throws IOException {
        log.info("=== S3BinaryContentStorage 테스트 초기화 시작 ===");

        // Properties 로드
        Properties props = new Properties();
        props.load(new FileInputStream(".env"));

        // S3Properties 수동 생성 및 설정
        S3Properties s3Properties = new S3Properties();
        s3Properties.setAccessKey(props.getProperty("AWS_S3_ACCESS_KEY"));
        s3Properties.setSecretKey(props.getProperty("AWS_S3_SECRET_KEY"));
        s3Properties.setRegion(props.getProperty("AWS_S3_REGION"));
        s3Properties.setBucket(props.getProperty("AWS_S3_BUCKET"));

        String expirationStr = props.getProperty("AWS_S3_PRESIGNED_URL_EXPIRATION");
        if (expirationStr != null) {
            s3Properties.setExpiration(Integer.parseInt(expirationStr));
        }

        // S3BinaryContentStorage 초기화
        storage = new S3BinaryContentStorage(s3Properties);

        // 테스트 데이터 준비
        Path imagePath = Paths.get("src/test/resources/test2.png");
        bytes = Files.readAllBytes(imagePath);

        log.info("초기화 완료 - Access Key: {}, Region: {}, Bucket: {}",
            s3Properties.getAccessKey().substring(0, 4) + "***",
            s3Properties.getRegion(),
            s3Properties.getBucket());
    }

    @Test
    @Order(1)
    void put_uploadsFileToS3() {
        log.info("---  업로드 테스트 시작 ---");

        // When
        UUID result = storage.put(binaryContentId, bytes);

        // Then
        assertEquals(binaryContentId, result);
        log.info("업로드 성공 파일 ID: {}, 크기: {} bytes", result, bytes.length);
    }

    @Test
    @Order(2)
    void get() throws IOException {
        log.info("---  다운로드 테스트 시작 ---");

        // When
        try (InputStream inputStream = storage.get(binaryContentId)) {
            byte[] downloadedBytes = inputStream.readAllBytes();

            // Then
            assertEquals(bytes.length, downloadedBytes.length);
            log.info("이미지 다운로드 성공! 크기: {} bytes", downloadedBytes.length);
        }
    }

    @Test
    @Order(3)
    void download() {
        log.info("---  이미지 Presigned URL 리다이렉트 테스트 시작 ---");

        // Given
        BinaryContentDto binaryContentDto = new BinaryContentDto(
            binaryContentId,
            "test.png",
            (long) bytes.length,
            "image/png"
        );

        // When
        ResponseEntity<Resource> response = storage.download(binaryContentDto);

        // Then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());

        String redirectUrl = response.getHeaders().getLocation().toString();
        assertTrue(redirectUrl.contains("amazonaws.com"));
        assertTrue(redirectUrl.contains("response-content-disposition"));

        log.info("리다이렉트 응답 생성 성공!");
        log.info("리다이렉트 URL: {}", redirectUrl);
    }
}
