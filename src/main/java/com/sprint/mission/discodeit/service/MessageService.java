package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.util.*;

public interface MessageService {
    Message createMessage(Message message);
    Message updateMessage(UUID id, Message updatedmessage);
    boolean deleteMessage(UUID id);
    List<Message> getMessagesByChannel(UUID id);

}