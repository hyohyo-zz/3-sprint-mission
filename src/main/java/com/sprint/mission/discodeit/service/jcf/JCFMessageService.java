//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//
//
//import java.util.*;
//
//public class JCFMessageService implements MessageService {
//    private static JCFMessageService instance;
//    private final Map<UUID, Message> data = new LinkedHashMap<>();
//    private final UserService userService;
//    private final ChannelService channelService;
//
//    public JCFMessageService(UserService userService, ChannelService channelService) {
//        this.userService = userService;
//        this.channelService = channelService;
//    }
//
//    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
//        if (instance == null) {
//            instance = new JCFMessageService(userService, channelService);
//        }
//        return instance;
//    }
//
//    //메시지 생성
//    @Override
//    public void create(Message message) {
//        Channel channel = message.getChannel();
//        channel.validateMembership(message.getSender());
//        channel.validateCategory(message.getCategory());
//
//        message.validateContent();
//        data.put(message.getId(), message);
//    }
//
//    //메시지 조회
//    @Override
//    public Message find(UUID id) {
//        Message message = this.data.get(id);
//
//        //메시지id 존재하지 않음
//        if (message == null) {
//            throw new IllegalArgumentException(" --해당 ID의 메시지를 찾을 수 없습니다.");
//        }
//        return this.data.get(id);
//    }
//
//    //메시지 전체조회
//    @Override
//    public List<Message> findAll() {
//        return new ArrayList<>(this.data.values());
//    }
//
//    //메시지 수정
//    @Override
//    public Message update(UUID id, Message update) {
//        Message selected = this.data.get(id);
//        selected.update(update);
//        return selected;
//    }
//
//    //메시지 삭제
//    @Override
//    public boolean delete(UUID id) {
//        return data.remove(id) != null;
//    }
//
//
//}
