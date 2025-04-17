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
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class ServiceFactory {

    private static final ServiceFactory instance = new ServiceFactory();

    private final ChannelService channelService;
    private final UserService userService;
    private final MessageService messageService;

//    private ServiceFactory() {
//        this.channelService = new JCFChannelService();
//        this.userService = new JCFUserService((JCFChannelService) channelService);
//        this.messageService = new JCFMessageService((JCFUserService) userService, (JCFChannelService) channelService);
//    }

//    private ServiceFactory() {
//        this.channelService = new FileChannelService();
//        this.userService = new FileUserService((FileChannelService) channelService);
//        this.messageService = new FileMessageService((FileUserService) userService, (FileChannelService) channelService);
//    }

    private ServiceFactory() {
        FileChannelRepository channelRepository = new FileChannelRepository();
        FileUserRepository userRepository = new FileUserRepository(channelRepository);
        FileMessageRepository messageRepository = new FileMessageRepository(userRepository, channelRepository);

        this.channelService = new BasicChannelService(channelRepository);
        this.userService = new BasicUserService(channelRepository, userRepository);
        this.messageService = new BasicMessageService(messageRepository, userRepository, channelRepository);
    }


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
