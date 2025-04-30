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

import static com.sprint.mission.discodeit.util.DataInitializer.*;

public class FileChannelService implements ChannelService {
    private final String FILE_PATH = CHANNEL_FILE_PATH;
    private Map<UUID, Channel> data = loadData();

    //채널 생성
    @Override
    public void create(Channel channel) {
        validateDuplicateChannelName(channel);
        channel.validateUniqueCategory();
        channel.addKeyUserToMembers();

        data.put(channel.getId(), channel);
        saveData();
    }

    //채널 조회
    @Override
    public Channel find(UUID id) {
        Channel channel = this.data.get(id);

        if (channel == null) {  //아무 객체도 가리키지 않음
            throw new IllegalArgumentException(" --해당 ID의 채널을 찾을 수 없습니다.");
        }

        return channel;
    }

    //특정 채널 정보
    @Override
    public List<Channel> findByChannelName(String channelName) {
        List<Channel> result = data.values().stream()
                .filter(channel -> channel.getChannelName().contains(channelName))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        return result;
    }

    //채널 전체 조회
    @Override
    public List<Channel> findAllByUserId() {
        List<Channel> channels = new ArrayList<>(data.values());

        if (channels.isEmpty()) {   //리스트있지만 요소없음
            System.out.println(" --조회 가능한 채널이 없습니다.");
        }

        return channels;
    }

    //채널 수정
    @Override
    public Channel update(UUID id, Channel update) {
        Channel channel = this.data.get(id);

        if (channel == null) {
            throw new IllegalArgumentException(" --해당 ID의 채널을 찾을 수 없습니다.");
        }

        channel.update(update);
        return channel;
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

    //중복 채널명 검증
    private void validateDuplicateChannelName(Channel channel) {
        for (Channel existingChannel : data.values()) {
            if (existingChannel.getChannelName().equals(channel.getChannelName())
                    && existingChannel.getKeyUser().equals(channel.getKeyUser())) {
                throw new IllegalArgumentException(" --- "+ channel.getKeyUser().getName()+"님! 이미 등록된 채널입니다.");
            }
        }
    }

    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadData() {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
               ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

}
