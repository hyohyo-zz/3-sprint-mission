package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Message API 통합테스트")
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MessageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("메시지 생성 - 성공")
    void createMessage_Success() throws Exception {
        UUID channelId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID authorId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        MessageCreateRequest request = new MessageCreateRequest("통합 테스트 내용", channelId, authorId);
        String json = objectMapper.writeValueAsString(request);

        MockMultipartFile messagePart = new MockMultipartFile(
            "messageCreateRequest", "", "application/json", json.getBytes()
        );

        mockMvc.perform(multipart("/api/messages")
                .file(messagePart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("통합 테스트 내용"))
            .andDo(print());
    }

    @Test
    @DisplayName("메시지 조회 - 성공")
    void getMessagesByChannelId() throws Exception {
        UUID channelId = UUID.fromString("44444444-4444-4444-4444-444444444444");

        mockMvc.perform(get("/api/messages")
                .param("channelId", channelId.toString())
                .param("page", "0")     // 페이지 번호
                .param("size", "10"))   // 페이지 당 항목 수
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andDo(print());
    }

    @Test
    @DisplayName("메시지 수정 - 성공")
    void updateMessage() throws Exception {
        UUID messageId = UUID.fromString("66666666-6666-6666-6666-666666666666");

        MessageUpdateRequest request = new MessageUpdateRequest("수정된 내용");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("수정된 내용"))
            .andDo(print());
    }

    @Test
    @DisplayName("메시지 삭제 - 성공")
    void deleteMessage() throws Exception {
        UUID messageId = UUID.fromString("66666666-6666-6666-6666-666666666666");

        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
            .andExpect(status().isNoContent());
    }
}
