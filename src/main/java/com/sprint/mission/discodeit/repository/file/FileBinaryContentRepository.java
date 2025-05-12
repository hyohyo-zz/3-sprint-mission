package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.config.DiscodeitProperties;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final String filePath;
    private Map<UUID, BinaryContent> data;

    public FileBinaryContentRepository(DiscodeitProperties properties) {
        this.filePath = properties.getFilePath() + "/binarycontent.ser";
        this.data = loadData();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        BinaryContent content = new BinaryContent(
                binaryContent.getBytes(),
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
    public List<BinaryContent> findAllByIdIn() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean delete(UUID id) {
        return this.data.remove(id) != null;
    }

    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println(ErrorMessages.format("[binaryContent]", ErrorMessages.ERROR_SAVE));
            e.printStackTrace();
        }
    }

    // 불러오기 메서드
    @SuppressWarnings("unchecked")
    private Map<UUID, BinaryContent> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, BinaryContent>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println(ErrorMessages.format("[binaryContent]", ErrorMessages.ERROR_NOT_FOUND));
            System.out.println("새 데이터를 시작합니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(ErrorMessages.format("[binaryContent]", ErrorMessages.ERROR_LOAD));
            e.printStackTrace();
        }
        // 실패 시 빈 Map 반환
        return new HashMap<>();
    }
}
