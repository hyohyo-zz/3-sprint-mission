package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final UserMapper userMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    public UserDto create(UserCreateRequest request,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        String email = request.email();
        String userName = request.username();
        String password = request.password();
        log.info("[user] 생성 요청: email={}, userName={}", email, userName);

        if (userRepository.existsByEmail(email)) {
            log.warn("[user] 생성 실패 - email 중복됨: email={}", email);
            throw new IllegalArgumentException(
                ErrorMessages.format("Email", ErrorMessages.ERROR_EXISTS));
        }
        if (userRepository.existsByUsername(userName)) {
            log.warn("[user] 생성 실패 - userName 중복됨: name={}", userName);
            throw new IllegalArgumentException(
                ErrorMessages.format("UserName", ErrorMessages.ERROR_EXISTS));
        }

        BinaryContent nullableProfile = optionalProfileCreateRequest
            .map(profileCreateRequest -> {
                byte[] bytes = profileCreateRequest.bytes();

                BinaryContent binaryContent = new BinaryContent(
                    profileCreateRequest.fileName(),
                    (long) profileCreateRequest.bytes().length,
                    profileCreateRequest.contentType()
                );

                binaryContentRepository.save(binaryContent);

                // Storage에 bytes 저장
                binaryContentStorage.put(binaryContent.getId(), bytes);
                return binaryContent;
            })
            .orElse(null);

        User user = new User(userName, email, password, nullableProfile);
        userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(user, now);
        userStatusRepository.save(userStatus);
        log.info("[user] 생성 완료: userId={}, name={}, email={}, isProfile={}",
            user.getId(), userName, email, nullableProfile != null);

        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto find(UUID userId) {
        log.info("[user] 조회 요청: id={}", userId);

        return userRepository.findById(userId)
            .map(userMapper::toDto)
            .orElseThrow(() -> {
                log.warn("[user] 조회 실패 - 존재하지 않는 id: id={}", userId);
                return new NoSuchElementException(
                    ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND));
            });
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        log.info("[user] 전체 조회 요청");

        List<UserDto> userDtos = userRepository.findAll()
            .stream()
            .map(userMapper::toDto)
            .toList();

        log.info("[user] 전체 조회 응답: size={}", userDtos.size());
        return userDtos;
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();
        String newPassword = userUpdateRequest.newPassword();

        // update시 요청된 값만 로그 출력
        String logMessage = makeUpdateLog(userId, userUpdateRequest, optionalProfileCreateRequest);
        log.info("[user] 수정 요청: {}", logMessage);

        //1. 수정할 엔티티 조회
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("[user] 수정 실패 - 존재하지 않는 id: id={}", userId);
            return new NoSuchElementException(
                ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND));
        });

        //2. username/email 중복 체크
        if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            log.warn("[user] 수정 실패 - email 중복됨: id={}", newEmail);
            throw new IllegalArgumentException(
                ErrorMessages.format("Email", ErrorMessages.ERROR_EXISTS));
        }
        if (!user.getUsername().equals(newUsername) && userRepository.existsByUsername(
            newUsername)) {
            log.warn("[user] 수정 실패 - userName 중복됨: id={}", newUsername);
            throw new IllegalArgumentException(
                ErrorMessages.format("UserName", ErrorMessages.ERROR_EXISTS));
        }

        BinaryContent newNullableProfile = optionalProfileCreateRequest
            .map(profileRequest -> {
                Optional.ofNullable(user.getProfile())
                    .ifPresent(binaryContentRepository::delete);

                String fileName = profileRequest.fileName();
                String contentType = profileRequest.contentType();
                byte[] bytes = profileRequest.bytes();

                BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                    contentType);
                binaryContentRepository.save(binaryContent);

                binaryContentStorage.put(binaryContent.getId(), bytes);
                return binaryContent;
            })
            .orElse(null);

        user.update(newUsername, newEmail, newPassword, newNullableProfile);
        log.info("[user] 수정 완료: {}", logMessage);

        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
                log.warn("[user] 삭제 실패 - 존재하지 않는 id: id={}", userId);
                return new NoSuchElementException(
                    ErrorMessages.format("User", ErrorMessages.ERROR_NOT_FOUND));
            }
        );
        Optional.ofNullable(user.getProfile()).ifPresent(binaryContentRepository::delete);
        userStatusRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
        log.info("[user] 삭제 완료: id={}", userId);
    }

    private String makeUpdateLog(UUID userId, UserUpdateRequest request,
        Optional<BinaryContentCreateRequest> newProfile) {
        StringBuilder logMessage = new StringBuilder("userId=" + userId);

        String newUsername = request.newUsername();
        String newEmail = request.newEmail();
        String newPassword = request.newPassword();

        if (newUsername != null) {
            logMessage.append(", newUsername=").append(newUsername);
        }
        if (newEmail != null) {
            logMessage.append(", newEmail=").append(newEmail);
        }
        if (newPassword != null) {
            logMessage.append(", newPassword=******");
        }
        if (newProfile.isPresent()) {
            logMessage.append(", newProfileImage=true");
        }

        return logMessage.toString();
    }
}
