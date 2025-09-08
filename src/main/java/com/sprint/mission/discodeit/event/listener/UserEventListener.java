package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.event.EventType;
import com.sprint.mission.discodeit.event.UserEvent;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.SseService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListener {

    private final SseService sseService;
    private final UserMapper userMapper;

    @TransactionalEventListener
    public void on(UserEvent event) {
        String eventName = switch (event.getType()) {
            case CREATED -> "users.created";
            case UPDATED -> "users.updated";
            case DELETED -> "users.deleted";
        };

        Object payload = (event.getType() == EventType.DELETED)
            ? Map.of("id", event.getId())
            : userMapper.toDto(event.getEntity());

        sseService.broadcast(eventName, payload);
        log.info("[SSE] {} 이벤트 발행: payload={}", eventName, payload);
    }

}
