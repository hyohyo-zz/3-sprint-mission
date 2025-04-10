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
