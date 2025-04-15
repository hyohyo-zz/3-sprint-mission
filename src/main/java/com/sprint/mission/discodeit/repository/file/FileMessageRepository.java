package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private static final long serialVersionUID = 1L;
    private final String FILE_PATH = "src/main/java/com/sprint/mission/discodeit/message.ser";

    private final Map<UUID, Message> data = loadData();

    //메시지 생성
    @Override
    public void create(Message message) {
        Channel channel = message.getChannel();
        channel.validateMembership(message.getSender());
        channel.validateCategory(message.getCategory());

        message.validateContent();
        data.put(message.getId(), message);
        saveData();
    }

    //메시지 조회
    @Override
    public Message read(UUID id) {
        return this.data.get(id);
    }

    //메시지 전체조회
    @Override
    public List<Message> readAll() {
        return new ArrayList<>(this.data.values());
    }

    //메시지 수정
    @Override
    public Message update(UUID id, Message update) {
        Message selected = this.data.get(id);
        selected.update(update);
        return selected;
    }

    //메시지 삭제
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
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
    private Map<UUID, Message> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}
