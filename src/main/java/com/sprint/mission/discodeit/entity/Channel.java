package com.sprint.mission.discodeit.entity;

import java.util.Set;
import java.util.UUID;

public class Channel {
    private UUID id;
    private String channelName;
    private String category;
    private Set<User> members;
    private long createdAt;
    private long updatedAt;

    public Channel(String channelName, String category, Set<User> members) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.category = category;
        this.members = members;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public UUID getChannelId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String toString() {
        return "Channel{" +
                "Id='" + id + '\'' +
                ", ChannelName='" + channelName + '\'' +
                ", Category=" + category + '\'' +
                ", Members=" + members +
                '}';
    }
}
