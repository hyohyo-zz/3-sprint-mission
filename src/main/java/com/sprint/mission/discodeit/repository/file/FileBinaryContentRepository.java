package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.DiscodeitProperties;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PostConstruct;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class FileBinaryContentRepository implements BinaryContentRepository {
    private static final long serialVersionUID = 1L;

    private final String filePath;
    private Map<UUID, BinaryContent> data;

    public FileBinaryContentRepository(DiscodeitProperties properties) {
        this.filePath = properties.getFilePath() + "/binarycontent.ser";
        this.data = loadData();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        BinaryContent content = new BinaryContent(
                binaryContent.getUserId(),
                binaryContent.getMessageId(),
                binaryContent.getContent(),
                binaryContent.getContentType(),
                binaryContent.getOriginalFilename()
        );
        data.put(content.getId(), content);  // ID 기준으로 저장
        saveData();  // 파일 저장 등

        return content;
    }

    @Override
    public BinaryContent find(UUID id) {
        return this.data.get(id);
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<BinaryContent> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(file -> Objects.equals(file.getUserId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(UUID id) {
        return this.data.remove(id) != null;
    }

    @Override
    public boolean deleteByUserId(UUID userId) {
        List<UUID> toRemove = data.values().stream()
                .filter(file -> Objects.equals(file.getUserId(), userId))
                .map(BinaryContent::getId)
                .toList();

        boolean deleted = false;
        for(UUID id : toRemove) {
            deleted |= data.remove(id) != null;
        }
        saveData();
        return deleted;
    }

    @Override
    public boolean deleteByMessageId(UUID messageId) {
        List<UUID> toRemove = data.values().stream()
                .filter(file -> Objects.equals(file.getMessageId(), messageId))
                .map(BinaryContent::getId)
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
            System.err.println("[binaryContent] 데이터 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, BinaryContent> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, BinaryContent>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[binaryContent] 저장된 파일이 없습니다. 새 데이터를 시작합니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[binaryCotent] 데이터 불러오기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        // 실패 시 빈 Map 반환
        return new HashMap<>();
    }
}
