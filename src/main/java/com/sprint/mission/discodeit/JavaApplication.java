package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaApplication {
    public static void main(String[] args){
        User user1 = new User("aaa","asdf@gmail.com","010-0000-0000","pw1");
        User user2 = new User("Bob", "bob@example.com", "010-2222-3333", "pw2");

        Set<User> members = new HashSet<>();

        Channel channel = new Channel("sb01","2팀", members);





    }
}
