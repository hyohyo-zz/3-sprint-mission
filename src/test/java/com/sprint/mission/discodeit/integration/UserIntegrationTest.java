package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
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
@DisplayName("User API 통합테스트")
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유저 생성 - 성공")
    void createUser_Success() throws Exception {
        UserCreateRequest request = new UserCreateRequest("생성유저", "create@email.com",
            "password123!");
        String json = objectMapper.writeValueAsString(request);

        MockMultipartFile userPart = new MockMultipartFile(
            "userCreateRequest", "", "application/json", json.getBytes()
        );

        mockMvc.perform(multipart("/api/users")
                .file(userPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("생성유저"))
            .andExpect(jsonPath("$.email").value("create@email.com"))
            .andDo(print());
    }

    @Test
    @DisplayName("유저 전체 조회 - 성공")
    void getUserAll_Success() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))  // 조현아, 투현아 2명
            .andExpect(jsonPath("$[0].username").value("조현아"))
            .andExpect(jsonPath("$[0].email").value("zzo@email.com"))
            .andExpect(jsonPath("$[1].username").value("투현아"))
            .andExpect(jsonPath("$[1].email").value("z2@email.com"))
            .andDo(print());
    }

    @Test
    @DisplayName("유저 수정 - 성공")
    void updateUser_Success() throws Exception {
        String userId = "11111111-1111-1111-1111-111111111111";
        UserUpdateRequest request = new UserUpdateRequest("뉴현아", "updated@email.com",
            "newPassword123!");
        String json = objectMapper.writeValueAsString(request);

        MockMultipartFile userPart = new MockMultipartFile(
            "userUpdateRequest", "", "application/json", json.getBytes()
        );

        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{id}", userId)
                .file(userPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("뉴현아"))
            .andExpect(jsonPath("$.email").value("updated@email.com"))
            .andDo(print());
    }

    @Test
    @DisplayName("유저 수정 - 실패 (존재하지 않는 유저)")
    void updateUser_Failure_UserNotFound() throws Exception {
        String nonExistentUserId = "99999999-9999-9999-9999-999999999999";
        UserUpdateRequest request = new UserUpdateRequest("뉴현아", "updated@email.com",
            "newPassword123!");
        String json = objectMapper.writeValueAsString(request);

        MockMultipartFile userPart = new MockMultipartFile(
            "userUpdateRequest", "", "application/json", json.getBytes()
        );

        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{id}", nonExistentUserId)
                .file(userPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").exists())
            .andDo(print());
    }

    @Test
    @DisplayName("유저 삭제 - 성공")
    void deleteUser_Success() throws Exception {
        String userId = "11111111-1111-1111-1111-111111111111";

        mockMvc.perform(delete("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    @DisplayName("유저 삭제 - 실패 (존재하지 않는 유저)")
    void deleteUser_Fail_UserNotFound() throws Exception {
        String nonExistentUserId = "99999999-9999-9999-9999-999999999999";

        mockMvc.perform(delete("/api/users/{id}", nonExistentUserId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").exists())
            .andDo(print());
    }
}
