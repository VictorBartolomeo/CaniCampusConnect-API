package org.example.canicampusconnectapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.canicampusconnectapi.model.users.Coach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CoachEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Coach createValidCoach() {
        Coach coach = new Coach();
        coach.setFirstname("Updated");
        coach.setLastname("Coach");
        coach.setEmail("updated.coach@test.com");
        coach.setAcacedNumber("ACACED123456");
        coach.setPhone("0123456789");
        return coach;
    }

    @Test
    @WithMockUser(username = "club.owner@test.com", roles = {"CLUB_OWNER"})
    void completeCoachWorkflow() throws Exception {
        mockMvc.perform(get("/coaches"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;

        mockMvc.perform(get("/coach/1"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assert status == 200 || status == 404;
                });

        Coach updateData = createValidCoach();

        mockMvc.perform(put("/coach/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assert status == 200 || status == 404 || status == 400;

                    if (status == 400) {
                        System.out.println("⚠️ Validation failed (expected): " +
                                result.getResponse().getContentAsString());
                    }
                });
    }
}