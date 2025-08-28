package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Cacheable(value = "notificationsByUser", key = "#receiverId")
    @Transactional(readOnly = true)
    @Override
    public List<NotificationDto> findMyNotifications(UUID receiverId) {
        log.info("[notification] findMyNotifications (cacheable) - receiverId={}", receiverId);

        List<NotificationDto> result = notificationRepository.findByReceiver_IdOrderByCreatedAtDesc(
                receiverId)
            .stream()
            .map(notificationMapper::toDto)
            .collect(Collectors.toList());

        log.info("[notification] 응답(DB 쿼리 실행됨): size={}", result.size());
        return result;
    }

    @CacheEvict(value = "notificationsByUser", key = "#notificationId")
    @Override
    public void deleteMyNotification(UUID receiverId, UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        if (!notification.getReceiver().getId().equals(receiverId)) {
            throw new SecurityException("본인의 알림만 삭제할 수 있습니다.");
        }

        notificationRepository.delete(notification);
    }
}
