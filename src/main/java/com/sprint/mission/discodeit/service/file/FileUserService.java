//package com.sprint.mission.discodeit.service.file;
//
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static com.sprint.mission.discodeit.util.DataInitializer.*;
//
//public class FileUserService implements UserService {
//    private static final long serialVersionUID = 1L;
//    private final String FILE_PATH = USER_FILE_PATH;
//
//    private Map<UUID, User> data = loadData();
//
//    private final ChannelService channelService;
//
//    public FileUserService(ChannelService channelService) {
//        this.channelService = channelService;
//    }
//
//    @Override
//    public void create(User user) {
//        for (User existingUser : data.values()) {
//            if (existingUser.getEmail().equals(user.getEmail())) {
//                throw new IllegalArgumentException(" --- 이미 등록된 이메일입니다.");
//            }
//        }
//        this.data.put(user.getId(), user);
//
//        saveData();
//    }
//
//    //유저 아이디 조회
//    @Override
//    public User find(UUID id) {
//        return this.data.get(id);
//    }
//
//    //유저 이름으로 조회
//    @Override
//    public List<User> findByName(String name) {
//        List<User> result = data.values().stream()
//                .filter(user -> user.getName().contains(name))
//                .collect(Collectors.toList());
//
//        if (result.isEmpty()) {
//            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
//        }
//        return result;
//    }
//
//    //유저 전체 조회
//    @Override
//    public List<User> findAll() {
//        return new ArrayList<>(data.values());
//    }
//
//    //유저 수정
//    @Override
//    public User update(UUID id, User update) {
//        User user = this.data.get(id);
//
//        if (user == null) {
//            throw new IllegalArgumentException(" --해당 ID의 채널을 찾을 수 없습니다.");
//        }
//
//        user.update(update);
//        return user;
//    }
//
//    //유저 삭제
//    @Override
//    public boolean delete(UUID id, String password) {
//        User user = this.data.get(id);
//        if (!user.getPassword().equals(password)) {
//            System.out.println("!!유저 탈퇴 실패!! --- 비밀번호 불일치");
//            return false;
//        }
//        System.out.println("<<유저 [" + user.getName() + "] 탈퇴 성공>>");
//        boolean isDeleted = this.data.remove(id) != null;
//
//        if (isDeleted){
//            removeUserFromChannels(user);
//        }
//        return isDeleted;
//    }
//
//    //채널 전체에서 해당 유저 삭제
//    public void removeUserFromChannels(User user) {
//        for (Channel channel : channelService.findAllByUserId()) {
//            Set<User> members = new HashSet<>(channel.getMembers());
//            if (members.remove(user)) {
//                Channel updatedChannel = new Channel(
//                        channel.getChannelName(),
//                        channel.getKeyUser(),
//                        channel.getCategory(),
//                        members
//                );
//                channelService.update(channel.getId(), updatedChannel);
//            }
//        }
//    }
//
//    private void saveData() {
//        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//            oos.writeObject(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 불러오기 메서드
//    @SuppressWarnings("unchecked")
//    private Map<UUID, User> loadData() {
//        try (FileInputStream fis = new FileInputStream(FILE_PATH);
//             ObjectInputStream ois = new ObjectInputStream(fis)) {
//            return (Map<UUID, User>) ois.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new HashMap<>();
//        }
//    }
//
//}
