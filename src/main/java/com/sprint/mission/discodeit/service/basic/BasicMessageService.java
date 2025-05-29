package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public Message create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachmentRequests) {

    User author = userRepository.findById(request.authorId())
        .orElseThrow(() -> new NoSuchElementException(
            ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND))
        );
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new NoSuchElementException(
            ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND))
        );

    List<BinaryContent> attachments = attachmentRequests.stream()
        .map(req ->
            new BinaryContent(req.fileName(), (long) req.bytes().length, req.contentType(),
                req.bytes()))
        .map(binaryContentRepository::save)
        .toList();

    Message message = new Message(
        request.content(),
        channel,
        author,
        attachments
    );

    message.validateContent();
    return messageRepository.save(message);
  }

  @Override
  public Message find(UUID id) {
    return messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
        ErrorMessages.format("Message", ErrorMessages.ERROR_NOT_FOUND)
    ));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream()
        .toList();
  }

  @Override
  @Transactional
  public Message update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException(
            ErrorMessages.format("Message", ErrorMessages.ERROR_NOT_FOUND)));

    message.update(newContent);
    return message;
  }

  @Override
  public void delete(UUID messageId) {
    messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(
        ErrorMessages.format("Message", ErrorMessages.ERROR_NOT_FOUND)));

    messageRepository.deleteById(messageId);
  }
}
