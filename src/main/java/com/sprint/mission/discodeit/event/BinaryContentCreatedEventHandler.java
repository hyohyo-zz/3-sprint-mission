package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class BinaryContentCreatedEventHandler {

    private final BinaryContentStorage storage;
    private final BinaryContentRepository binaryContentRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(BinaryContentCreatedEvent event) {
        BinaryContentDto meta = event.meta();
        UUID id = meta.id();
        String key = event.objectKey();

        log.info("[BinaryEvent] 업로드 시작 - id={}, key={}, size={}, type={}", id, key, meta.size(), meta.contentType());

        try(InputStream in = event.inputStreamSupplier().get()) {
            storage.put(key, in, meta.size(), meta.contentType());

            BinaryContent binaryContent = binaryContentRepository.findById(meta.id())
                .orElseThrow(() -> new IllegalStateException("not found: " + meta.id()));
            binaryContent.updateStatus(BinaryContentStatus.SUCCESS);

            log.info("[BinaryContent] 업로드 성공 - id={}, key={}", id, key);
        } catch(Exception e) {
            BinaryContent binaryContent = binaryContentRepository.findById(meta.id())
                .orElse(null);
            if (binaryContent != null) binaryContent.updateStatus(BinaryContentStatus.FAIL);

            log.error("[BinaryContent] 업로드 실패 - id={}, key={}, ex={}", meta.id(), key, e.toString());
        }
    }

}
