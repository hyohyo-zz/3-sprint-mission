package com.sprint.mission.discodeit.storage.s3;

import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Getter
public class S3Properties {

    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private int presignedUrlExpiration = 600;

}
