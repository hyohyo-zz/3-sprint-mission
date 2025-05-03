package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.DiscodeitProperties;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;

import java.io.*;
import java.time.Instant;
import java.util.*;


public class FileMessageRepository implements MessageRepository {
    private static final long serialVersionUID = 1L;

    private final String filePath;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private Map<UUID, Message> data;

    public FileMessageRepository(DiscodeitProperties properties, UserRepository userRepository, ChannelRepository channelRepository) {
        if (properties.getFilePath() == null) {
            throw new IllegalStateException("filePath 설정이 null입니다. application.yaml 설정 확인 필요");
        }
        this.filePath = properties.getFilePath() + "/message.ser";
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.data = new HashMap<>();
    }

    // 파일 있으면 불러오기
    @PostConstruct
    public void init() {
        this.data = loadData();
    }
    //메시지 생성
    @Override
    public Message create(Message message) {
        data.put(message.getId(), message);
        saveData();
        return message;
    }

    //메시지 조회
    @Override
    public Message find(UUID id) {
        return this.data.get(id);
    }

    //메시지 전체조회
    @Override
    public List<Message> findAll() {
        return new ArrayList<>(this.data.values());
    }

    //메시지 수정
    @Override
    public Message update(UUID id, Message update) {
        Message selected = this.data.get(id);
        selected.update(update.getContent());
        saveData();
        return selected;
    }

    //메시지 삭제
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public Optional<Instant> findLastMessageTimeByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(msg -> Objects.equals(msg.getChannelId(), channelId))
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder());
    }

    @Override
    public boolean deleteByChannelId(UUID channelId) {
        List<UUID> toRemove = data.values().stream()
                .filter(message -> Objects.equals(message.getChannelId(), channelId))
                .map(Message::getId)
                .toList();

        boolean deleted = false;
        for (UUID id : toRemove) {
            deleted |= data.remove(id) != null;
        }

        saveData();
        return deleted;
    }

    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[메시지] 데이터 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, Message> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, Message>) ois.readObject();
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
