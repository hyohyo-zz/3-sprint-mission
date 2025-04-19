package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private static JCFUserService instance;
    private final Map<UUID, User> data = new HashMap<>();
    private final ChannelService channelService;

    public JCFUserService(ChannelService channelService) {
        this.channelService = channelService;
    }

    public static JCFUserService getInstance(ChannelService channelService) {
        if (instance == null) {
            instance = new JCFUserService(channelService);
        }
        return instance;
    }

    //유저 생성
    @Override
    public void create(User user) {
        //이미 등록된 이메일 추가시
        for (User existingUser : data.values()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new IllegalArgumentException(" --- 이미 등록된 이메일입니다.");
            }
        }
        this.data.put(user.getId(), user);
    }

    //유저 아이디 조회
    @Override
    public User read(UUID id) {
        return this.data.get(id);
    }

    //유저 이름으로 조회
    @Override
    public List<User> readByName(String name) {
        List<User> result = data.values().stream()
                .filter(user -> user.getName().contains(name))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        return result;
    }

    //유저 전체 조회
    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    //유저 수정
    @Override
    public User update(UUID id, User update) {
        User user = this.data.get(id);

        if (user == null) {
            throw new IllegalArgumentException(" --해당 ID의 채널을 찾을 수 없습니다.");
        }

        user.update(update);
        return user;
    }

    //유저 삭제
    @Override
    public boolean delete(UUID id, String password) {
        User user = this.data.get(id);
        if (!user.getPassword().equals(password)) {
            System.out.println("!!유저 탈퇴 실패!! --- 비밀번호 불일치");
            return false;
        }
        System.out.println("<<유저 [" + user.getName() + "] 탈퇴 성공>>");
        boolean isDeleted = this.data.remove(id) != null;

        if (isDeleted){
            removeUserFromChannels(user);
        }
        return isDeleted;
    }

    //채널 전체에서 해당 유저 삭제
    public void removeUserFromChannels(User user) {
        for (Channel channel : channelService.readAll()) {
            Set<User> members = new HashSet<>(channel.getMembers());
            if (members.remove(user)) {
                Channel updatedChannel = new Channel(
                        channel.getChannelName(),
                        channel.getKeyUser(),
                        channel.getCategory(),
                        members
                );
                channelService.update(channel.getId(), updatedChannel);
            }
        }
    }

}