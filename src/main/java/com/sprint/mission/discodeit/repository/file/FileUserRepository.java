package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

import static com.sprint.mission.discodeit.util.DataInitializer.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserRepository implements UserRepository {
    private static final long serialVersionUID = 1L;

    private final String FILE_PATH = USER_FILE_PATH;

    private ChannelRepository channelRepository;

    private Map<UUID, User> data = loadData();

    public FileUserRepository(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void create(User user) {
        this.data.put(user.getId(), user);
        saveData();
    }

    //유저 아이디 조회
    @Override
    public User read(UUID id) {
        return this.data.get(id);
    }

    //유저 이름으로 조회
    @Override
    public List<User> readByName(String name) {
        return data.values().stream()
                .filter(user -> user.getName().contains(name))
                .collect(Collectors.toList());
    }

    //유저 전체 조회
    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    //유저 수정
    @Override
    public User update(UUID id, User update) {
        User user = this.data.get(id);
        user.update(update);
        return user;
    }

    //유저 삭제
    @Override
    public boolean delete(UUID id) {
        return this.data.remove(id) != null;
    }

    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[유저] 데이터 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[유저] 저장된 파일이 없습니다. 새 데이터를 시작합니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[유저] 데이터 불러오기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        // 실패 시 빈 Map 반환
        return new HashMap<>();
    }

}
