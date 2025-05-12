package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.Response.MessageResponse;
import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
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
    public MessageResponse create(MessageCreateRequest request, List<BinaryContentCreateRequest> attachmentRequests) {

        User sender = userRepository.find(request.senderId()).orElseThrow(()-> new IllegalArgumentException(
                ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND))
        );
        Channel channel = channelRepository.find(request.channelId())
                .orElseThrow(()-> new IllegalArgumentException(
                        ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND))
                );

        List<UUID> attachmentIds = attachmentRequests.stream()
                .map(attachmentRequest -> {
                    byte[] bytes = attachmentRequest.bytes();
                    String contentType = attachmentRequest.contentType();
                    String fileName = attachmentRequest.originalFilename();

                    BinaryContent binaryContent = new BinaryContent(bytes, contentType, fileName);
                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    return createdBinaryContent.getId();
                })
                .toList();

        Message message = new Message(
                sender.getId(),
                channel.getId(),
                request.category(),
                request.content(),
                attachmentIds
        );

        channel.validateCategory(request.category());
        message.validateContent();

        return toMessageResponse(message);
    }

    @Override
    public MessageResponse find(UUID id) {
        Message message = messageRepository.find(id).orElseThrow(()-> new IllegalArgumentException(
                ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND)));

        User sender= userRepository.find(message.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND)));

        return toMessageResponse(message);
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        Channel channel = channelRepository.find(channelId).orElseThrow(()-> new IllegalArgumentException(
                ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND))
        );

        List<Message> messages = messageRepository.findAllByChannelId(channelId).stream()
                .filter(msg -> msg.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .toList();

        List<MessageResponse> responses = new ArrayList<>();

        for (Message message : messages) {
            User sender = userRepository.find(message.getSenderId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            ErrorMessages.format("Seder", ErrorMessages.ERROR_NOT_FOUND)
                    ));

            responses.add(toMessageResponse(message));
        }
        return responses;
    }

    @Override
    public MessageResponse update(UUID messageId, MessageUpdateRequest request) {
        String newContent = request.newContent();
        Message message = messageRepository.find(messageId).orElseThrow(()-> new IllegalArgumentException(
                ErrorMessages.format("Message", ErrorMessages.ERROR_NOT_FOUND)));

        message.update(newContent);
        return toMessageResponse(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.find(messageId).orElseThrow(()-> new IllegalArgumentException(
                ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND)));

        messageRepository.deleteById(messageId);
    }

    private MessageResponse toMessageResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSenderId(),
                message.getContent(),
                message.getCreatedAt(),
                message.getAttachmentIds()
        );
    }
}
