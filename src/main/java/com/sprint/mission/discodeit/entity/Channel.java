package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.ErrorMessages;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter @Setter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String channelName;
    private List<String> categories;

    private ChannelType type;

    private Instant createdAt;
    private Instant updatedAt;

    public Channel(ChannelType type, String channelName, List<String> categories) {
        this.id = UUID.randomUUID();
        this.type = type;

        this.channelName = channelName;
        this.categories = categories;

        this.createdAt = Instant.now();
    }

    public void update(String newChannelName, List<String> newCategories) {
        boolean anyValueUpdated = false;
        if (newChannelName != null && !newChannelName.equals(this.channelName)) {
            this.channelName = newChannelName;
            anyValueUpdated = true;
        }
        if (newCategories != null && !newCategories.equals(this.categories)) {
            this.categories = newCategories;
            anyValueUpdated = true;
        }
    }

    //카테고리 중복 확인
    public void validateUniqueCategory() {
        Set<String> unique = new HashSet<>();
        for (String category : this.categories) {
            if (!unique.add(category)) {
                throw new IllegalArgumentException(
                        ErrorMessages.format("Category", ErrorMessages.ERROR_EXISTS)
                );
            }
        }
    }

    //채널에 없는 카테고리에 메시지 생성시
    public void validateCategory(String category){
        if (!categories.contains(category)) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("Category", ErrorMessages.ERROR_NOT_FOUND)
            );
        }
    }
}
