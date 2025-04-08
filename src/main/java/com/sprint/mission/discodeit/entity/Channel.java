package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel {
    private UUID id;
    private String channelName;
    private List<String> categories;
    private Set<User> members;
    private long createdAt;
    private long updatedAt;

    public Channel(String channelName, List<String> categories, Set<User> members) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.categories = categories;
        this.members = members;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
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

    public void update(Channel updateChannelData) {
        this.channelName = updateChannelData.channelName;
        this.categories = new ArrayList<>(categories);
        this.members = new HashSet<>(updateChannelData.members);
        this.updatedAt = System.currentTimeMillis();
    }

    public String toString() {
        return "Channel{" +
                "ChannelName='" + channelName + '\'' +
                ", Category=" + categories + '\'' +
                ", Members=" + members +
                '}';
    }
}
