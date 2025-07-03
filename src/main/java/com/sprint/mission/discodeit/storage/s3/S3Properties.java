package com.sprint.mission.discodeit.storage.s3;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Getter
@Setter
public class S3Properties {

    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private int expiration = 600;

    @PostConstruct
    private void loadProperties() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(".env"));

        this.accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        this.secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        this.region = props.getProperty("AWS_S3_REGION");
        this.bucket = props.getProperty("AWS_S3_BUCKET");

        String expirationStr = props.getProperty("AWS_S3_PRESIGNED_URL_EXPIRATION");
        if (expirationStr != null) {
            this.expiration = Integer.parseInt(expirationStr);
        }
    }

}
