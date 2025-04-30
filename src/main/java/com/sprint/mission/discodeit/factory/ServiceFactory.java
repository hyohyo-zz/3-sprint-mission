//package com.sprint.mission.discodeit.factory;
//
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import com.sprint.mission.discodeit.repository.UserStatusRepository;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.service.basic.BasicChannelService;
//import com.sprint.mission.discodeit.service.basic.BasicMessageService;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//import org.springframework.stereotype.Component;
//
//public class ServiceFactory {
//
//    private static final ServiceFactory instance = new ServiceFactory();
//
//    private final ChannelService channelService;
//    private final UserService userService;
//    private final MessageService messageService;
//
//
//    private ServiceFactory() {
//        FileChannelRepository channelRepository = new FileChannelRepository();
//        FileUserRepository userRepository = new FileUserRepository(channelRepository);
//        FileMessageRepository messageRepository = new FileMessageRepository(userRepository, channelRepository);
//
//        UserStatusRepository userStatusRepository = new UserStatusRepository();
//        BinaryContentRepository binaryContentRepository = new BinaryContentRepository();
//
//        this.channelService = new BasicChannelService(channelRepository);
//        this.userService = new BasicUserService(channelRepository, userRepository, userStatusRepository, binaryContentRepository);
//        this.messageService = new BasicMessageService(messageRepository, userRepository, channelRepository);
//    }
//
//    public static ServiceFactory getInstance() {
//        return instance;
//    }
//
//    public ChannelService createChannelService() {
//        return channelService;
//    }
//
//    public UserService createUserService() {
//        return userService;
//    }
//
//    public MessageService createMessageService() {
//        return messageService;
//    }
//
//}
