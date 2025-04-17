package com.sprint.mission.discodeit.util;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

//초기화
public class DataInitializer {
    private static final List<String> FILE_PATHS = List.of(
            "src/main/java/com/sprint/mission/discodeit/user.ser",
            "src/main/java/com/sprint/mission/discodeit/channel.ser",
            "src/main/java/com/sprint/mission/discodeit/message.ser"
    );

    public static void clearSerializedData() {
        for (String path : FILE_PATHS) {
            File file = Path.of(path).toFile();
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("초기화 완료 " + file.getName());
                } else {
                    System.out.println("초기화 실패 " + file.getName());
                }
            }
        }
    }
}