package org.example.canicampusconnectapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.canicampusconnectapi.model.users.Coach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleBasedAccessControlTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    private Coach createValidCoach() {
        Coach coach = new Coach();
        // Champs de User (héritage)
        coach.setFirstname("TestFirstname");
        coach.setLastname("TestLastname");
        coach.setEmail("test.coach@example.com");
        coach.setPassword("ValidPass123!");
        coach.setPhone("0123456789");

        // Champs spécifiques à Coach
        coach.setAcacedNumber("ACACED123456");

        return coach;
    }

    @Test
    @WithMockUser(username = "owner@test.com", roles = {"OWNER"})
    void updateCoach_WithOwnerRole_ShouldReturn403() throws Exception {
        Coach coachUpdate = createValidCoach();
        mockMvc.perform(put("/coach/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coachUpdate)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "coach@test.com", roles = {"COACH"})
    void updateCoach_WithCoachRole_ShouldSucceedOrReturn404() throws Exception {
        Coach coachUpdate = createValidCoach();

        mockMvc.perform(put("/coach/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coachUpdate)))
                .andDo(result -> {
                    System.out.println("✅ COACH Status: " + result.getResponse().getStatus());
                    System.out.println("✅ COACH Response: " + result.getResponse().getContentAsString());
                })
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 200 || status == 404,
                            "Expected 200 or 404 but got " + status);
                });
    }

    @Test
    @WithMockUser(username = "club.owner@test.com", roles = {"CLUB_OWNER"})
    void updateCoach_WithClubOwnerRole_ShouldSucceedOrReturn404() throws Exception {
        Coach coachUpdate = createValidCoach();

        mockMvc.perform(put("/coach/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coachUpdate)))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 200 || status == 404,
                            "Expected 200 or 404 but got " + status + ". Response: " + result.getResponse().getContentAsString());
                });
    }

    @Test
    @WithMockUser(username = "simple.user@test.com", roles = {"USER"})
    void updateCoach_WithUserRole_ShouldReturn403() throws Exception {
        Coach coachUpdate = createValidCoach();

        mockMvc.perform(put("/coach/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coachUpdate)))
                .andDo(result -> {
                    System.out.println("🚫 USER Status: " + result.getResponse().getStatus());
                })
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "owner@test.com", roles = {"OWNER"})
    void getCoach_WithOwnerRole_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/coach/1"))
                .andDo(result -> {
                    System.out.println("🔍 GET OWNER Status: " + result.getResponse().getStatus());
                })
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "coach@test.com", roles = {"COACH"})
    void getCoach_WithCoachRole_ShouldSucceedOrReturn404() throws Exception {
        mockMvc.perform(get("/coach/1"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 200 || status == 404,
                            "Expected 200 or 404 but got " + status);
                });
    }

    @Test
    @WithMockUser(username = "owner@test.com", roles = {"OWNER"})
    void accessClubOwnerOnlyEndpoint_WithOwnerRole_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/coaches")) // Ce endpoint n'a que @IsClubOwner (niveau classe)
                .andDo(result -> {
                    System.out.println("🔍 COACHES OWNER Status: " + result.getResponse().getStatus());
                })
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "club.owner@test.com", roles = {"CLUB_OWNER"})
    void accessClubOwnerOnlyEndpoint_WithClubOwnerRole_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/coaches"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertEquals(200, status, "Expected 200 but got " + status);
                });
    }
}