package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class ServiceFactory {

    private static final ServiceFactory instance = new ServiceFactory();

    private final ChannelService channelService;
    private final UserService userService;
    private final MessageService messageService;

    //FileRepository 구현체를 활용
    private ServiceFactory() {
        FileChannelRepository channelRepository = new FileChannelRepository();
        FileUserRepository userRepository = new FileUserRepository(channelRepository);
        FileMessageRepository messageRepository = new FileMessageRepository(userRepository, channelRepository);

        this.channelService = new BasicChannelService(channelRepository);
        this.userService = new BasicUserService(channelRepository, userRepository);
        this.messageService = new BasicMessageService(messageRepository, userRepository, channelRepository);
    }

//JCFRepository 구현체를 활용
//    private ServiceFactory() {
//        JCFChannelRepository channelRepo = new JCFChannelRepository();
//        JCFUserRepository userRepo = new JCFUserRepository(channelRepo);
//        JCFMessageRepository messageRepo = new JCFMessageRepository(userRepo, channelRepo);
//
//        UserService userService = new BasicUserService(channelRepo, userRepo);
//        ChannelService channelService = new BasicChannelService(channelRepo);
//        MessageService messageService = new BasicMessageService(messageRepo, userRepo, channelRepo);
//    }

//JCFService 구현체를 활용
//    private ServiceFactory() {
//        ChannelService channelService = JCFChannelService.getInstance();
//        UserService userService = JCFUserService.getInstance(channelService);
//        MessageService messageService = JCFMessageService.getInstance(userService, channelService);
//
//        this.channelService = channelService;
//        this.userService = userService;
//        this.messageService = messageService;
//    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public ChannelService createChannelService() {
        return channelService;
    }

    public UserService createUserService() {
        return userService;
    }

    public MessageService createMessageService() {
        return messageService;
    }

}
