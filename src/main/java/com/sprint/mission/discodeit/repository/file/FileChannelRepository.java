package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.config.DiscodeitProperties;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import jakarta.annotation.PostConstruct;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class FileChannelRepository implements ChannelRepository {
    private final String filePath;
    private Map<UUID, Channel> data;

    public FileChannelRepository(DiscodeitProperties properties) {
        if (properties.getFilePath() == null) {
            System.out.println(ErrorMessages.format("[Channel]", ErrorMessages.ERROR_FILE_PATH_NULL));
        }
        this.filePath = properties.getFilePath() + "/channel.ser";
        this.data = new HashMap<>();
    }

    // 파일 있으면 불러오기
    @PostConstruct
    public void init() {
        this.data = loadData();
    }
    //채널 생성
    @Override
    public Channel create(Channel channel) {
        data.put(channel.getId(), channel);
        saveData();
        return channel;
    }

    //채널 조회
    @Override
    public Optional<Channel> find(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    //채널 전체 조회
    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    //특정 채널 정보
    public List<Channel> findByChannelName(String channelName) {
        List<Channel> result = data.values().stream()
                .filter(channel -> channel.getChannelName().contains(channelName))
                .collect(Collectors.toList());
        return result;
    }

    //채널 수정
    @Override
    public Channel update(UUID id, Channel update) {
        Channel channel = this.data.get(id);
        channel.update(update);
        saveData();
        return channel;
    }

    //채널 삭제
    @Override
    public boolean delete(UUID id, UUID userId, String password) {
        return this.data.remove(id) != null;
    }

    //채널 멤버셋
    @Override
    public Set<User> members(UUID id) {
        Channel channel = data.get(id);
        return channel != null ? channel.getMembers() : Set.of();
    }

    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println(ErrorMessages.format("[Channel]", ErrorMessages.ERROR_SAVE));
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println(ErrorMessages.format("[Channel]", ErrorMessages.ERROR_NOT_FOUND));
            System.out.println("새 데이터를 시작합니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(ErrorMessages.format("[Channel]", ErrorMessages.ERROR_LOAD));
            e.printStackTrace();
        }
        // 실패 시 빈 Map 반환
        return new HashMap<>();
    }
}
