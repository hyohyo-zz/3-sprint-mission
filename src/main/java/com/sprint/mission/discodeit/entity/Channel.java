package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String channelName;
    private User keyUser;
    private List<String> categories;
    private Set<User> members;
    private Instant createdAt;
    private Instant updatedAt;

    public Channel(String channelName, User keyUser, List<String> categories, Set<User> members) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.categories = categories;

        this.keyUser = keyUser;
        this.members = new HashSet<>(members);
        this.members.add(keyUser);

        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;    //updatedAt의 처음 시간은 createAt과 동일해야 함
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void update(Channel updateChannelData) {
        this.channelName = updateChannelData.channelName;
        this.keyUser = updateChannelData.keyUser;
        this.categories = new ArrayList<>(updateChannelData.categories);
        this.members = new HashSet<>(updateChannelData.members);
        this.updatedAt = Instant.now();
    }

    //채널 멤버가 아닌 유저가 메시지 생성시
    public void validateMembership(User sender) {
        if (!members.contains(sender)) {
            throw new IllegalArgumentException(
                    " ---" + sender.getName() + "(은/는) [" + channelName + "]채널 멤버가 아닙니다.");
        }
    }

    //카테고리 중복 확인
    public void validateUniqueCategory() {
        Set<String> categorySet = new HashSet<>(this.categories);
        if (categorySet.size() != this.categories.size()) {
            throw new IllegalArgumentException(" --- 중복된 카테고리가 포함되어 있습니다.");
        }
    }

    //채널에 없는 카테고리에 메시지 생성시
    public void validateCategory(String category){
        if (!categories.contains(category)) {
            throw new IllegalArgumentException(
                    " ---"+ category + "(은/는) [" +channelName + "]채널에 존재 하지 않는 카테고리입니다.");
        }
    }

    public void addKeyUserToMembers() {
        this.members.add(this.keyUser);
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
                ", KeyUser= '" + keyUser.getName() + '\'' +
                ", Category= '" + categories + '\'' +
                ", Members= '" + members.stream().map(User::getName).collect(Collectors.toList()) +
                "'}";
    }
}
