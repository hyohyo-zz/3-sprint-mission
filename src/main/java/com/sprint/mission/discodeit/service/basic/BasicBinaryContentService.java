package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessages;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

    public final BinaryContentRepository binaryContentRepository;
    public final BinaryContentStorage binaryContentStorage;
    public final BinaryContentMapper binaryContentMapper;

    @Transactional
    @Override
    public BinaryContentDto create(BinaryContentCreateRequest request) {
        String fileName = request.fileName();
        byte[] bytes = request.bytes();
        String contentType = request.contentType();

        log.info("[binaryContent] 업로드 요청: fileName={}, contentType={}, size={} bytes",
            fileName, contentType, bytes != null ? bytes.length : 0);

        if (bytes == null || bytes.length == 0) {
            log.warn("[binaryContent] 업로드 실패 - 파일 없음 또는 0바이트: fileName={}", fileName);

            throw new RuntimeException(
                ErrorMessages.format("binaryContent", ErrorMessages.ERROR_FILE_UPLOAD_INVALID));
        }

        //1. 메타데이터만 가진 BinaryContent 객체 생성
        BinaryContent binaryContent = new BinaryContent(
            fileName,
            (long) bytes.length,
            contentType
        );

        //2. 실제 바이너리 데이터 저장소에 따로 저장
        binaryContentStorage.put(binaryContent.getId(), bytes);
        binaryContentRepository.save(binaryContent);

        log.info("[binaryContent] 저장 완료: id={}, size={} bytes", binaryContent.getId(),
            bytes.length);

        return binaryContentMapper.toDto(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public BinaryContentDto find(UUID id) {
        log.info("[binaryContent] 조회 요청: id={}", id);

        return binaryContentRepository.findById(id)
            .map(binaryContentMapper::toDto)
            .orElseThrow(() -> {
                log.warn("[binaryContent] 조회 실패 - 존재하지 않는 id: id={}", id);
                return new NoSuchElementException(
                    ErrorMessages.format("BinaryContent", ErrorMessages.ERROR_NOT_FOUND));
            });
    }

    @Transactional(readOnly = true)
    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
        log.info("[binaryContent] 전체 조회 요청: size={}", binaryContentIds.size());

        return binaryContentRepository.findAllById(binaryContentIds).stream()
            .map(binaryContentMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            log.warn("[binaryContent] 삭제 실패 - 존재하지 않는 id: id={}", id);
            throw new IllegalArgumentException(
                ErrorMessages.format("binaryContent", ErrorMessages.ERROR_NOT_FOUND));
        }
        binaryContentRepository.deleteById(id);
        log.info("[binaryContent] 삭제 완료: id={}", id);
    }
}
