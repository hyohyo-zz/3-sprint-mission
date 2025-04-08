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
        JCFMessageService messageService = new JCFMessageService();

        //유저데이터
        User user1 = new User("조현아", "여", "akbkck8101@gmail.com", "010-6658-8101", "2701");
        User user2 = new User("신짱구", "남", "zzang9@gmail.com", "010-1234-5678", "9999");
        User user3 = new User("김철슈", "남", "steelwater@naver.com", "010-0000-0000", "steelwater");
        User user4 = new User("이훈이", "남", "2hun2@naver.com", "010-2345-6789", "2hun222");
        User user5 = new User("맹구", "남", "stone_lover9@gmail.com", "010-1010-0101", "stone_lover");
        User user6 = new User("한유리", "여", "yuryyy@gmail.com", "010-1111-2222", "12345");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);
        userService.create(user5);
        userService.create(user6);


        //채널에 유저 멤버
        Set<User> members1 = new HashSet<>();
        members1.add(user1);
        members1.add(user2);
        members1.add(user4);

        Set<User> members2 = new HashSet<>();
        members2.add(user2);
        members2.add(user3);
        members2.add(user5);


        Set<User> members3 = new HashSet<>();
        members3.add(user6);
        //카테고리

        List<String> cat1 = new ArrayList<>();
        cat1.add("공지");
        cat1.add("질문");
        cat1.add("2팀");

        List<String> cat2 = new ArrayList<>();
        cat2.add("이벤트");
        cat2.add("소통");

        List<String> cat3 = new ArrayList<>();
        cat3.add("기타");

        //채널 생성
        Channel channel1 = new Channel("sp01", cat1, members1);
        Channel channel2 = new Channel("sp02", cat2, members2);
        Channel channel3 = new Channel("sp03", cat3, members3);
        channelService.create(channel1);
        channelService.create(channel2);
        channelService.create(channel3);
        List<Channel>  channels = List.of(channel1,channel2,channel3);

        Message message1 = new Message(user1, channel1, channel1.getCategory().get(0), "공지입니다.");
        Message message2 = new Message(user2, channel1, channel1.getCategory().get(2), "안녕하세요.");
        Message message3 = new Message(user1, channel2, channel2.getCategory().get(1), "소통해요");
        Message message4 = new Message(user3, channel2, channel2.getCategory().get(1), "좋아요");

        messageService.create(message1);
        messageService.create(message2);
        messageService.create(message3);
        messageService.create(message4);


        System.out.println("\n-------------------- User ------------------------------------------------------------------------------");
        System.out.println("전체 유저 목록: ");
        userService.readAll().forEach(System.out::println);

        System.out.println("\n신짱구 유저 조회(이름으로 검색): ");
        userService.read("신짱구").forEach(System.out::println);

        System.out.println("\n이훈이 유저 조회(id로 검색): ");
        System.out.println(userService.read(user4.getId()).toString());


        //이름에 "구"가 포함된 유저
        System.out.println("\n이름에 '구'가 포함된 유저('구'로 검색): ");
        userService.read("구").forEach(System.out::println);

        //유저 수정 테스트
        System.out.println("\n유저 수정하기\n수정 전: ");
        System.out.println(userService.read(user3.getId()).toString());

        User updatedUser = userService.update(user3.getId(), new User("김철수", "남", "steelwater@naver.com", "010-1155-0000", "steelwater"));
        System.out.println("수정 후(이름, 번호): ");
        System.out.println(updatedUser.toString());

        //성별 그룹화
        System.out.println("\n성별 그룹화");
        userService.groupByGender().forEach((gender, list) -> {
            System.out.println("\n["+gender + "]\n" + list);
        });

        System.out.print("\n[sp01] 채널에 있는 유저: \n");
        members1.forEach(System.out::println);

        System.out.println("\n.\n.\n<<이훈이 삭제>>\n");
        userService.delete(user4.getId());
        //멤버에서도 지우기
        members1.remove(user4);

        //멤버에서 삭제 후 재조회
        System.out.print("삭제 후 [sp01] 채널에 있는 유저: \n");
        members1.forEach(System.out::println);

        System.out.println("\n삭제 후 전체 유저 목록: ");
        userService.readAll().forEach(System.out::println);



        System.out.println("\n-------------------- Channel ------------------------------------------------------------------------------");

        System.out.println("\n전체 채널 목록");
        channelService.readAll().forEach(channel -> System.out.println("["+channel.getChannelName()+"]"));

        //특정 채널 정보
        System.out.print("\n[sp01]채널 조회");
        for (Channel channel : channelService.findChannel("sp01")) {
            System.out.println("\n채널명: " + channel.getChannelName());
            System.out.println("카테고리: " + channel.getCategory());
        }

        //채널별 카테고리
        System.out.print("\n채널별 카테고리 목록:\n");
        channelService.groupByChannel().forEach((channelName, list) -> {
            System.out.println("["+channelName + "]\n" + list);
        });

        //채널별 유저 수
        System.out.println("\n채널별 유저 수:");
        for (Channel channel : channels) {
            int memberCount = channelService.members(channel.getId()).size();
            System.out.println("["+channel.getChannelName() + "] 채널 유저 수: " + memberCount + "명");
        }

        System.out.print("\n[sp01] 채널 멤버: ");
        for ( User member : channelService.members(channel1.getId())){
            System.out.print(member.getName()+" ");
        }

        System.out.print("\n[sp02] 채널 멤버: ");
        for ( User member : channelService.members(channel2.getId())){
            System.out.print(member.getName()+" ");
        }

        //채널 수정
        System.out.println("\n\n채널 수정하기\n수정 전: ");
        System.out.println(channelService.read(channel3.getId()).toString());

        Channel updatedChannel = channelService.update(channel3.getId(), new Channel("변경sp03", cat3, members3));
        System.out.println("수정 후(채널 이름): ");
        System.out.println(updatedChannel.toString());

        System.out.println("\n.\n.\n<<채널3 삭제>>\n");
        channelService.delete(channel3.getId());

        //채널 삭제 후 재조회
        System.out.print("삭제 후 전체 채널: \n");
        channelService.readAll().forEach(channel -> System.out.println("["+channel.getChannelName()+"]"));


        System.out.println("\n-------------------- Message ------------------------------------------------------------------------------");
        System.out.println("전체 메시지");
        messageService.readAll().forEach(System.out::println);

        System.out.println("\n메시지3 조회");
        System.out.println(messageService.read(message3.getId()));

        Message m1 = messageService.read(message1.getId());

        Message updated = new Message(m1.getSender(), m1.getChannel(), m1.getCategory(),"공지 수정합니다~~~~");

        messageService.update(message1.getId(), updated);
        System.out.println("\n수정된 메시지: \n" + messageService.read(message1.getId()).toString());

        //수정후 전체 메시지
        System.out.println("\n\n전체 메시지");
        messageService.readAll().forEach(System.out::println);

        System.out.println("\n.\n.\n<<메시지4 삭제>>\n");
        messageService.delete(message4.getId());

        //채널 삭제 후 재조회
        System.out.print("삭제 후 전체 메시지: \n");
        messageService.readAll().forEach(System.out::println);



    }
}
