package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void testUserOperations() throws Exception {
        UserRequest createReq = new UserRequest("Иван Иванов", "ivan@example.com", "COORDINATOR");
        String userJson = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Иван Иванов"))
                .andExpect(jsonPath("$.email").value("ivan@example.com"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andReturn().getResponse().getContentAsString();

        UserDto createdUser = objectMapper.readValue(userJson, UserDto.class);
        Long userId = createdUser.getId();

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Иван Иванов"));

        UserRequest patchReq = new UserRequest("Иван Петров", null, null);
        mockMvc.perform(patch("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Иван Петров"))
                .andExpect(jsonPath("$.email").value("ivan@example.com"));

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    void testCascadeDeleteChain() throws Exception {
        UserRequest coordReq = new UserRequest("Координатор", "coord@example.com", "COORDINATOR");
        String coordJson = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coordReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long coordId = objectMapper.readValue(coordJson, UserDto.class).getId();

        UserRequest volReq = new UserRequest("Волонтёр", "vol@example.com", "VOLUNTEER");
        String volJson = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(volReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long volId = objectMapper.readValue(volJson, UserDto.class).getId();

        VolunteerCampaignRequest campReq = new VolunteerCampaignRequest(
                "Помощь приюту",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 10),
                coordId,
                "ACTIVE"
        );
        String campJson = mockMvc.perform(post("/api/volunteer-campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long campId = objectMapper.readValue(campJson, VolunteerCampaignDto.class).getId();

        VolunteerTaskRequest taskReq = new VolunteerTaskRequest(
                campId,
                "Выгул собак",
                5,
                LocalDateTime.of(2026, 11, 2, 10, 0),
                3
        );
        String taskJson = mockMvc.perform(post("/api/volunteer-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long taskId = objectMapper.readValue(taskJson, VolunteerTaskDto.class).getId();

        VolunteerResponseRequest respReq = new VolunteerResponseRequest(taskId, volId, "APPROVED");
        String respJson = mockMvc.perform(post("/api/volunteer-responses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(respReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long respId = objectMapper.readValue(respJson, VolunteerResponseDto.class).getId();

        ParticipationReportRequest repReq = new ParticipationReportRequest(respId, 3, "CONFIRMED");
        String repJson = mockMvc.perform(post("/api/participation-reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long repId = objectMapper.readValue(repJson, ParticipationReportDto.class).getId();

        mockMvc.perform(get("/api/volunteer-campaigns/" + campId)).andExpect(status().isOk());
        mockMvc.perform(get("/api/volunteer-tasks/" + taskId)).andExpect(status().isOk());
        mockMvc.perform(get("/api/volunteer-responses/" + respId)).andExpect(status().isOk());
        mockMvc.perform(get("/api/participation-reports/" + repId)).andExpect(status().isOk());

        mockMvc.perform(delete("/api/volunteer-campaigns/" + campId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/volunteer-campaigns/" + campId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/volunteer-tasks/" + taskId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/volunteer-responses/" + respId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/participation-reports/" + repId)).andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    void testServerTimeGeneration() throws Exception {
        UserRequest userReq = new UserRequest("Сервер Тайм", "time@example.com", "VOLUNTEER");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }
}
