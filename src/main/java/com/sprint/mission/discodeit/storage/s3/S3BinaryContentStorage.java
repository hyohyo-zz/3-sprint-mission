package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Properties s3;

    String accessKey;
    String secretKey;
    String region;
    String bucket;

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        String key = binaryContentId.toString();
        S3Client client = getS3Client();
        bucket = s3.getBucket();
        String contentType = detectContentType(bytes);

        client.putObject(builder -> builder
                .bucket(bucket)
                .key(key)
                .contentLength((long) bytes.length)
                .contentType(contentType),
            RequestBody.fromBytes(bytes)
        );

        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        String key = binaryContentId.toString();
        S3Client client = getS3Client();
        bucket = s3.getBucket();

        return client.getObject(builder -> builder
            .bucket(bucket)
            .key(key)
        );
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        String key = binaryContentDto.id().toString();
        String contentType = binaryContentDto.contentType();

        // Content-Disposition 헤더를 포함한 PresignedUrl 생성
        String presignedUrl = generatePresignedUrl(key, contentType);

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(presignedUrl))
            .build();
    }

    private S3Client getS3Client() {
        region = s3.getRegion();
        accessKey = s3.getAccessKey();
        secretKey = s3.getSecretKey();

        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        int expiration = s3.getExpiration();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        try (S3Presigner presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()) {

            // 확장자 추가
            String extension = getExtensionFromContentType(contentType);
            String fileName = key.substring(key.lastIndexOf('/') + 1) + extension;

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentType(contentType)
                .responseContentDisposition("attachment; filename=\"" + fileName + "\"")
                .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expiration))
                .getObjectRequest(getObjectRequest)
                .build();

            return presigner.presignGetObject(presignRequest).url().toString();
        }
    }
//
//    public String generateViewUrl(String key) {
//        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
//
//        try (S3Presigner presigner = S3Presigner.builder()
//            .region(Region.of(region))
//            .credentialsProvider(StaticCredentialsProvider.create(credentials))
//            .build()) {
//
//            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucket)
//                .key(key)
//                .build();
//
//            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(10))
//                .getObjectRequest(getObjectRequest)
//                .build();
//
//            return presigner.presignGetObject(presignRequest).url().toString();
//        }
//    }

    /**
     * ContentType에서 확장자 매핑
     */
    private String getExtensionFromContentType(String contentType) {
        if (contentType == null) {
            return "";
        }

        return switch (contentType.toLowerCase()) {
            case "image/jpeg", "image/jpg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "text/plain" -> ".txt";
            case "application/pdf" -> ".pdf";
            default -> "";
        };
    }


    private String detectContentType(byte[] bytes) {
        try {
            Tika tika = new Tika();
            return tika.detect(bytes);
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }

}
