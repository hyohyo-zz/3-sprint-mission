package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Message {
    private UUID id;
    private User sender;        //보낸사람
    private Channel channel;
    private String category;
    private String content;     //내용
    private long createdAt;
    private long updatedAt;

    public Message(User sender,Channel channel, String category, String content) {
        this.id = UUID.randomUUID();
        this.sender = sender;
        this.channel = channel;
        this.category = category;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;    //updatedAt의 처음 시간은 createAt과 동일해야 함

        validateMessage();
    }

    public UUID getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }

    public Channel getChannel() {
        return channel;
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getCategory() {return category;}
    public void setCategory(String category) {
        this.category = category;
    }

    public long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void update(Message updateMessageData) {
        this.content = updateMessageData.content;
        this.updatedAt = System.currentTimeMillis();
    }

    public void validateMessage() {
        //채널 멤버가 아닌 유저가 메시지 생성시
        if (!channel.getMembers().contains(sender)) {
            throw new IllegalArgumentException(
                    " ---" + sender.getName()+"(은/는) [" + channel.getChannelName() + "]채널 멤버가 아닙니다.");
        }

        //채널에 없는 카테고리에 메시지 생성시
        if (!channel.getCategory().contains(category)) {
            throw new IllegalArgumentException(
                    " ---"+ category + "(은/는) [" + channel.getChannelName() + "]채널에 존재 하지 않는 카테고리입니다.");
        }
    }

    public String toString() {
        return "Message{" +
                "id= '" + id + '\'' +
                ", sender= '" + sender.getName() + '\'' +
                ", channel= '" + channel.getChannelName() + '\'' +
                ", category= '" + category + '\'' +
                ", content= '" + content + '\'' +
                '}';
    }

}
