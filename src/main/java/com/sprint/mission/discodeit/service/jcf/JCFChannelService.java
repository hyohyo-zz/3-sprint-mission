package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;


import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    //채널 생성
    @Override
    public void create(Channel channel) {

        //중복 채널명 검증
        for (Channel existingChannel : data.values()) {
            if (existingChannel.getChannelName().equals(channel.getChannelName())
            && existingChannel.getKeyUser().equals(channel.getKeyUser())) {

                throw new IllegalArgumentException(" --- "+ channel.getKeyUser().getName()+"님! 이미 등록된 채널입니다.");
            }
        }

        //중복 카테고리 검증
        Set<String> categorySet = new HashSet<>(channel.getCategory());
        if (channel.getCategory().size() != categorySet.size()) {      //list사이즈=2, set사이즈1 -> 중복있다
            throw new IllegalArgumentException(" --- 중복된 카테고리가 포함되어 있습니다.");
        }
        channel.getMembers().add(channel.getKeyUser()); // 키 유저를 초기 멤버로 추가

        data.put(channel.getId(), channel);

    }

    //채널 조회
    @Override
    public Channel read(UUID id) {
        return this.data.get(id);
    }

    //채널 전체 조회
    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    //채널 수정
    @Override
    public Channel update(UUID id, Channel update) {
        Channel selected = this.data.get(id);
        selected.update(update);
        return selected;
    }

    //채널 삭제
    @Override
    public boolean delete(UUID id, User user, String password) {
        Channel channel = this.data.get(id);
        if (!user.getPassword().equals(password)) {
            System.out.println("!!채널 삭제 실패!! --- 비밀번호 불일치");
            return false;
        }
        System.out.println("<<채널 [" + channel.getChannelName() + "] 삭제 성공>>");
        return this.data.remove(id) != null;
    }


    //채널 멤버셋
    @Override
    public Set<User> members(UUID id) {
        Channel channel = data.get(id);
        return channel != null ? channel.getMembers() : Set.of();
    }

    //채널별 카테고리
    public Map<String, List<List<String>>> groupByChannel() {
        return data.values().stream()
                .collect(Collectors.groupingBy(
                        Channel::getChannelName,
                        Collectors.mapping(Channel::getCategory,
                                Collectors.toList())));
    }

    //특정 채널 정보
    public List<Channel> findChannel(String channelName) {
        return data.values().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .collect(Collectors.toList());
    }

}
