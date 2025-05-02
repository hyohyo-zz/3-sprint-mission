package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.DataInitializer.CHANNEL_FILE_PATH;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final String FILE_PATH = CHANNEL_FILE_PATH;

    private Map<UUID, Channel> data = loadData();

    //채널 생성
    @Override
    public void create(Channel channel) {
        data.put(channel.getId(), channel);
        saveData();
    }

    //채널 조회
    @Override
    public Channel find(UUID id) {
        return this.data.get(id);
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
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[메시지] 데이터 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[메시지] 저장된 파일이 없습니다. 새 데이터를 시작합니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[메시지] 데이터 불러오기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        // 실패 시 빈 Map 반환
        return new HashMap<>();
    }
}
