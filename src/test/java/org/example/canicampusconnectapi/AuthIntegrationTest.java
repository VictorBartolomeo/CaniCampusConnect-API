package org.example.canicampusconnectapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.dto.UserLoginDto;
import org.example.canicampusconnectapi.model.users.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AuthIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private OwnerDao ownerDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        Owner owner = new Owner();
        owner.setEmail("test@example.com");
        owner.setFirstname("Test");
        owner.setLastname("User");
        owner.setPassword(passwordEncoder.encode("Password123!"));
        owner.setEmailValidated(true);
        ownerDao.save(owner);
    }

    @Test
    @Transactional
    void loginSuccessful_ShouldBeOk() throws Exception {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("Password123!");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @Transactional
    void loginWithWrongPassword_ShouldBeUnauthorized() throws Exception {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("WrongPassword123!");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @Transactional
    void loginWithInvalidEmail() throws Exception {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("nonexistent@example.com");
        loginDto.setPassword("Password123!");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }
}