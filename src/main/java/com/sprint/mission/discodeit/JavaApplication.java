package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import jdk.jfr.Category;


import java.util.*;

public class JavaApplication {
    public static void main(String[] args) {

        JCFChannelService channelService = new JCFChannelService();
        JCFUserService userService = new JCFUserService(channelService);
        JCFMessageService messageService = new JCFMessageService(userService, channelService);

        List<User> users = createAndRegisterUsers(userService);
        List<Channel> channels = createAndRegisterChannels(channelService, users);
        createAndRegisterMessages(messageService, users, channels);

        demonstrateUserOperations(userService, users);
        demonstrateChannelOperations(channelService, channels,users);
        demonstrateMessageOperations(messageService);

    }


    //유저생성 및 등록
    private static List<User> createAndRegisterUsers(JCFUserService userService) {

        User user1 = new User("조현아", "여", "akbkck8101@gmail.com", "010-6658-8101", "2701");
        User user2 = new User("신짱구", "남", "zzang9@gmail.com", "010-1234-5678", "9999");
        User user3 = new User("김철슈", "남", "steelwater@naver.com", "010-0000-0000", "steelwater");
        User user4 = new User("이훈이", "남", "2hun2@naver.com", "010-2345-6789", "2hun222");
        User user5 = new User("맹구", "남", "stone_lover9@gmail.com", "010-1010-0101", "stone_lover");
        User user6 = new User("한유리", "여", "yuryyy@gmail.com", "010-1111-2222", "12345");


        //-----심화-----//
        //중복 이메일 유저_ex)
        User user7 = new User("한유리", "여", "yuryyy@gmail.com", "010-1111-2222", "12345");

        System.out.println("<--------------------유저를 등록합니다------------------------->\n.\n.");
        List<User> u_i = List.of(user1, user2, user3, user4, user5, user6, user7);
        List<User> users = new ArrayList<>();   //등록된 유저만 users에 담음

        for (int i = 0; i < u_i.size(); i++) {
            try {
                userService.create(u_i.get(i));
                users.add(u_i.get(i));
                System.out.println("유저"+(i+1)+" 등록 완료");
            } catch (IllegalArgumentException e) {
                System.out.println("!!유저" + (i+1) + " 등록 실패!!" + e.getMessage());
            }
        }
        System.out.println(".\n.\n<--------------------유저 등록 종료---------------------------->\n");
        return users;
        //-----심화-----//
    }

    //채널,멤버,카테고리 생성 및 등록
    private static List<Channel> createAndRegisterChannels(JCFChannelService channelService, List<User> users) {
        //채널1 멤버, 카테고리생성
        Set<User> members1 = Set.of(users.get(0), users.get(1), users.get(3));
        List<String> cat1 = List.of("공지", "질문", "2팀");

        //채널2 멤버, 카테고리생성
        Set<User> members2 = Set.of(users.get(1), users.get(2), users.get(4));
        List<String> cat2 = List.of("이벤트", "소통");

        //채널3 멤버, 카테고리생성
        Set<User> members3 = Set.of(users.get(5));
        List<String> cat3 = List.of("기타");

        Channel channel1 = new Channel("sp01", cat1, members1);
        Channel channel2 = new Channel("sp02", cat2, members2);
        Channel channel3 = new Channel("sp03", cat3, members3);


        //-----심화-----//
        //중복 채널명_ex)
        Channel channel4 = new Channel("sp03", cat3, members3);

        //중복 카테고리_ex)
        List<String> cat5 = List.of("공지","공지");
        Channel channel5 = new Channel("sp05", cat5, members3);

        System.out.println("<--------------------채널을 등록합니다------------------------->\n.\n.");

        List<Channel> ch_i = List.of(channel1, channel2, channel3,channel4,channel5);
        List<Channel> channels = new ArrayList<>();   //등록된 채널만담기

        for (int i = 0; i < ch_i.size(); i++) {
            try {
                channelService.create(ch_i.get(i));
                channels.add(ch_i.get(i));
                System.out.println("채널"+(i+1)+"[" + ch_i.get(i).getChannelName() + "] 등록 완료");

            } catch (IllegalArgumentException e) {
                System.out.println("!!채널" + (i+1) + "[" + ch_i.get(i).getChannelName() + "] 등록 실패!!" + e.getMessage());
            }
        }
        System.out.println(".\n.\n<--------------------채널 등록 종료---------------------------->\n");
        return channels;
        //-----심화-----//
    }

    //메시지 생성 및 등록
    private static void createAndRegisterMessages(JCFMessageService messageService, List<User> users, List<Channel> channels) {
        Message message1 = new Message(users.get(0), channels.get(0), "공지", "공지입니다.");
        Message message2 = new Message(users.get(1), channels.get(0), "2팀", "안녕하세요.");
        Message message3 = new Message(users.get(4), channels.get(1), "소통", "소통해요");
        Message message4 = new Message(users.get(2), channels.get(1), "소통", "좋아요");


        //-----심화-----//
        //채널 멤버가 아닌 유저가 메시지 생성시_ex)
        Message message5 = new Message(users.get(5), channels.get(1), "소통", "hi");

        //채널에 없는 카테고리에 메시지 생성시_ex)
        Message message6 = new Message(users.get(0), channels.get(0), "공자", "hi");

        System.out.println("<--------------------메시지를 저장합니다----------------------->\n.\n.");
        List<Message> me_i = List.of(message1, message2, message3, message4, message5, message6);
        List<Message> messages = new ArrayList<>();

        for (int i = 0; i < me_i.size(); i++) {
            try {
                messageService.create(me_i.get(i));
                messages.add(me_i.get(i));
                System.out.println("메시지"+(i+1)+" 저장 완료");
            } catch (IllegalArgumentException e) {
                System.out.println("!!메시지" + (i+1) + " 저장 실패!!" + e.getMessage());
            }
        }
        System.out.println(".\n.\n<--------------------메시지 저장 종료-------------------------->\n");
        //-----심화-----//
    }


    private static void demonstrateUserOperations(JCFUserService userService, List<User> users){
        System.out.println("\n########################### User ###############################");

        readUser(userService, users);
        updateUser(userService, users);
        deleteUser(userService, users);
        groupingUser(userService, users);

    }

    private static void readUser(JCFUserService  userService, List<User> users){
        System.out.println("------------------------- ReadUser -----------------------------");
        //전체 유저조회(다건 조회)
        System.out.println("\n전체 유저 목록: ");
        userService.readAll().forEach(System.out::println);

        //특정 유저조회(단건 조회, 이름)
        System.out.println("\n신짱구 유저 조회(이름으로 검색): ");
        userService.read("신짱구").forEach(System.out::println);

        //특정 유저조회(단건 조회, id)
        System.out.println("\n이훈이 유저 조회(id로 검색): ");
        System.out.println(userService.read(users.get(3).getId()).toString());

        //이름에 "구"가 포함된 유저
        System.out.println("\n이름에 '구'가 포함된 유저('구'로 검색): ");
        userService.read("구").forEach(System.out::println);
    }
    private static void updateUser(JCFUserService userService, List<User> users){
        System.out.println("\n------------------------- UpdateUser ---------------------------");
        //유저 수정 테스트
        System.out.println("\n유저 수정하기\n수정 전: ");

        //수정하려는 유저 origin에 저장
        User origin = users.get(2);
        System.out.println(userService.read(origin.getId()));

        //수정한 유저정보 updated에 저장
        User updated = userService.update(origin.getId(), new User("김철수", origin.getGender(), origin.getEmail(), origin.getPhone(), origin.getPassword()));
        System.out.println("수정 후(이름, 번호): ");
        System.out.println(userService.read(updated.getId()));
    }
    private static void deleteUser(JCFUserService userService, List<User> users){
        System.out.println("\n------------------------ DeleteUser ----------------------------");
        //유저 삭제 실패
        System.out.println("\n"+ users.get(3).getName() +" 유저 탈퇴 진행중..\n.\n.\n");
        userService.delete(users.get(3).getId(),"0000");

        //유저 삭제 성공
        System.out.println("\n"+ users.get(3).getName() +"유저 탈퇴 진행중..\n.\n.\n");
        userService.delete(users.get(3).getId(),"2hun222");

        //삭제 후 전체 유저 조회
        System.out.println("\n\n전체 유저 목록: ");
        userService.readAll().forEach(System.out::println);
        System.out.println();
    }

    private static void groupingUser(JCFUserService userService, List<User> users){
        System.out.println("------------------------- GroupingUser -------------------------");
        //성별 그룹화
        System.out.println("\n성별 그룹화");
        userService.groupByGender().forEach((gender, list) -> {
            System.out.println("[" + gender + "]\n" + list);
        });
    }


    private static void demonstrateChannelOperations(JCFChannelService channelService, List<Channel> channels, List<User> users) {
        System.out.println("\n########################### Channel ############################");

        readChannel(channelService,channels,users);
        updateChannel(channelService,channels,users);
        deleteChannel(channelService,channels,users);

    }

    private static void readChannel(JCFChannelService channelService, List<Channel> channels, List<User> users){
        System.out.println("------------------------- ReadChannel --------------------------");
        System.out.println("\n전체 채널 목록");
        channelService.readAll().forEach(channel -> System.out.println("[" + channel.getChannelName() + "]"));

        //특정 채널 정보
        System.out.print("\n[sp01]채널 조회");
        for (Channel channel : channelService.findChannel("sp01")) {
            System.out.println("\n채널명: " + channel.getChannelName());
            System.out.println("카테고리: " + channel.getCategory());
        }

        //채널 멤버조회
        System.out.print("\n[sp01] 채널 멤버: ");
        for (User member : channelService.members(channels.get(0).getId())) {
            System.out.print(member.getName() + " ");
        }

        System.out.print("\n[sp02] 채널 멤버: ");
        for (User member : channelService.members(channels.get(1).getId())) {
            System.out.print(member.getName() + " ");

        }
        System.out.println();
    }
    private static void updateChannel(JCFChannelService channelService, List<Channel> channels, List<User> users){
        System.out.println("\n------------------------- UpdateChannel -----------------------");

        //채널 수정
        System.out.println("\n채널 수정하기\n수정 전: ");
        //수정하려는 채널 original에 저장
        Channel original =  channels.get(2);
        System.out.println(channelService.read(original.getId()));

        //수정한 채널 updated에 저장, 출력(채널명 수정)
        Channel updated = channelService.update(original.getId(), new Channel("변경sp03", original.getCategory(), original.getMembers()));
        System.out.println("수정 후(채널 이름): ");
        System.out.println(updated.toString());
    }
    private static void deleteChannel(JCFChannelService channelService, List<Channel> channels, List<User> users){
        System.out.println("\n------------------------ DeleteChannel ------------------------");

        //채널 삭제
        System.out.println("\n.\n.\n<<채널3 삭제 완료>>\n");
        channelService.delete(channels.get(2).getId());

        //채널 삭제 후 재조회
        System.out.print("삭제 후 전체 채널: \n");
        channelService.readAll().forEach(channel -> System.out.println("[" + channel.getChannelName() + "]"));
    }
    private static void groupingChannel(JCFChannelService channelService, List<Channel> channels, List<User> users) {
        System.out.println("------------------------- GroupingUser -------------------------");
        //채널별 카테고리
        System.out.print("\n채널별 카테고리 목록:\n");
        channelService.groupByChannel().forEach((channelName, list) -> {
            System.out.println("[" + channelName + "]\n" + list);
        });

        //채널별 유저 수
        System.out.println("\n채널별 유저 수:");
        for (Channel channel : channels) {
            int memberCount = channelService.members(channel.getId()).size();
            System.out.println("[" + channel.getChannelName() + "] 채널 유저 수: " + memberCount + "명");
        }
    }


    private static void demonstrateMessageOperations(JCFMessageService messageService) {
        System.out.println("\n########################### Message ###########################");

        readMessages(messageService);
        updateMessages(messageService);
        deleteMessages(messageService);
    }

    private static void readMessages(JCFMessageService messageService) {
        System.out.println("------------------------- ReadMessage -------------------------");
        //메시지 전체조회(다건 조회)
        System.out.println("\n전체 메시지");
        messageService.readAll().forEach(System.out::println);

        //메시지3 조회(단건 조회)
        //-----심화-----//
        try{
            System.out.println("\n메시지3 조회");
            System.out.println(messageService.read(messageService.readAll().get(2).getId()));
            System.out.println(messageService.read(UUID.randomUUID()));

        } catch (IllegalArgumentException e) {
            System.out.println("\n!!메시지 조회 실패!!" + e.getMessage());

        }
        //-----심화-----//
    }
    private static void updateMessages(JCFMessageService messageService) {
        System.out.println("\n------------------------- UpdateMessage ------------------------");
        //수정할 메시지 origin변수에 저장
        Message origin = messageService.read(messageService.readAll().get(0).getId());

        //변경 메시지 updated변수에 저장
        Message updated = new Message(origin.getSender(), origin.getChannel(), origin.getCategory(), "공지 수정합니다~~~~");

        //메시지 수정
        messageService.update(origin.getId(), updated);
        System.out.println("\n메시지1 수정: \n" + messageService.read(origin.getId()));

        //수정후 전체 메시지
        System.out.println("\n전체 메시지");
        messageService.readAll().forEach(System.out::println);
    }
    private static void deleteMessages(JCFMessageService messageService) {
        System.out.println("\n------------------------ DeleteMessage ------------------------");
        //메시지 삭제
        System.out.println("\n.\n.\n<<메시지4 삭제 완료>>\n");
        messageService.delete(messageService.readAll().get(3).getId());

        //메시지 삭제 후 재조회
        System.out.print("삭제 후 전체 메시지: \n");
        messageService.readAll().forEach(System.out::println);
    }

}
