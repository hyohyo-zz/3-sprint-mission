package com.sprint.mission.discodeit.service.file;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserService implements UserService {
    private static final long serialVersionUID = 1L;
    private final String FILE_PATH = "src/main/java/com/sprint/mission/discodeit/user.ser";

    private Map<UUID, User> data = loadData();

    private final ChannelService channelService;

    public FileUserService(FileChannelService channelService) {
        this.channelService = channelService;
    }

    @Override
    public void create(User user) {
        data.put(user.getId(), user);
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
    public boolean delete(UUID id, String password) {
        return this.data.remove(id) != null;
    }

    //채널 전체에서 해당 유저 삭제
    public void removeUserFromChannels(User user) {
        for (Channel channel : channelService.readAll()) {
            Set<User> members = new HashSet<>(channel.getMembers());
            if (members.remove(user)) {
                Channel updatedChannel = new Channel(
                        channel.getChannelName(),
                        channel.getKeyUser(),
                        channel.getCategory(),
                        members
                );
                channelService.update(channel.getId(), updatedChannel);
            }
        }
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
    private Map<UUID, User> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

}
