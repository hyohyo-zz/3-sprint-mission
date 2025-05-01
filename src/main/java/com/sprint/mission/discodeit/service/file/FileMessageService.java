package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import static com.sprint.mission.discodeit.util.DataInitializer.*;

public class FileMessageService implements MessageService {
    private static final long serialVersionUID = 1L;
    private final String FILE_PATH = MESSAGE_FILE_PATH;

    private final Map<UUID, Message> data = loadData();

    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    //메시지 생성
    @Override
    public void create(Message message) {
        data.put(message.getId(), message);
        saveData();
    }

    //메시지 조회
    @Override
    public Message find(UUID id) {
        Message message = this.data.get(id);

        //메시지id 존재하지 않음
        if (message == null) {
            throw new IllegalArgumentException(" --해당 ID의 메시지를 찾을 수 없습니다.");
        }
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
        selected.update(update);
        return selected;
    }

    //메시지 삭제
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
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
    private Map<UUID, Message> loadData() {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
