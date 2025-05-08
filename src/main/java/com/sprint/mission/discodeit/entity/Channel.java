package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.ErrorMessages;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String channelName;
    private User creator;
    private List<String> categories;
    private Set<User> members;

    private boolean isPrivate;

    private Instant createdAt;
    private Instant updatedAt;

    public Channel(String channelName, User creator, List<String> categories, Set<User> members , boolean isPrivate) {
        this.id = UUID.randomUUID();
        this.channelName = channelName != null ? channelName : "";
        this.categories = categories  != null ? categories : new ArrayList<>();

        this.creator = creator;
        this.members = new HashSet<>(members);
        this.members.add(creator);

        this.isPrivate = isPrivate;

        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;    //updatedAt의 처음 시간은 createAt과 동일해야 함
    }

    public void update(Channel updateChannelData) {
        this.channelName = updateChannelData.channelName;
        this.creator = updateChannelData.creator;
        this.categories = new ArrayList<>(updateChannelData.categories);
        this.members = new HashSet<>(updateChannelData.members);
        this.updatedAt = Instant.now();
    }

    public void update(String newChannelName, List<String> newCategories) {
        this.channelName = newChannelName;
        this.categories = new ArrayList<>(newCategories);
        this.updatedAt = Instant.now();
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    //채널 멤버가 아닌 유저가 메시지 생성시
    public void validateMembership(User sender) {
        if (!members.contains(sender)) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("Sender", ErrorMessages.ERROR_NOT_FOUND));
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

    public void addCreatorToMembers() {
        this.members.add(this.creator);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Channel channel)) return false;
        return id.equals(channel.id);
    }

    public String toString() {
        return "Channel{" +
                "ChannelName= '" + channelName + '\'' +
                ", KeyUser= '" + creator.getName() + '\'' +
                ", Category= '" + categories + '\'' +
                ", Members= '" + members.stream().map(User::getName).collect(Collectors.toList()) +
                "'}";
    }
}
