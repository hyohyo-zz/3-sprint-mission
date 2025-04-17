package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.stream.Collectors;


public class FileChannelService implements ChannelService {
    private final String FILE_PATH = "src/main/java/com/sprint/mission/discodeit/channel.ser";
    private Map<UUID, Channel> data = loadData();

    //채널 생성
    @Override
    public void create(Channel channel) {
        data.put(channel.getId(), channel);
        saveData();
    }

    //채널 조회
    @Override
    public Channel read(UUID id) {
        return this.data.get(id);
    }

    //특정 채널 정보
    @Override
    public List<Channel> readByName(String channelName) {
        return data.values().stream()
                .filter(channel -> channel.getChannelName().contains(channelName))
                .collect(Collectors.toList());
    }

    //채널 전체 조회
    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    //채널 수정
    @Override
    public Channel update(UUID id, Channel update) {
        Channel channel = this.data.get(id);
        channel.update(update);
        return channel;
    }

    //채널 삭제
    @Override
    public boolean delete(UUID id, User user, String password) {
        return this.data.remove(id) != null;
    }


    //채널 멤버셋
    @Override
    public Set<User> members(UUID id) {
        Channel channel = data.get(id);
        return channel != null ? channel.getMembers() : Set.of();
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

}
