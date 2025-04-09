package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;


import java.util.*;

public class JavaApplication {
    public static void main(String[] args) {

        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
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

        List<User> users = List.of(user1, user2, user3, user4, user5, user6);
        users.forEach(userService::create);
        return users;
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

        List<Channel> channels = List.of(channel1, channel2, channel3);
        channels.forEach(channelService::create);
        return channels;
    }

    //메시지 생성 및 등록
    private static void createAndRegisterMessages(JCFMessageService messageService, List<User> users, List<Channel> channels) {
        Message message1 = new Message(users.get(0), channels.get(0), "공지", "공지입니다.");
        Message message2 = new Message(users.get(1), channels.get(0), "2팀", "안녕하세요.");
        Message message3 = new Message(users.get(0), channels.get(1), "소통", "소통해요");
        Message message4 = new Message(users.get(2), channels.get(1), "소통", "좋아요");

        messageService.create(message1);
        messageService.create(message2);
        messageService.create(message3);
        messageService.create(message4);
    }

    private static void demonstrateUserOperations(JCFUserService userService, List<User> users){
        System.out.println("\n--------------------------- User ---------------------------");
        System.out.println("전체 유저 목록: ");
        userService.readAll().forEach(System.out::println);

        System.out.println("\n신짱구 유저 조회(이름으로 검색): ");
        userService.read("신짱구").forEach(System.out::println);

        System.out.println("\n이훈이 유저 조회(id로 검색): ");
        System.out.println(userService.read(users.get(3).getId()).toString());

        //이름에 "구"가 포함된 유저
        System.out.println("\n이름에 '구'가 포함된 유저('구'로 검색): ");
        userService.read("구").forEach(System.out::println);

        //유저 수정 테스트
        System.out.println("\n유저 수정하기\n수정 전: ");
        User origin = users.get(2);
        System.out.println(userService.read(origin.getId()));

        User updated = userService.update(origin.getId(), new User("김철수", origin.getGender(), origin.getEmail(), origin.getPhone(), origin.getPassword()));
        System.out.println("수정 후(이름, 번호): ");
        System.out.println(userService.read(updated.getId()));

        //성별 그룹화
        System.out.println("\n성별 그룹화");
        userService.groupByGender().forEach((gender, list) -> {
            System.out.println("\n[" + gender + "]\n" + list);
        });

        //유저 삭제
        System.out.println("\n.\n.\n<<이훈이 삭제>>\n");
        userService.delete(users.get(3).getId());

        //삭제 후 전체 유저 조회
        System.out.println("전체 유저 목록: ");
        userService.readAll().forEach(System.out::println);

    }

    private static void demonstrateChannelOperations(JCFChannelService channelService, List<Channel> channels, List<User> users) {
        System.out.println("\n--------------------------- Channel ---------------------------");

        System.out.println("\n전체 채널 목록");
        channelService.readAll().forEach(channel -> System.out.println("[" + channel.getChannelName() + "]"));

        //특정 채널 정보
        System.out.print("\n[sp01]채널 조회");
        for (Channel channel : channelService.findChannel("sp01")) {
            System.out.println("\n채널명: " + channel.getChannelName());
            System.out.println("카테고리: " + channel.getCategory());
        }

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

        System.out.print("\n[sp01] 채널 멤버: ");
        for (User member : channelService.members(channels.get(0).getId())) {
            System.out.print(member.getName() + " ");
        }

        System.out.print("\n[sp02] 채널 멤버: ");
        for (User member : channelService.members(channels.get(1).getId())) {
            System.out.print(member.getName() + " ");

        }

        //채널의 유저삭제(멤버 변경)
        UUID targetId = users.get(3).getId();   //타겟지정(유저4)
        Channel channel1 = channels.get(1);     //채널선택(채널2)

        // 채널2의 멤버셋에서 삭제
        Set<User> updatedMembers = new HashSet<>(channel1.getMembers());
        updatedMembers.removeIf(user -> user.getId().equals(targetId));

        // 채널2 정보 업데이트
        Channel updatedChannel1 = new Channel(channel1.getChannelName(), channel1.getCategory(), updatedMembers);
        channelService.update(channel1.getId(), updatedChannel1);

        //채널1 멤버확인
        System.out.print("\n[sp02] 채널 멤버: ");
        for (User member : channelService.members(channels.get(1).getId())) {
            System.out.print(member.getName() + " ");

        }

        //채널 수정
        System.out.println("\n\n채널 수정하기\n수정 전: ");
        Channel original =  channels.get(2);
        System.out.println(channelService.read(original.getId()));

        Channel updated = channelService.update(original.getId(), new Channel("변경sp03", original.getCategory(), original.getMembers()));
        System.out.println("수정 후(채널 이름): ");
        System.out.println(updated.toString());

        System.out.println("\n.\n.\n<<채널3 삭제>>\n");
        channelService.delete(channels.get(2).getId());

        //채널 삭제 후 재조회
        System.out.print("삭제 후 전체 채널: \n");
        channelService.readAll().forEach(channel -> System.out.println("[" + channel.getChannelName() + "]"));

    }

    private static void demonstrateMessageOperations(JCFMessageService messageService) {
        System.out.println("\n--------------------------- Message ---------------------------");
        //메시지 전체조회(다건 조회)
        System.out.println("전체 메시지");
        messageService.readAll().forEach(System.out::println);

        //메시지3 조회(단건 조회)
        System.out.println("\n메시지3 조회");
        System.out.println(messageService.read(messageService.readAll().get(2).getId()));


        //수정할 메시지 origin변수에 저장
        Message origin = messageService.read(messageService.readAll().get(2).getId());

        //변경 메시지 updated변수에 저장
        Message updated = new Message(origin.getSender(), origin.getChannel(), origin.getCategory(), "공지 수정합니다~~~~");

        //메시지 수정
        messageService.update(origin.getId(), updated);
        System.out.println("\n메시지3 수정: \n" + messageService.read(origin.getId()));

        //수정후 전체 메시지
        System.out.println("\n\n전체 메시지");
        messageService.readAll().forEach(System.out::println);

        //메시지 삭제
        System.out.println("\n.\n.\n<<메시지4 삭제>>\n");
        messageService.delete(messageService.readAll().get(3).getId());

        //메시지 삭제 후 재조회
        System.out.print("삭제 후 전체 메시지: \n");
        messageService.readAll().forEach(System.out::println);


    }

}
