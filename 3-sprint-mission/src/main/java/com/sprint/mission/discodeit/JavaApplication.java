package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.*;

public class JavaApplication {
    public static void main(String[] args){

        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        //유저데이터
        User user1 = new User("조현아","여자","akbkck8101@gmail.com","010-6658-8101","2701");
        User user2 = new User("신짱구","남자","zzang9@gmail.com","010-1234-5678","9999");
        User user3 = new User("김철수","남자","steelwater@naver.com","010-0000-0000","steelwater");
        User user4 = new User("이훈이","남자","2hun2@naver.com","010-2345-6789","2hun222");
        User user5 = new User("맹구","남자","stone_lover9@gmail.com","010-1010-0101","stone_lover");
        User user6 = new User("한유리","여자","yuryyy@gmail.com","010-1111-2222","12345");
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);
        userService.createUser(user5);
        userService.createUser(user6);

        //채널에 유저 멤버
        Set<User> members1 = new HashSet<>();
        members1.add(user1);
        members1.add(user2);
        members1.add(user4);

        Set<User> members2 = new HashSet<>();
        members2.add(user2);
        members2.add(user3);
        members2.add(user5);
        members2.add(user6);

        Set<User> members3 = new HashSet<>();

        //카테고리
        List<String> cat1 = new ArrayList<>();
        cat1.add("공지");
        cat1.add("질문");
        cat1.add("2팀");

        List<String> cat2 = new ArrayList<>();
        cat2.add("이벤트");
        cat2.add("소통");

        List<String> cat3 = new ArrayList<>();

        //채널 생성
        Channel channel1 = new Channel("sb01",cat1, members1);
        Channel channel2 = new Channel("sb02",cat2, members2);
        Channel channel3 = new Channel("sb03",cat3, members3);
        channelService.createChannel(channel1);
        channelService.createChannel(channel2);
        channelService.createChannel(channel3);

        Message message1 = new Message(user1, channel1, channel1.getCategory().get(0), "공지입니다.");
        Message message2 = new Message(user2, channel1, channel1.getCategory().get(2),"안녕하세요.");
        Message message3 = new Message(user1, channel2, channel2.getCategory().get(1), "소통해요");
        Message message4 = new Message(user3, channel2, channel2.getCategory().get(1),"좋아요");

        messageService.createMessage(message1);
        messageService.createMessage(message2);
        messageService.createMessage(message3);
        messageService.createMessage(message4);



        System.out.println("\n-------------------- User ----------------------------------------");
        System.out.println("전체 유저 목록: ");
        userService.Users().forEach(System.out::println);

        System.out.println("\n신짱구 유저 정보: ");
        userService.Users().stream()
                .filter(user -> user.getName().equals("신짱구"))
                .forEach(System.out::println);

        //이름에 "구"가 포함된 유저
        System.out.println("\n이름에 '구'가 포함된 유저: ");
        userService.findByName("구").forEach(System.out::println);

        //성별 그룹화
        System.out.println("\n성별 그룹화: ");
        userService.groupByGender().forEach((gender, users) -> {
            System.out.println(gender +": ");
            users.forEach(System.out::println);
        });




        //성별 + 채널 그룹화


        //채널이 sp01이고 남자인 유저



        System.out.println("\n-------------------- Channel ----------------------------------------");

        System.out.println("\n전체 채널 목록");
        channelService.Channels().forEach(channel -> System.out.println(channel.getChannelName()));

        //채널별 유저 수
        System.out.println("\n채널별 유저 수:");
        System.out.println(userService.countByChannel());

        System.out.print("\n[sp01] 채널 멤버: ");
        for ( User member : channelService.getChannelMembers(channel1.getChannelId())){
            System.out.print(member.getName()+"님 ");
        }

        System.out.print("\n[sp02] 채널 멤버: ");
        for ( User member : channelService.getChannelMembers(channel2.getChannelId())){
            System.out.print(member.getName()+"님 ");
        }

        //채널별 카테고리

        //신짱구의 채널목록
        System.out.println("신짱구의 채널 내역: ");
        channelService.Channels().forEach(channel -> {
            if (channel.getMembers().stream().anyMatch(user -> user.getName().equals("신짱구"))){
                System.out.println("- [" + channel.getChannelName() + "]");
            }
        });

        System.out.println("\n\n-------------------- Message ----------------------------------------");
        System.out.println("전체 메시지");
        for (Channel channel : channelService.Channels()) {
            System.out.println("\n["+ channel.getChannelName()+"] 채널 메시지 ▽" );

            for (String category : channel.getCategory()) {
                System.out.println("- " + category);

                for (Message msg : messageService.getMessagesByChannel(channel.getChannelId())) {
                    if (msg.getCategory().equals(category)) {
                        System.out.print("  "+msg.getSender().getName() + ": " + msg.getContent() + "\n");
                    }

                }

            }

        }


    }
}
