package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;


import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel createChannel(Channel channel) {
        data.put(channel.getChannelId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> Channels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel updateChannel(UUID id, Channel updatedChannel) {
        if (!data.containsKey(id)) {
            return null;
        }
        Channel existing = data.get(id);
        existing.setChannelName(updatedChannel.getChannelName());
        existing.setCategory(updatedChannel.getCategory());
        existing.setMembers(updatedChannel.getMembers());
        existing.setUpdatedAt(System.currentTimeMillis());
        return existing;
    }

    @Override
    public boolean deleteChannel(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public Set<User> getChannelMembers(UUID id) {
        Channel channel = data.get(id);
        return channel != null ? channel.getMembers() : Set.of();
    }


    public Map<String, Integer> countUserByChannel() {
        return data.values().stream()
                .collect(Collectors.toMap(
                        Channel::getChannelName,
                        ch -> ch.getMembers().size()
                ));
    }

    //채널에 성별로 그룹화
    public Map<String, Map<String, List<User>>> groupByGenderAndChannel() {
        Map<String, Map<String, List<User>>> result = new HashMap<>();

        for (Channel channel : data.values()) {
            System.out.println("[" + channel.getChannelName() + "]");
            String channelName = channel.getChannelName();
            Set<User> members = channel.getMembers();

            Map<String, List<User>> genderMap = members.stream().collect(Collectors.groupingBy(User::getGender));
            genderMap.forEach((gender, users) ->
                System.out.println("   - " + gender + ": " + users.size() + "명"));

            result.put(channelName, genderMap);

        }
        return result;
    }

    public Map<String, List<User>> groupBy_sp01_male() {
        List<User> maleUsers = data.values().stream()
                .filter(c -> c.getChannelName().equals("sp01"))
                .flatMap(c -> c.getMembers().stream())
                .filter(u -> u.getGender().equals("남"))
                .toList();

        Map<String,List<User>> result = new HashMap<>();
        result.put("남", maleUsers);

        maleUsers.forEach(u->System.out.print(u.getName() + " "));
        return result;
    }

    //user4 멤버에서 지우기
    public void deleteMember(User targetUser) {
        data.values().forEach(channel ->
                channel.getMembers().removeIf(user -> user.getId().equals(targetUser.getId()))
        );
    }

    //채널별 카테고리
    public void channelandcategory() {
        for (Channel channel : data.values()) {
            System.out.print("\n["+ channel.getChannelName() + "]채널: ");

            List<String> cat = channel.getCategory();
            if (cat.isEmpty()) System.out.print("카테고리 없음");
            else cat.forEach(c -> System.out.print("(" + c + ") "));
        }
    }



}
