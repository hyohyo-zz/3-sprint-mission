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
        //이미 생성된 채널 추가시
        for (Channel existingChannel : data.values()) {
            if (existingChannel.getChannelName().equals(channel.getChannelName())) {
                throw new IllegalArgumentException(" --- 이미 등록된 채널입니다.");
            }
        }

        List<String> categoryList = channel.getCategory();
        Set<String> categorySet = new HashSet<>(categoryList);
        if (categoryList.size() != categorySet.size()) {    //list사이즈=2, set사이즈1 -> 중복있다
            throw new IllegalArgumentException(" --- 중복된 카테고리가 포함되어 있습니다.");
        }

        this.data.put(channel.getId(),channel);
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
    public boolean delete(UUID id) {
        return data.remove(id) != null;
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

    //멤버에서 유저삭제
    public void removeMember(User user){
        for (Channel channel : data.values()) {
            Set<User> members = new HashSet<>(channel.getMembers());

            if (members.remove(user)) {
                Channel updated = new Channel(channel.getChannelName(), channel.getCategory(), members);
                this.data.put(channel.getId(), updated);
            }
        }
    }
}
