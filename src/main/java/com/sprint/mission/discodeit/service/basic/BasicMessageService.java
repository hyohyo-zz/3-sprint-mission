package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.Response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.Response.MessageResponse;
import com.sprint.mission.discodeit.dto.request.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.request.create.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.MessageUpdateRequest;
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

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponse create(MessageCreateRequest request, Optional<BinaryContentRequest> attachmentCreateRequest) {

        User sender = userRepository.find(request.senderId()).orElseThrow(()-> new IllegalArgumentException(
                ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND))
        );
        Channel channel = channelRepository.find(request.channelId())
                .orElseThrow(()-> new IllegalArgumentException(
                        ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND))
                );

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
                        null,
                        savedMessage.getId(),
                        file.content(),
                        file.contentType(),
                        file.originalFilename()
                );
                savedAttachments.add(binaryContentRepository.save(attachment));
            }
        }

        return toMessageResponse(savedMessage, savedAttachments, sender.getName());
    }

    @Override
    public MessageResponse find(UUID id) {
        Message message = messageRepository.find(id);
        if (message == null) {
            System.out.println(ErrorMessages.format("Message", ErrorMessages.ERROR_NOT_FOUND));
        }

        User sender= userRepository.find(message.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND)));

        List<BinaryContent> attachmemts = binaryContentRepository.findAll().stream()
                .filter(file -> file.getMessageId().equals(message.getId()))
                .toList();

        return toMessageResponse(message, attachmemts, sender.getName());
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        Channel channel = channelRepository.find(channelId).orElseThrow(()-> new IllegalArgumentException(
                ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND))
        );

        List<Message> messages = messageRepository.findAll().stream()
                .filter(msg -> msg.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .toList();

        List<MessageResponse> responses = new ArrayList<>();

        for (Message message : messages) {
            User sender = userRepository.find(message.getSenderId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            ErrorMessages.format("Seder", ErrorMessages.ERROR_NOT_FOUND)
                    ));

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
            throw new IllegalArgumentException(
                    ErrorMessages.format("Message", ErrorMessages.ERROR_NOT_FOUND));
        }

        message.update(request.newContent());

        Message updatedMessage = messageRepository.update(request.messageId(), message);

        User sender = userRepository.find(updatedMessage.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.format("Sender", ErrorMessages.ERROR_NOT_FOUND)
                ));

        List<BinaryContent> attachments = binaryContentRepository.findAll().stream()
                .filter(file -> file.getMessageId().equals(updatedMessage.getId()))
                .toList();

        return toMessageResponse(updatedMessage, attachments, sender.getName());
    }

    @Override
    public boolean delete(UUID messageId) {
        Message message = messageRepository.find(messageId);
        if (message == null) {
            throw new IllegalArgumentException(
                    ErrorMessages.format("Message", ErrorMessages.ERROR_NOT_FOUND));
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
