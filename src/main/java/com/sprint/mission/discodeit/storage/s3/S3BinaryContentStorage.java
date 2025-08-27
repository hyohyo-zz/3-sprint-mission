package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.exception.binarycontent.FileSaveFailedException;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Slf4j
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final S3Properties props;

    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;

    @PostConstruct
    public void init() {
        this.region = props.getRegion();
        this.accessKey = props.getAccessKey();
        this.secretKey = props.getSecretKey();
        this.bucket = props.getBucket();
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        String key = binaryContentId.toString();
        String contentType = detectContentType(bytes);

        log.info("[S3] put(bytes) 요청: key={}, contentType={}, size={}", key, contentType,
            bytes.length);

        s3Client.putObject(
            b -> b.bucket(bucket).key(key).contentType(contentType)
                .contentLength((long) bytes.length),
            RequestBody.fromBytes(bytes)
        );
        return binaryContentId;
    }

    @Override
    public void put(String objectKey, InputStream in, long contentLength, String contentType) {
        try {
            PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(contentType)
                .contentLength(contentLength)
                .build();

            s3Client.putObject(req, RequestBody.fromInputStream(in, contentLength));
            log.info("[S3] put(stream) 성공: key={}, len={}", objectKey, contentLength);
        } catch (S3Exception | SdkClientException e) {
            log.error("[S3] put(stream) 실패: key={}, msg={}", objectKey, e.getMessage(), e);
            throw new FileSaveFailedException(objectKey, e);
        }
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        String key = binaryContentId.toString();
        log.info("[S3] get 요청 : key={}", key);
        try {
            return s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
        } catch (NoSuchKeyException e) {
            log.warn("[S3] get - 키 없음: {}", key);
            throw e;
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        String key = binaryContentDto.id().toString();
        String contentType = binaryContentDto.contentType();

        log.info("[S3] Presigned URL 생성 요청: key={}, contentType={}", key, contentType);
        String presignedUrl = generatePresignedUrl(key, contentType);
        log.info("[S3] Presigned URL 생성 성공: {}", presignedUrl);

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(presignedUrl))
            .build();
    }

    private S3Client getS3Client() {
        log.debug("[S3] S3Client 생성: region={}, accessKey=****", region);

        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        int expiration = props.getExpiration();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        try (S3Presigner presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()) {

            String fileName = key;
            String ext = getExtensionFromContentType(contentType);
            if (!ext.isEmpty()) {
                fileName += ext;
            }

            var getReq = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentType(contentType)
                .responseContentDisposition("attachment; filename=\"" + fileName + "\"")
                .build();

            var presign = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expiration))
                .getObjectRequest(getReq)
                .build();

            return presigner.presignGetObject(presign).url().toString();
        }
    }

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
            return new Tika().detect(bytes);
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }

}
