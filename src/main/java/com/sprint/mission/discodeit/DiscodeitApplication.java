package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.config.DiscodeitProperties;
import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.dto.Response.MessageResponse;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_private;
import com.sprint.mission.discodeit.dto.request.create.ChannelCreateRequest_public;
import com.sprint.mission.discodeit.dto.request.create.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.create.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.update.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.update.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.DataInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableConfigurationProperties(DiscodeitProperties.class)
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		//초기화
		DataInitializer initializer = context.getBean(DataInitializer.class);
		initializer.clearSerializedData();

		context.close();
		context = SpringApplication.run(DiscodeitApplication.class, args);

		//콩받기
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);


		List<UserResponse> users = createAndRegisterUsers(userService);
		List<ChannelResponse> channels = createAndRegisterChannels(channelService, users);
		List<MessageResponse> messages = createAndRegisterMessages(messageService, users, channels);

		demonstrateUserOperations(userService, users);
		demonstrateChannelOperations(channelService, channels, users);
		demonstrateMessageOperations(messageService, messages, channels);

	}

	//유저생성 및 등록
	private static List<UserResponse> createAndRegisterUsers(UserService userService) {
		List<UserCreateRequest> users = List.of(
				new UserCreateRequest("조현아", "akbkck8101@gmail.com", "010-6658-8101", "2701",null,null),
				new UserCreateRequest("신짱구",  "zzang9@gmail.com", "010-1234-5678", "9999",null,null),
				new UserCreateRequest("김철슈", "steelwater@naver.com", "010-0000-0000", "steelwater", null,null),
				new UserCreateRequest("이훈이", "2hun2@naver.com", "010-2345-6789", "2hun222",null,null),
				new UserCreateRequest("맹구", "stone_lover9@gmail.com", "010-1010-0101", "stone_lover",null,null),
				new UserCreateRequest("한유리",  "yuryyy@gmail.com", "010-1111-2222", "12345",null,null),
				new UserCreateRequest("한유리", "yuryyy@gmail.com", "010-1111-2222", "12345",null,null)  //중복 이메일 유저_ex)
		);

		System.out.println("<--------------------유저를 등록합니다------------------------->\n.\n.");

		List<UserResponse> registeredUsers = users.stream()
				.map(user -> {
					try {
						UserResponse response = userService.create(user);
						System.out.println("유저 등록 완료: " + user.name());
						return response;
					} catch (IllegalArgumentException e) {
						System.out.println("!!유저 등록 실패!! " + e.getMessage());
						return null;
					}
				})
				.filter(Objects::nonNull)
				.toList();

		System.out.println(".\n.\n<--------------------유저 등록 종료---------------------------->\n");
		return registeredUsers;
	}

	//채널,멤버,카테고리 생성 및 등록
	private static List<ChannelResponse> createAndRegisterChannels(ChannelService channelService, List<UserResponse> users) {
		//채널1 멤버, 카테고리생성
		Set<UUID> members1 = Set.of(users.get(0).id(), users.get(1).id(), users.get(3).id());
		List<String> cat1 = List.of("공지", "질문", "2팀");

		Set<UUID> members2 = Set.of(users.get(1).id(), users.get(2).id(), users.get(4).id());
		List<String> cat2 = List.of("이벤트", "소통");

		Set<UUID> members3 = Set.of(users.get(5).id());
		List<String> cat3 = List.of("기타");

		//중복 카테고리
		List<String> cat5 = List.of("공지","공지");

		List<Map<String, Object>> channelConfigs = List.of(
				Map.of("name", "sp01", "creator", users.get(1), "categories", cat1, "members", members1, "isPrivate", false),
				Map.of("name", "sp02", "creator", users.get(1), "categories", cat2, "members", members2, "isPrivate", false),
				Map.of("name", "sp03", "creator", users.get(5), "categories", cat3, "members", members3, "isPrivate", false),
				Map.of("name", "sp03", "creator", users.get(5), "categories", cat3, "members", members3, "isPrivate", false),
				Map.of("name", "sp05", "creator", users.get(5), "categories", cat5, "members", members3, "isPrivate", false),
				Map.of("name", "", "creator", users.get(5), "categories", new ArrayList<>(), "members", members3, "isPrivate", true),
				Map.of("name", "", "creator", users.get(5), "categories", new ArrayList<>(), "members", members3, "isPrivate", true)
		);

		System.out.println("<--------------------채널을 등록합니다------------------------->\n.\n.");
		List<ChannelResponse> registeredChannels = new ArrayList<>();

		for (Map<String, Object> channelConfig : channelConfigs) {
			String name = (String) channelConfig.get("name");
			UserResponse creator = (UserResponse) channelConfig.get("creator");
			List<String> categories = (List<String>) channelConfig.get("categories");
			Set<UUID> members = (Set<UUID>) channelConfig.get("members");
			boolean isPrivate = (boolean) channelConfig.get("isPrivate");

			try {
				ChannelResponse response;
				if (isPrivate) {
					// PRIVATE 채널은 이름/카테고리 없이 생성
					var request = new ChannelCreateRequest_private(creator.id(), members);
					response = channelService.createPrivateChannel(request);
				} else {
					var request = new ChannelCreateRequest_public(name, creator.id(), categories, members);
					response = channelService.createPublicChannel(request);
				}

				registeredChannels.add(response);
				if(isPrivate){
					System.out.println("Private 채널 등록 완료");
				} else System.out.println("채널 등록 완료: " + name);
				// 실제 저장된 채널을 찾아서 registeredChannels에 추가하려면 Repository에서 다시 조회 필요
			} catch (IllegalArgumentException e) {
				System.out.println("!!채널 등록 실패!! " + name + ": " + e.getMessage());
			}
		}

		System.out.println(".\n.\n<--------------------채널 등록 종료---------------------------->\n");
		return registeredChannels;
	}

	//메시지 생성 및 등록
	private static List<MessageResponse> createAndRegisterMessages(MessageService messageService, List<UserResponse> users, List<ChannelResponse> channels) {
		List<MessageCreateRequest> messages = List.of(
				new MessageCreateRequest(users.get(0).id(), channels.get(0).channelId(), "공지", "공지입니다.",null),
				new MessageCreateRequest(users.get(1).id(), channels.get(0).channelId(), "2팀", "안녕하세요.",null),
				new MessageCreateRequest(users.get(4).id(), channels.get(1).channelId(), "소통", "소통해요",null),
				new MessageCreateRequest(users.get(2).id(), channels.get(1).channelId(), "소통", "좋아요",null),
				new MessageCreateRequest(users.get(5).id(), channels.get(1).channelId(), "소통", "hi",null),  //채널 멤버가 아닌 유저
				new MessageCreateRequest(users.get(0).id(), channels.get(0).channelId(), "공자", "hi",null)   //채널에 없는 카테고리
		);

		System.out.println("<--------------------메시지를 저장합니다----------------------->\n.\n.");

		Map<UUID, String> channelIdToName = channels.stream()
				.collect(Collectors.toMap(ChannelResponse::channelId, ChannelResponse::channelName));

		List<MessageResponse> registeredMessages = messages.stream()
				.map(request -> {
					try {
						MessageResponse response = messageService.create(request);
						System.out.println("[" + channelIdToName.get(request.channelId()) + "] 채널에 메시지 등록 완료");
						return response;
					} catch (IllegalArgumentException e) {
						System.out.println("!!메시지 등록 실패!! " + e.getMessage());
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		System.out.println(".\n.\n<--------------------메시지 저장 종료-------------------------->\n");
		return registeredMessages;
	}


	private static void demonstrateUserOperations(UserService userService, List<UserResponse> users){
		System.out.println("\n########################### User ###############################");

		findUser(userService, users);
		updateUser(userService, users);
		deleteUser(userService, users);

	}

	private static void findUser(UserService  userService, List<UserResponse> users){
		System.out.println("------------------------- ReadUser -----------------------------");
		//전체 유저조회(다건 조회)
		System.out.println("\n전체 유저 목록: ");
		userService.findAll().forEach(System.out::println);

		//특정 유저조회(단건 조회, 이름)
		System.out.println("\n신짱구 유저 조회(이름으로 검색): ");
		System.out.println(userService.findByUserName("신짱구"));

		//특정 유저조회(단건 조회, id)
		System.out.println("\n이훈이 유저 조회(id로 검색): ");
		System.out.println(userService.find(users.get(3).id()).toString());

		//이름에 "구"가 포함된 유저
		System.out.println("\n이름에 '구'가 포함된 유저('구'로 검색): ");
		System.out.println(userService.findByUserNameKeyWords("구"));
	}
	private static void updateUser(UserService userService, List<UserResponse> users){
		System.out.println("\n------------------------- UpdateUser ---------------------------");
		//유저 수정 테스트
		System.out.println("\n유저 수정하기\n수정 전: ");

		//수정하려는 유저 original에 저장
		UserResponse original = users.get(2);
		System.out.println(userService.find(original.id()));

		//수정한 유저정보 updated에 저장
		UserResponse updated = userService.update( new UserUpdateRequest(original.id(),"김철수", original.email(), "010-0011-0011", original.password(), null));
		System.out.println("수정 후(이름, 번호): ");
		System.out.println(userService.find(updated.id()));
	}
	private static void deleteUser(UserService userService, List<UserResponse> users){
		System.out.println("\n------------------------ DeleteUser ----------------------------");
		System.out.println("\n전체 유저 목록: ");
		userService.findAll().forEach(System.out::println);

		//유저 삭제 실패
		System.out.println("\n"+ users.get(3).userName() +" 유저 탈퇴 진행중..\n.\n.\n");
		userService.delete(users.get(3).id(),"0000");

		//유저 삭제 성공
		System.out.println("\n"+ users.get(3).userName() +"유저 탈퇴 진행중..\n.\n.\n");
		userService.delete(users.get(3).id(),"2hun222");

		//삭제 후 전체 유저 조회
		System.out.println("\n\n전체 유저 목록: ");
		userService.findAll().forEach(System.out::println);
		System.out.println();
	}

	private static void demonstrateChannelOperations(ChannelService channelService, List<ChannelResponse> channels, List<UserResponse> users) {
		System.out.println("\n########################### Channel ############################");

		findChannel(channelService, users);
		updateChannel(channelService,channels,users);
		deleteChannel(channelService,channels,users);
	}

	private static void findChannel(ChannelService channelService, List<UserResponse> users){
		System.out.println("------------------------- findChannel --------------------------");
		System.out.println("\n전체 채널 목록");

		//신짱구 유저가 있는 채널 조회
		UUID userId = users.get(1).id();
		List<ChannelResponse> userChannels = channelService.findAllByUserId(userId);

		if (userChannels.isEmpty()) {
			System.out.println("조회할 채널이 없습니다.");
		} else {
			userChannels.forEach(channel -> System.out.println("[" + channel.channelName() + "]"));
		}

		//특정 채널 정보
		try {
			ChannelResponse channel = channelService.find(userChannels.get(0).channelId());
			System.out.println("\n채널명: " + channel.channelName());
			System.out.println("카테고리: " + channel.categories());

		} catch (IllegalArgumentException e) {
			System.out.println("!!채널 조회 실패!! " + e.getMessage());
		}


		//조회 위해 private채널 별도 저장
		Set<UUID> members = Set.of(users.get(1).id(), users.get(2).id(), users.get(5).id());

		ChannelResponse privateChannel = channelService.createPrivateChannel(
				new ChannelCreateRequest_private(users.get(5).id(), members));

		//private 채널 조회
		try {
			System.out.println("\nPrivate 채널 조회");
			ChannelResponse channel = channelService.find(privateChannel.channelId());
			System.out.println(channel);

		} catch (IllegalArgumentException e) {
			System.out.println("!!채널 조회 실패!! " + e.getMessage());
		}

		//sp01채널 멤버조회
		System.out.println("\n[sp01]채널 멤버 조회");
		ChannelResponse sp01 = userChannels.stream()
				.filter(c -> "sp01".equals(c.channelName()))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("[sp01]채널을 찾을 수 없습니다."));

		for (User member : channelService.members(sp01.channelId())) {
			System.out.print(member.getName() + " ");
		}

		System.out.println();
	}
	private static void updateChannel(ChannelService channelService, List<ChannelResponse> channels, List<UserResponse> users){
		System.out.println("\n------------------------- UpdateChannel -----------------------");

		//채널 수정
		ChannelResponse original = channels.get(2);    //수정하려는 채널

		System.out.println("\n["+ original.channelName() +"] 채널 수정 진행중\n.\n.\n.");
		System.out.println("수정 전: " + channelService.find(original.channelId()));

		//수정한 채널 updated에 저장, 출력(채널명 수정)
		try {
			ChannelUpdateRequest updatedChannel1 = new ChannelUpdateRequest(
					original.channelId(), "변경sp03", original.categories(), original.creatorId());
			ChannelResponse success_updatedChannel1 = channelService.update(updatedChannel1);
			System.out.println("수정 후: " + success_updatedChannel1);
		} catch (IllegalArgumentException e) {
			System.out.println("!!채널 수정 실패!! " + e.getMessage());
		}

		//수정한 채널 updated에 저장, 출력(방장 변경 + member에 추가)
		System.out.println("\n["+ original.channelName() +"] 채널 수정 진행중\n.\n.\n.");
		System.out.println("\n["+ original.channelName() +"] 수정 완료(새 유저로 방장 변경): ");
		System.out.println("수정 전: " + channelService.find(original.channelId()));

		try {
			ChannelUpdateRequest updatedChannel2 = new ChannelUpdateRequest(
					original.channelId(), original.channelName(), original.categories(), users.get(1).id());
			ChannelResponse success_updatedChannel2 = channelService.update(updatedChannel2);
			System.out.println("방장 변경 후: " + success_updatedChannel2);
		} catch (IllegalArgumentException e) {
			System.out.println("!!방장 변경 실패!! " + e.getMessage());
		}

		//기존 유저에서 방장변경
		//채널1에서 변경
		original = channels.get(0);
		System.out.println("\n["+ original.channelName() +"] 채널 수정 진행중\n.\n.\n.");
		System.out.println("\n["+ original.channelName() +"] 수정 완료(기존 유저로 방장 변경): ");
		System.out.println("수정 전: " + channelService.find(original.channelId()));

		try {
			ChannelUpdateRequest updatedChannel3 = new ChannelUpdateRequest(
					original.channelId(), original.channelName(), original.categories(), users.get(0).id());
			ChannelResponse success_updatedChannel3 = channelService.update(updatedChannel3);
			System.out.println("수정 후: " + success_updatedChannel3);
		} catch (IllegalArgumentException e) {
			System.out.println("!!채널 수정 실패!! " + e.getMessage());
		}
	}
	private static void deleteChannel(ChannelService channelService, List<ChannelResponse> channels, List<UserResponse> users){
		System.out.println("\n------------------------ DeleteChannel ------------------------");

		//삭제 시도할 유저
		UUID userId = users.get(1).id();

		System.out.print("\n전체 채널 목록: \n");
		channelService.findAllByUserId(userId).forEach(System.out::println);

		//채널 삭제 실패
		System.out.println("\n["+ channels.get(2).channelName() + "] 채널 삭제 진행중..\n.\n.\n");
		try {
			channelService.delete(channels.get(2).channelId(), userId, "0000"); // 비밀번호가 다를 수 있음
			System.out.println("삭제 성공!");
		} catch (IllegalArgumentException e) {
			System.out.println("삭제 실패: " + e.getMessage()); // → 비밀번호 불일치로 삭제 실패
		}

		//채널 삭제 성공
		System.out.println("\n["+ channels.get(2).channelName()+"] 채널 삭제 진행중..\n.\n.\n");
		try {
			channelService.delete(channels.get(2).channelId(), userId, "9999"); // 비밀번호가 다를 수 있음
			System.out.println("삭제 성공!");
		} catch (IllegalArgumentException e) {
			System.out.println("삭제 실패: " + e.getMessage()); // → 비밀번호 불일치로 삭제 실패
		}

		//삭제 후 전체 유저 조회
		System.out.print("\n전체 채널 목록: \n");
		channelService.findAllByUserId(userId).forEach(System.out::println);
	}

	private static void demonstrateMessageOperations(MessageService messageService, List<MessageResponse> messages, List<ChannelResponse> channels) {
		System.out.println("\n########################### Message ###########################");

		findMessages(messageService, channels);
		updateMessages(messageService, messages, channels);
		deleteMessages(messageService, messages, channels);
	}

	private static void findMessages(MessageService messageService, List<ChannelResponse> channels) {
		System.out.println("------------------------- ReadMessage -------------------------");
		//메시지 전체조회(다건 조회)
		System.out.println("\n전체 메시지");
		messageService.findAllByChannelId(channels.get(0).channelId()).forEach(System.out::println);

		//메시지3 조회(단건 조회)
		try{
			UUID messageId = messageService.findAllByChannelId(channels.get(0).channelId()).get(0).id();
			System.out.println("\n메시지 조회");
			System.out.println(messageService.find(messageId));

		} catch (IllegalArgumentException e) {
			System.out.println("\n!!메시지 조회 실패!!" + e.getMessage());

		}

		//존재하지 않는 메시지 조회
		try{
			System.out.println("\n존재하지 않는 메시지 조회");
			System.out.println(messageService.find(UUID.randomUUID()));
		} catch (IllegalArgumentException e) {
			System.out.println("\n!!메시지 조회 실패!!" + e.getMessage());
		}
	}
	private static void updateMessages(MessageService messageService, List<MessageResponse> messages, List<ChannelResponse> channels) {
		System.out.println("\n------------------------- UpdateMessage ------------------------");

		//수정할 메시지 original변수에 저장
		MessageResponse original = messages.get(0);
		System.out.println("\n메시지1 수정 전: \n" + messageService.find(original.id()));

		//변경 메시지 updated변수에 저장
		MessageUpdateRequest updateRequest = new MessageUpdateRequest(
				original.id(), "공지 수정합니다~~~~");

		//메시지 수정
		MessageResponse updated = messageService.update(updateRequest);
		System.out.println("\n메시지1 수정 후: \n" + updated);

		//수정후 전체 메시지
		System.out.println("\n["+ channels.get(0).channelName() +"]의 전체 메시지");
		messageService.findAllByChannelId(channels.get(0).channelId()).forEach(System.out::println);
	}
	private static void deleteMessages(MessageService messageService,  List<MessageResponse> messages, List<ChannelResponse> channels) {
		System.out.println("\n------------------------ DeleteMessage ------------------------");
		//메시지 삭제
		MessageResponse original = messages.get(3);
		UUID targetChannelId = channels.get(1).channelId();

		System.out.println("[sp02]채널의 메시지 조회");
		try {
			messageService.findAllByChannelId(targetChannelId).forEach(System.out::println);
		} catch (Exception e) {
			System.out.println("삭제 전 메시지 조회 실패: " + e.getMessage());
		}

		try{
			messageService.delete(original.id());
			System.out.println("\n.\n.\n<<[sp02]의 메시지 삭제 완료>>\n");
		} catch (IllegalArgumentException e) {
			System.out.println("\n!!메시지 삭제 실패!!" + e.getMessage());
		}

		//메시지 삭제 후 재조회
		System.out.print("삭제 후 전체 메시지: \n");
		for (ChannelResponse channel : channels) {
			String channelName = channel.channelName();

			try {
				if(channelName == null || channelName.isBlank()) {
					System.out.println("\nPrivate 채널입니다.");
				} else {
					System.out.println("\n[" + channel.channelName() + "]의 메시지");
					messageService.findAllByChannelId(channel.channelId()).forEach(System.out::println);
				}
			} catch (IllegalArgumentException | NoSuchElementException e) {
				System.out.println("메시지가 없거나 조회할 수 없습니다. " + e.getMessage());
			}
		}
	}
}

