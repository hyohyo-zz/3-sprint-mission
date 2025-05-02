package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.*;

import static com.sprint.mission.discodeit.util.DataInitializer.MESSAGE_FILE_PATH;

@Repository
public class FileMessageRepository implements MessageRepository {
    private static final long serialVersionUID = 1L;

    private final String FILE_PATH = MESSAGE_FILE_PATH;

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    private final Map<UUID, Message> data = loadData();

    public FileMessageRepository(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
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
    private Map<UUID, Message> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
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
