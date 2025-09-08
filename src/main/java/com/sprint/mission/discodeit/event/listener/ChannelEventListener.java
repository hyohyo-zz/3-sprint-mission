package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.event.ChannelEvent;
import com.sprint.mission.discodeit.event.EventType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.SseService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChannelEventListener {

    private final SseService sseService;
    private final ChannelMapper channelMapper;

    @TransactionalEventListener
    public void on(ChannelEvent event) {
        String eventName = switch (event.getType()) {
            case CREATED -> "channels.created";
            case UPDATED -> "channels.updated";
            case DELETED -> "channels.deleted";
        };

        Object payload;
        if (event.getType() == EventType.DELETED) {
            payload = Map.of("id", event.getId());
        } else {
            payload = channelMapper.toDto(event.getEntity());
        }

        sseService.broadcast(eventName, payload);
        log.info("[SSE] {} 이벤트 발행: payload={}", eventName, payload);
    }

}
