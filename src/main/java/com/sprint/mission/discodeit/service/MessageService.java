package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public interface MessageService {

    public void create(Message message);

    public Message read(UUID id);

    public List<Message> readAll();

    public Message update(UUID id, Message update);

    public boolean delete(UUID id);


}