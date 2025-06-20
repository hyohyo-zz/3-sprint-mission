package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*전체 API의 전역 예외를 처리하는 핸들러 클래스*/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
        log.warn("[예외 처리] DiscodeitException 발생: {}", ex.getMessage(), ex);
        log.debug("[예외 처리] 에러 코드: {}, HTTP 상태: {}", ex.getErrorCode(),
            ex.getErrorCode().getStatus());

        return ResponseEntity.status(ex.getErrorCode().getStatus())
            .body(ErrorResponse.of(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherException(Exception e) {
        log.error("[예외 처리] 예상치 못한 예외 발생: {}", e.getMessage(), e);

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                Instant.now(),
                "UNEXPECTED_EXCEPTION",
                e.getMessage() != null ? e.getMessage() : "알 수 없는 오류입니다.",
                Map.of(),
                e.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
    }

}

