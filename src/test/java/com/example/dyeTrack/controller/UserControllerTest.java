package com.example.dyeTrack.controller;

import com.example.dyeTrack.core.util.HashUtil;
import com.example.dyeTrack.in.user.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.example.dyeTrack.out.security.JWTService;
import com.example.dyeTrack.out.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;

    @Value("${email.secret.key}")
    private String emailSecretKey;

    @BeforeEach
    void cleanDB() {
        userRepository.deleteAll();
    }

    // ----------------- UTILITAIRES -----------------
    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private ReturnUserTokenDTO registerUser(String pseudo, String email, String password) throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setPseudo(pseudo);
        dto.setEmail(email);
        dto.setPassword(password);

        String response = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(dto)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, ReturnUserTokenDTO.class);
    }

    private String registerAndGetToken() throws Exception {
        return registerUser("Hugo", "hugo@test.com", "password").getToken();
    }

    private void assertUserToken(ReturnUserTokenDTO returned, String email, String pseudo) {
        String hashedEmail = HashUtil.hashEmail(email, emailSecretKey);
        var userOpt = userRepository.findByEmail(hashedEmail);
        assertThat(userOpt).isPresent();

        var user = userOpt.get();
        Long extractedUserId = jwtService.extractUserId(returned.getToken());
        assertThat(extractedUserId).isEqualTo(user.getId());
        assertThat(returned.getUserDTO().getPseudo()).isEqualTo(pseudo);
        assertThat(user.getPseudo()).isEqualTo(pseudo);
        assertThat(returned.getUserDTO().getDateNaissance()).isNull();
        assertThat(user.getDateNaissance()).isNull();
    }

    // ----------------- REGISTER -----------------
    @Test
    void testRegister_returnsCreatedUser() throws Exception {
        ReturnUserTokenDTO returned = registerUser("Hugo", "hugo@test.com", "password");
        assertUserToken(returned, "hugo@test.com", "Hugo");
    }

    @Test
    void testRegister_failsWhenEmailAlreadyExists() throws Exception {
        registerUser("Hugo", "hugo@test.com", "password");

        RegisterUserDTO duplicate = new RegisterUserDTO();
        duplicate.setPseudo("Autre");
        duplicate.setEmail("hugo@test.com");
        duplicate.setPassword("autrepassword");

        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(duplicate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegister_failsWhenFieldsEmpty() throws Exception {
        String[][] cases = {
                { "", "hugo@test.com", "password" }, // pseudo vide
                { "Hugo", "", "password" }, // email vide
                { "Hugo", "hugo@test.com", "" } // password vide
        };

        for (String[] c : cases) {
            RegisterUserDTO dto = new RegisterUserDTO();
            dto.setPseudo(c[0]);
            dto.setEmail(c[1]);
            dto.setPassword(c[2]);

            mockMvc.perform(post("/api/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(dto)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ----------------- LOGIN -----------------
    @Test
    void testLogin_success() throws Exception {
        registerUser("Hugo", "hugo@test.com", "password");

        LoginUserDTO loginDto = new LoginUserDTO();
        loginDto.setEmail("hugo@test.com");
        loginDto.setPassword("password");

        String response = mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(loginDto)))
                .andReturn().getResponse().getContentAsString();

        ReturnUserTokenDTO returned = objectMapper.readValue(response, ReturnUserTokenDTO.class);
        assertUserToken(returned, "hugo@test.com", "Hugo");
    }

    @Test
    void testLogin_failsWithWrongCredentials() throws Exception {
        registerUser("Hugo", "hugo@test.com", "password");

        LoginUserDTO[] cases = {
                new LoginUserDTO() {
                    {
                        setEmail("hugo@test.com");
                        setPassword("wrong");
                    }
                },
                new LoginUserDTO() {
                    {
                        setEmail("wrong@test.com");
                        setPassword("password");
                    }
                }
        };

        for (LoginUserDTO dto : cases) {
            mockMvc.perform(post("/api/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(dto)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ----------------- GET CONNECTED USER -----------------
    @Test
    void testGetUserConnected_success() throws Exception {
        String token = registerAndGetToken();

        String response = mockMvc.perform(get("/api/user/getUserConnected")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDTO userDTO = objectMapper.readValue(response, UserDTO.class);
        assertThat(userDTO.getPseudo()).isEqualTo("Hugo");
    }

    @Test
    void testGetUserConnected_failsWithoutOrInvalidToken() throws Exception {
        mockMvc.perform(get("/api/user/getUserConnected"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/user/getUserConnected")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }
}
