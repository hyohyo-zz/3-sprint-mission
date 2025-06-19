package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;

    @Override
    @Transactional
    public MessageDto create(MessageCreateRequest request,
        List<BinaryContentCreateRequest> attachmentRequests) {
        UUID authorId = request.authorId();
        UUID channelId = request.channelId();
        log.info("[message] 생성 요청: authorId={}, channelId={}", authorId, channelId);

        User author = userRepository.findById(authorId)
            .orElseThrow(() -> {
                log.warn("[message] 생성 실패 - 존재하지 않는 userId: userId={}", authorId);
                return new NoSuchElementException(
                    ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND));
            });

        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> {
                log.warn("[message] 생성 실패 - 존재하지 않는 channelId: channelId={}", channelId);
                return new NoSuchElementException(
                    ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND));
            });

        List<BinaryContent> attachments = attachmentRequests == null ? List.of() :
            attachmentRequests.stream()
                .map(req -> {
                    BinaryContent binaryContent = new BinaryContent(req.fileName(),
                        (long) req.bytes().length,
                        req.contentType());
                    BinaryContent savedAttachment = binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(savedAttachment.getId(), req.bytes());
                    return savedAttachment;
                })
                .toList();
        log.debug("[message] 첨부파일 요청 수: {}",
            attachmentRequests != null ? attachmentRequests.size() : 0);

        Message message = new Message(
            request.content(),
            channel,
            author,
            attachments
        );

        message.validateContent(attachments);
        messageRepository.save(message);
        log.info("[message] 생성 완료: messageId={}, authorId={}, channelId={}",
            message.getId(), authorId, channelId);

        return messageMapper.toDto(message);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageDto find(UUID id) {
        log.info("[message] 조회 요청: id={}", id);

        return messageRepository.findById(id)
            .map(messageMapper::toDto)
            .orElseThrow(() -> {
                log.warn("[message] 조회 실패 - 존재하지 않는 id: id={}", id);
                return new NoSuchElementException(
                    ErrorMessages.format("Message", ErrorMessages.ERROR_NOT_FOUND));
            });
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
        Pageable pageable) {
        Slice<MessageDto> slice = messageRepository.findByChannelIdAndCreatedAtLessThan(channelId,
                Optional.ofNullable(createAt).orElse(Instant.now()), pageable)
            .map(messageMapper::toDto);

        log.info("[message] 전체 조회 요청: channelId={}, size={}", channelId, slice.getContent().size());
        log.debug("[message] Slice 정보: {}", slice);

        Instant nextCursor = null;
        if (slice.hasContent()) {
            nextCursor = slice.getContent().get(slice.getContent().size() - 1)
                .createdAt();
        }

        log.info("[message] 전체 조회 응답: channelId={}, 결과 개수={}, nextCursor={}",
            channelId, slice.getContent().size(), nextCursor);
        return pageResponseMapper.fromSlice(slice, nextCursor);
    }

    @Override
    @Transactional
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        String newContent = request.newContent();
        log.info("[message] 수정 요청: messageId={}, newContent={}", messageId, request.newContent());

        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> {
                log.warn("[message] 수정 실패 - 존재하지 않는 id: id={}", messageId);
                return new NoSuchElementException(
                    ErrorMessages.format("Message", ErrorMessages.ERROR_NOT_FOUND));
            });

        message.update(newContent);
        log.info("[message] 수정 완료: messageId={}, newContent={}", messageId, request.newContent());

        return messageMapper.toDto(message);
    }

    @Transactional
    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            log.warn("[message] 삭제 실패 - 존재하지 않는 id: id={}", messageId);
            throw new NoSuchElementException(
                ErrorMessages.format("Message", ErrorMessages.ERROR_NOT_FOUND));
        }

        messageRepository.deleteById(messageId);
        log.info("[message] 삭제 완료: id={}", messageId);
    }
}
