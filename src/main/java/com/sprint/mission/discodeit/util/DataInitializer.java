package com.sprint.mission.discodeit.util;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

//초기화
public class DataInitializer {
    public static final String CHANNEL_FILE_PATH = "src/main/java/com/sprint/mission/discodeit/channel.ser";
    public static final String USER_FILE_PATH = "src/main/java/com/sprint/mission/discodeit/user.ser";
    public static final String MESSAGE_FILE_PATH = "src/main/java/com/sprint/mission/discodeit/message.ser";
    public static final String BINARYCONTENT_FILE_PATH = "src/main/java/com/sprint/mission/discodeit/binarycontent.ser";


    private static final List<String> FILE_PATHS = List.of(
            CHANNEL_FILE_PATH,
            USER_FILE_PATH,
            MESSAGE_FILE_PATH,
            BINARYCONTENT_FILE_PATH
    );

    public static void clearSerializedData() {
        for (String path : FILE_PATHS) {
            try {
                File file = Path.of(path).toFile();

                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        System.out.println("초기화 완료 " + file.getName());
                    } else {
                        System.out.println("초기화 실패 " + file.getName());
                    }
                } else {
                    System.out.println("초기화할 파일이 존재하지 않음 " + file.getName());
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