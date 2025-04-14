package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String channelName;
    private User keyUser;
    private List<String> categories;
    private Set<User> members;
    private long createdAt;
    private long updatedAt;

    public Channel(String channelName, User keyUser, List<String> categories, Set<User> members) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.categories = categories;

        this.keyUser = keyUser;
        this.members = new HashSet<>(members);
        this.members.add(keyUser);

        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;    //updatedAt의 처음 시간은 createAt과 동일해야 함
    }

    public UUID getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<String> getCategory() {
        return categories;
    }
    public void setCategory(List<String> categories) {
        this.categories = categories;
    }

    public Set<User> getMembers() {
        return members;
    }
    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getKeyUser() {
        return keyUser;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void update(Channel updateChannelData) {
        this.channelName = updateChannelData.channelName;
        this.keyUser = updateChannelData.keyUser;
        this.categories = new ArrayList<>(updateChannelData.categories);
        this.members = new HashSet<>(updateChannelData.members);
        this.updatedAt = System.currentTimeMillis();
    }

    public void validateMembership(User sender) {
        //채널 멤버가 아닌 유저가 메시지 생성시
        if (!members.contains(sender)) {
            throw new IllegalArgumentException(
                    " ---" + sender.getName() + "(은/는) [" + channelName + "]채널 멤버가 아닙니다.");
        }
    }
        public void validateCategory(String category) {
        //채널에 없는 카테고리에 메시지 생성시
        if (!categories.contains(category)) {
            throw new IllegalArgumentException(
                    " ---"+ category + "(은/는) [" +channelName + "]채널에 존재 하지 않는 카테고리입니다.");
        }
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
