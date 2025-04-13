package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.ServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JavaApplication {
    public static void main(String[] args) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();

        ChannelService channelService = serviceFactory.createChannelService();
        UserService userService = serviceFactory.createUserService();
        MessageService messageService = serviceFactory.createMessageService();


        List<User> users = createAndRegisterUsers(userService);
        List<Channel> channels = createAndRegisterChannels(channelService, users);
        List<Message> messages = createAndRegisterMessages(messageService, users, channels);

        demonstrateUserOperations(userService, users);
        demonstrateChannelOperations(channelService, channels,users);
        demonstrateMessageOperations(messageService);

    }


    //유저생성 및 등록
    private static List<User> createAndRegisterUsers(UserService userService) {
        List<User> users = List.of(
            new User("조현아", "여", "akbkck8101@gmail.com", "010-6658-8101", "2701"),
            new User("신짱구", "남", "zzang9@gmail.com", "010-1234-5678", "9999"),
            new User("김철슈", "남", "steelwater@naver.com", "010-0000-0000", "steelwater"),
            new User("이훈이", "남", "2hun2@naver.com", "010-2345-6789", "2hun222"),
            new User("맹구", "남", "stone_lover9@gmail.com", "010-1010-0101", "stone_lover"),
            new User("한유리", "여", "yuryyy@gmail.com", "010-1111-2222", "12345"),
            //-----심화-----//
            //중복 이메일 유저_ex)
            new User("한유리", "여", "yuryyy@gmail.com", "010-1111-2222", "12345")
        );

        System.out.println("<--------------------유저를 등록합니다------------------------->\n.\n.");

        List<User> registeredUsers = users.stream()
                .filter(user -> {
                    try {
                        userService.create(user);
                        System.out.println("유저 등록 완료: " + user.getName());
                        return true;
                    } catch (IllegalArgumentException e) {
                        System.out.println("!!유저 등록 실패!! " + e.getMessage());
                        return false;
                    }
                })
                .toList();

        System.out.println(".\n.\n<--------------------유저 등록 종료---------------------------->\n");
        return registeredUsers;
        //-----심화-----//
    }

    //채널,멤버,카테고리 생성 및 등록
    private static List<Channel> createAndRegisterChannels(ChannelService channelService, List<User> users) {
        //채널1 멤버, 카테고리생성
        Set<User> members1 = new HashSet<>(Set.of(users.get(0), users.get(1), users.get(3)));
        List<String> cat1 = List.of("공지", "질문", "2팀");

        //채널2 멤버, 카테고리생성
        Set<User> members2 = new HashSet<>(Set.of(users.get(1), users.get(2), users.get(4)));
        List<String> cat2 = List.of("이벤트", "소통");

        //채널3 멤버, 카테고리생성
        Set<User> members3 = new HashSet<>(Set.of(users.get(5)));
        List<String> cat3 = List.of("기타");

        //중복 카테고리
        List<String> cat5 = List.of("공지","공지");

        List<Channel> channels = List.of(
                new Channel("sp01", users.get(1), cat1, members1),
                new Channel("sp02", users.get(1), cat2, members2),
                new Channel("sp03", users.get(5), cat3, members3),
                new Channel("sp03", users.get(5), cat3, members3),   //중복 채널명_ex)
                new Channel("sp05", users.get(5), cat5, members3)   //중복 카테고리_ex)
        );

        System.out.println("<--------------------채널을 등록합니다------------------------->\n.\n.");

        List<Channel> registeredChannels = channels.stream()
                .filter(channel -> {
                    try {
                        channelService.create(channel);
                        System.out.println("채널 등록 완료: " + channel.getChannelName());
                        return true;
                    } catch (IllegalArgumentException e) {
                        System.out.println("!!채널 등록 실패!! " + e.getMessage());
                        return false;
                    }
                })
                .toList(); // 성공한 채널만 리스트로 변환

        System.out.println(".\n.\n<--------------------채널 등록 종료---------------------------->\n");
        return registeredChannels;
    }

    //메시지 생성 및 등록
    private static List<Message> createAndRegisterMessages(MessageService messageService, List<User> users, List<Channel> channels) {
        List<Message> messages = List.of(
                new Message(users.get(0), channels.get(0), "공지", "공지입니다."),
                new Message(users.get(1), channels.get(0), "2팀", "안녕하세요."),
                new Message(users.get(4), channels.get(1), "소통", "소통해요"),
                new Message(users.get(2), channels.get(1), "소통", "좋아요"),
                new Message(users.get(5), channels.get(1), "소통", "hi"),  //채널 멤버가 아닌 유저
                new Message(users.get(0), channels.get(0), "공자", "hi")  //채널에 없는 카테고리
        );

        System.out.println("<--------------------메시지를 저장합니다----------------------->\n.\n.");

        List<Message> registeredMessages = messages.stream()
                .map(message -> {
                    try {
                        messageService.create(message);
                        System.out.println("[" + message.getChannel().getChannelName() + "] 채널에 메시지 등록 완료");
                        return message;
                    } catch (IllegalArgumentException e) {
                        System.out.println("!!메시지 등록 실패!! " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull) // null 제거 (등록 실패한 메시지 필터링)
                .collect(Collectors.toList());

        System.out.println(".\n.\n<--------------------메시지 저장 종료-------------------------->\n");
        return registeredMessages;
    }


    private static void demonstrateUserOperations(UserService userService, List<User> users){
        System.out.println("\n########################### User ###############################");

        readUser(userService, users);
        updateUser(userService, users);
        deleteUser(userService, users);
        groupingUser(userService, users);

    }

    private static void readUser(UserService  userService, List<User> users){
        System.out.println("------------------------- ReadUser -----------------------------");
        //전체 유저조회(다건 조회)
        System.out.println("\n전체 유저 목록: ");
        userService.readAll().forEach(System.out::println);

        //특정 유저조회(단건 조회, 이름)
        System.out.println("\n신짱구 유저 조회(이름으로 검색): ");
        userService.readByName("신짱구").forEach(System.out::println);

        //특정 유저조회(단건 조회, id)
        System.out.println("\n이훈이 유저 조회(id로 검색): ");
        System.out.println(userService.read(users.get(3).getId()).toString());

        //이름에 "구"가 포함된 유저
        System.out.println("\n이름에 '구'가 포함된 유저('구'로 검색): ");
        userService.readByName("구").forEach(System.out::println);
    }
    private static void updateUser(UserService userService, List<User> users){
        System.out.println("\n------------------------- UpdateUser ---------------------------");
        //유저 수정 테스트
        System.out.println("\n유저 수정하기\n수정 전: ");

        //수정하려는 유저 original에 저장
        User original = users.get(2);
        System.out.println(userService.read(original.getId()));

        //수정한 유저정보 updated에 저장
        User updated = userService.update(original.getId(), new User("김철수", original.getGender(), original.getEmail(), "010-0011-0011", original.getPassword()));
        System.out.println("수정 후(이름, 번호): ");
        System.out.println(userService.read(updated.getId()));
    }
    private static void deleteUser(UserService userService, List<User> users){
        System.out.println("\n------------------------ DeleteUser ----------------------------");
        System.out.println("\n전체 유저 목록: ");
        userService.readAll().forEach(System.out::println);

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

    private static void groupingUser(UserService userService, List<User> users){
        System.out.println("------------------------- GroupingUser -------------------------");
        //성별 그룹화
        System.out.println("\n성별 그룹화");
        userService.groupByGender().forEach((gender, list) -> {
            System.out.println("[" + gender + "]\n" + list);
        });
    }


    private static void demonstrateChannelOperations(ChannelService channelService, List<Channel> channels, List<User> users) {
        System.out.println("\n########################### Channel ############################");

        readChannel(channelService,channels,users);
        updateChannel(channelService,channels,users);
        deleteChannel(channelService,channels,users);
        groupingChannel(channelService, channels, users);
    }

    private static void readChannel(ChannelService channelService, List<Channel> channels, List<User> users){
        System.out.println("------------------------- ReadChannel --------------------------");
        System.out.println("\n전체 채널 목록");

        if (channelService.readAll().isEmpty()) {
            System.out.println("조회할 채널이 없습니다.");
        } else {
            channels.forEach(channel -> System.out.println("[" + channel.getChannelName() + "]"));
        }


        //특정 채널 정보
        try {
            Channel channel = channelService.read(channels.get(0).getId());
            System.out.println("\n채널명: " + channel.getChannelName());
            System.out.println("카테고리: " + channel.getCategory());

        } catch (IllegalArgumentException e) {
            System.out.println("!!채널 조회 실패!! " + e.getMessage());
        }

        //채널 멤버조회
        try {
            System.out.print("\n[sp01] 채널 멤버: ");
            for (User member : channelService.members(channels.get(0).getId())) {
                System.out.print(member.getName() + " ");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("!!채널 조회 실패!! " + e.getMessage());
        }
        System.out.println();
    }
    private static void updateChannel(ChannelService channelService, List<Channel> channels, List<User> users){
        System.out.println("\n------------------------- UpdateChannel -----------------------");

        //채널 수정
        Channel original = channels.get(2);     //수정하려는 채널

        System.out.println("\n["+ original.getChannelName() +"] 채널 수정 진행중\n.\n.\n.");
        System.out.println("\n["+ original.getChannelName() +"] 수정 완료(채널 이름): ");
        System.out.println("수정 전: " + channelService.read(original.getId()));

        //수정한 채널 updated에 저장, 출력(채널명 수정)
        try {
            Channel updatedChannel = channelService.update(original.getId(), new Channel("변경sp03", original.getKeyUser(),original.getCategory(), original.getMembers()));
            System.out.println("수정 후: " + channelService.read(updatedChannel.getId()));
        } catch (IllegalArgumentException e) {
            System.out.println("!!채널 수정 실패!! " + e.getMessage());
        }

        //수정한 채널 updated에 저장, 출력(방장 변경 + member에 추가)
        System.out.println("\n["+ original.getChannelName() +"] 채널 수정 진행중\n.\n.\n.");
        System.out.println("\n["+ original.getChannelName() +"] 수정 완료(새 유저로 방장 변경): ");
        System.out.println("수정 전: " + channelService.read(original.getId()));

        try {
            Channel updatedChannel = channelService.update(original.getId(), new Channel(original.getChannelName(), users.get(1), original.getCategory(), original.getMembers()));
            System.out.println("수정 후: " + channelService.read(updatedChannel.getId()));
        } catch (IllegalArgumentException e) {
            System.out.println("!!채널 수정 실패!! " + e.getMessage());
        }

        //기존 유저에서 방장변경
        //채널1에서 변경
        original = channels.get(0);
        System.out.println("\n["+ original.getChannelName() +"] 채널 수정 진행중\n.\n.\n.");
        System.out.println("\n["+ original.getChannelName() +"] 수정 완료(기존 유저로 방장 변경): ");
        System.out.println("수정 전: " + channelService.read(original.getId()));

        try {
            Channel updatedChannel = channelService.update(original.getId(), new Channel(original.getChannelName(), users.get(0), original.getCategory(), original.getMembers()));
            System.out.println("수정 후: " + channelService.read(updatedChannel.getId()));
        } catch (IllegalArgumentException e) {
            System.out.println("!!채널 수정 실패!! " + e.getMessage());
        }

    }
    private static void deleteChannel(ChannelService channelService, List<Channel> channels, List<User> users){
        System.out.println("\n------------------------ DeleteChannel ------------------------");

        System.out.print("\n전체 채널 목록: \n");
        channelService.readAll().forEach(System.out::println);

        //채널 삭제 실패
        System.out.println("\n["+ channels.get(2).getChannelName() +"] 채널 삭제 진행중..\n.\n.\n");
        channelService.delete(channels.get(2).getId(), users.get(1), "0000");

        //채널 삭제 성공
        System.out.println("\n["+ channels.get(2).getChannelName() +"] 채널 삭제 진행중..\n.\n.\n");
        channelService.delete(channels.get(2).getId(), users.get(1), "9999");

        //삭제 후 전체 유저 조회
        System.out.print("\n전체 채널 목록: \n");
        channelService.readAll().forEach(System.out::println);

    }
    private static void groupingChannel(ChannelService channelService, List<Channel> channels, List<User> users) {
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


    private static void demonstrateMessageOperations(MessageService messageService) {
        System.out.println("\n########################### Message ###########################");

        readMessages(messageService);
        updateMessages(messageService);
        deleteMessages(messageService);
    }

    private static void readMessages(MessageService messageService) {
        System.out.println("------------------------- ReadMessage -------------------------");
        //메시지 전체조회(다건 조회)
        System.out.println("\n전체 메시지");
        messageService.readAll().forEach(System.out::println);

        //메시지3 조회(단건 조회)
        try{
            System.out.println("\n메시지 조회");
            System.out.println(messageService.read(messageService.readAll().get(2).getId()));

        } catch (IllegalArgumentException e) {
            System.out.println("\n!!메시지 조회 실패!!" + e.getMessage());

        }

        //존재하지 않는 메시지 조회
        try{
            System.out.println("\n메시지 조회");
            System.out.println(messageService.read(UUID.randomUUID()));
        } catch (IllegalArgumentException e) {
            System.out.println("\n!!메시지 조회 실패!!" + e.getMessage());
        }
    }
    private static void updateMessages(MessageService messageService) {
        System.out.println("\n------------------------- UpdateMessage ------------------------");
        //수정할 메시지 original변수에 저장
        Message original = messageService.read(messageService.readAll().get(0).getId());
        System.out.println("\n메시지1 수정 전: \n" + messageService.read(original.getId()));

        //변경 메시지 updated변수에 저장
        Message updated = new Message(original.getSender(), original.getChannel(), original.getCategory(), "공지 수정합니다~~~~");

        //메시지 수정
        messageService.update(original.getId(), updated);
        System.out.println("\n메시지1 수정 후: \n" + messageService.read(original.getId()));

        //수정후 전체 메시지
        System.out.println("\n전체 메시지");
        messageService.readAll().forEach(System.out::println);
    }
    private static void deleteMessages(MessageService messageService) {
        System.out.println("\n------------------------ DeleteMessage ------------------------");
        //메시지 삭제
        System.out.println("\n.\n.\n<<메시지4 삭제 완료>>\n");
        messageService.delete(messageService.readAll().get(3).getId());

        //메시지 삭제 후 재조회
        System.out.print("삭제 후 전체 메시지: \n");
        messageService.readAll().forEach(System.out::println);
    }

}
