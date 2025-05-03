package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {

    private final DiscodeitProperties properties;

    @Bean
    public UserRepository userRepository(ChannelRepository channelRepository) {
        String type = properties.getType();

        if ("file".equalsIgnoreCase(type)) {
            return new FileUserRepository(properties, channelRepository);
        } else { // "jcf" 또는 null인 경우 기본값
            return new JCFUserRepository();
        }
    }
    @Bean
    public ChannelRepository channelRepository() {
        String type = properties.getType();
        if ("file".equalsIgnoreCase(type)) {
            return new FileChannelRepository(properties);
        } else {
            return new JCFChannelRepository();
        }
    }

    @Bean
    public MessageRepository messageRepository(UserRepository userRepository, ChannelRepository channelRepository) {
        String type = properties.getType();
        if ("file".equalsIgnoreCase(type)) {
            return new FileMessageRepository(properties, userRepository, channelRepository);
        } else {
            return new JCFMessageRepository();
        }
    }

    @Bean
    public ReadStatusRepository readStatusRepository() {
        String type = properties.getType();
        if ("file".equalsIgnoreCase(type)) {
            return new FileReadStatusRepository(properties);
        } else {
            return new JCFReadStatusRepository();
        }
    }

    @Bean
    public UserStatusRepository userStatusRepository() {
        String type = properties.getType();
        if ("file".equalsIgnoreCase(type)) {
            return new FileUserStatusRepository(properties);
        } else {
            return new JCFUserStatusRepository();
        }
    }

    @Bean
    public BinaryContentRepository binaryContentRepository() {
        String type = properties.getType();
        if ("file".equalsIgnoreCase(type)) {
            return new FileBinaryContentRepository(properties);
        } else {
            return new JCFBinaryContentRepository();
        }
    }
}