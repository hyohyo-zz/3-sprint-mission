package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.Response.UserDto;
import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.create.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.update.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    public UserDto create(UserCreateRequest request, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(ErrorMessages.format("Email", ErrorMessages.ERROR_EXISTS));
        }
        if (userRepository.existsByUserName(request.name())) {
            throw new IllegalArgumentException(ErrorMessages.format("UserName", ErrorMessages.ERROR_EXISTS));
        }

        UUID nullableProfileId = optionalProfileCreateRequest
               .map(profileCreateRequest -> {
                   byte[] bytes = profileCreateRequest.bytes();
                   String contentType = profileCreateRequest.contentType();
                   String fileName = profileCreateRequest.originalFilename();

                   BinaryContent binaryContent = new BinaryContent(bytes, contentType, fileName);
                   return binaryContentRepository.save(binaryContent).getId();
               })
               .orElse(null);

       User user = new User(request.name(), request.email(), request.phone(), request.password(), nullableProfileId);
       User savedUser = userRepository.create(user);

       Instant now = Instant.now();
       UserStatus userStatus = new UserStatus(savedUser.getId(), now);
       userStatusRepository.create(userStatus);

       return toUserResponse(savedUser);
    }

    @Override
    public UserDto find(UUID userId) {
        User user = userRepository.find(userId).orElseThrow(()-> new NoSuchElementException(
                ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND))
        );
        return toUserResponse(user);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        //1. 수정할 엔티티 조회
        User user = userRepository.find(userId).orElseThrow(()-> new NoSuchElementException(
                ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND))
        );

        String newUsername = userUpdateRequest.newUserName();
        String newEmail = userUpdateRequest.newEmail();

        //2. username/email 중복 체크
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException(ErrorMessages.format("Email", ErrorMessages.ERROR_EXISTS));
        }
        if (userRepository.existsByUserName(newUsername)) {
            throw new IllegalArgumentException(ErrorMessages.format("UserName", ErrorMessages.ERROR_EXISTS));
        }

        UUID nullableProfileId = optionalProfileCreateRequest
                .map(profileRequest -> {
                    Optional.ofNullable(user.getProfileImageId())
                            .ifPresent(binaryContentRepository::deleteById);

                    String fileName = profileRequest.originalFilename();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(bytes, contentType, fileName);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        String newPhone = userUpdateRequest.newPhone();
        String newPassword = userUpdateRequest.newPassword();
        user.update(newUsername, newEmail, newPhone, newPassword, nullableProfileId);

        User savedUser = userRepository.create(user);

        return toUserResponse(savedUser);
    }

    @Override
    public void delete(UUID userId, String password) {
        User user = userRepository.find(userId).orElseThrow(()-> new NoSuchElementException(
                ErrorMessages.format("Channel", ErrorMessages.ERROR_NOT_FOUND))
        );

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException(ErrorMessages.format("Password", ErrorMessages.ERROR_MISMATCH));
        } else {
            Optional.ofNullable(user.getProfileImageId())
                    .ifPresent(binaryContentRepository::deleteById);
            userStatusRepository.deleteByUserId(userId);

            userRepository.deleteById(userId);
        }
    }

    private UserDto toUserResponse(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                user.getProfileImageId(),
                online
        );
    }
}
