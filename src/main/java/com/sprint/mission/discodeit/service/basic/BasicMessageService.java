package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponse create(MessageCreateRequest request) {

        User sender = userRepository.find(request.senderId()).orElseThrow();
        Channel channel = channelRepository.find(request.channelId()).orElseThrow();

        if(channel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널");
        }

        Message message = new Message(
                sender.getId(),
                channel.getId(),
                request.category(),
                request.content()
        );

        channel.validateCategory(request.category());
        channel.validateMembership(sender);
        message.validateContent();

        Message savedMessage = messageRepository.create(message);

        List<BinaryContent> savedAttachments = new ArrayList<>();
        if(request.attachments() != null && !request.attachments().isEmpty()) {
            for(BinaryContentRequest file : request.attachments()) {
                BinaryContent attachment = new BinaryContent(
                        savedMessage.getId(),
                        file.content(),
                        file.contentType(),
                        file.originalFilename(),
                        "/files/" + file.originalFilename(), // 또는 URL 생성기 따로
                        Instant.now()
                );
                savedAttachments.add(binaryContentRepository.save(savedMessage.getId(), attachment));
            }
        }

        return toMessageResponse(savedMessage, savedAttachments, sender.getName());
    }

    @Override
    public MessageResponse find(UUID id) {
        Message message = messageRepository.find(id);
        if (message == null) {
            throw new IllegalArgumentException(" --해당 메시지를 찾을 수 없습니다.");
        }

        User sender= userRepository.find(message.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        List<BinaryContent> attachmemts = binaryContentRepository.findAll().stream()
                .filter(file -> file.getMessageId().equals(message.getId()))
                .toList();

        return toMessageResponse(message, attachmemts, sender.getName());
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        Channel channel = channelRepository.find(channelId).orElseThrow();
        if(channel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널");
        }

        List<Message> messages = messageRepository.findAll().stream()
                .filter(msg -> msg.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .toList();

        List<MessageResponse> responses = new ArrayList<>();

        for (Message message : messages) {
            User sender = userRepository.find(message.getSenderId())
                    .orElseThrow(() -> new IllegalArgumentException(("작성자를 찾을 수 없습니다.")));

            List<BinaryContent> attachments = binaryContentRepository.findAll().stream()
                    .filter(file -> file.getMessageId().equals(message.getId()))
                    .toList();

            responses.add(toMessageResponse(message, attachments, sender.getName()));
        }
        return responses;
    }

    @Override
    public MessageResponse update(MessageUpdateRequest request) {
        Message message = messageRepository.find(request.messageId());
        if(message == null) {
            throw new IllegalArgumentException("해당 메시지를 찾을 수 없습니다.");
        }

        message.update(request.newContent());

        Message updatedMessage = messageRepository.update(request.messageId(), message);

        User sender = userRepository.find(updatedMessage.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("작성자 조회 실패"));

        List<BinaryContent> attachments = binaryContentRepository.findAll().stream()
                .filter(file -> file.getMessageId().equals(updatedMessage.getId()))
                .toList();

        return toMessageResponse(updatedMessage, attachments, sender.getName());
    }

    @Override
    public boolean delete(UUID messageId) {
        Message message = messageRepository.find(messageId);
        if (message == null) {
            throw new IllegalArgumentException(" --해당 메시지를 찾을 수 없습니다.");
        }

        //첨부파일 삭제
        binaryContentRepository.deleteByMessageId(messageId);

        //메시지 삭제
        return messageRepository.delete(messageId);
    }

    private MessageResponse toMessageResponse(Message message, List<BinaryContent> attachments, String senderName) {
        List<BinaryContentResponse> attachmentsDtos = attachments.stream()
                .map(file -> new BinaryContentResponse(
                        file.getId(),
                        file.getContentType(),
                        file.getOriginalFilename(),
                        file.getUrl()
                )).toList();

        return new MessageResponse(
                message.getId(),
                message.getContent(),
                senderName,
                message.getCreatedAt(),
                attachmentsDtos
        );
    }
}
