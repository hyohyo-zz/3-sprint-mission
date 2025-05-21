package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.config.DiscodeitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

//초기화
@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final DiscodeitProperties properties;

    public void clearSerializedData() {
        String basePath = properties.getFilePath();
        List<String> fileNames = List.of(
                "channel.ser",
                "user.ser",
                "message.ser",
                "binarycontent.ser",
                "readstatus.ser",
                "userstatus.ser"
        );

        for (String fileName : fileNames) {
            try {
                File file = Path.of(basePath, fileName).toFile();

                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        System.out.println("초기화 완료: " + file.getName());
                    } else {
                        System.out.println("초기화 실패: " + file.getName());
                    }
                } else {
                    System.out.println("초기화할 파일 없음: " + file.getName());
                }
            } catch (Exception e) {
                System.out.println("!예외 발생!");
                System.out.println(" 예외 타입: " + e.getClass().getSimpleName());
                System.out.println(" 메시지: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}