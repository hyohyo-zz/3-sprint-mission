package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
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

    private ServiceFactory() {
        this.channelService = new FileChannelService();
        this.userService = new FileUserService((FileChannelService) channelService);
        this.messageService = new FileMessageService((FileUserService) userService, (FileChannelService) channelService);

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
