package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Channel API 통합테스트")
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ChannelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("PUBLIC 채널 생성 - 성공")
    void createPublicChannel_Success() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("새로운 공개채널", "공개채널 설명");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("새로운 공개채널"))
            .andExpect(jsonPath("$.description").value("공개채널 설명"))
            .andExpect(jsonPath("$.type").value("PUBLIC"))
            .andDo(print());
    }

    @Test
    @DisplayName("채널 수정 - 성공")
    void updateChannel_Success() throws Exception {
        String channelId = "44444444-4444-4444-4444-444444444444";
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("수정된채널명", "수정된 설명");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("수정된채널명"))
            .andExpect(jsonPath("$.description").value("수정된 설명"))
            .andExpect(jsonPath("$.type").value("PUBLIC"))
            .andDo(print());
    }

    @Test
    @DisplayName("채널 수정 - 실패 (존재하지 않는 채널)")
    void updateChannel_Failure_ChannelNotFound() throws Exception {
        String nonExistentChannelId = "99999999-9999-9999-9999-999999999999";
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("수정된채널명", "수정된 설명");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/channels/{channelId}", nonExistentChannelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").exists())
            .andDo(print());
    }

    @Test
    @DisplayName("채널 삭제 - 성공")
    void deleteChannel_Success() throws Exception {
        String channelId = "44444444-4444-4444-4444-444444444444";

        mockMvc.perform(delete("/api/channels/{channelId}", channelId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    @DisplayName("채널 삭제 - 실패 (존재하지 않는 채널)")
    void deleteChannel_Failure_ChannelNotFound() throws Exception {
        String nonExistentChannelId = "99999999-9999-9999-9999-999999999999";

        mockMvc.perform(delete("/api/channels/{channelId}", nonExistentChannelId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").exists())
            .andDo(print());
    }

    @Test
    @DisplayName("유효성 검증 실패 - 빈 채널명")
    void createPublicChannel_Failure_EmptyName() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("", "설명");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists())
            .andDo(print());
    }

    @Test
    @DisplayName("PRIVATE 채널 생성 - 실패 (빈 사용자 목록)")
    void createPrivateChannel_Failure_EmptyUserList() throws Exception {
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of());
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists())
            .andDo(print());
    }
}