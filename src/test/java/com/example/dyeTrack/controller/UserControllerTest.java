package com.example.dyeTrack.controller;

import com.example.dyeTrack.in.user.dto.*;
import com.example.dyeTrack.out.security.JWTService;
import com.example.dyeTrack.out.user.UserRepository;
import com.example.dyeTrack.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
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

    // ----------------- REGISTER -----------------
    @Test
    void testRegister_returnsCreatedUser() throws Exception {
        ReturnUserTokenDTO returned = TestUtils.registerUser(mockMvc, objectMapper, "Hugo", "hugo@test.com",
                "password");

        TestUtils.assertUserToken(returned, "hugo@test.com", "Hugo", userRepository, jwtService, emailSecretKey);
    }

    @Test
    void testRegister_failsWhenEmailAlreadyExists() throws Exception {
        TestUtils.registerUser(mockMvc, objectMapper, "Hugo", "hugo@test.com", "password");

        RegisterUserDTO duplicate = new RegisterUserDTO();
        duplicate.setPseudo("Autre");
        duplicate.setEmail("hugo@test.com");
        duplicate.setPassword("autrepassword");

        mockMvc.perform(post("/api/user/register")
                .contentType("application/json")
                .content(TestUtils.toJson(objectMapper, duplicate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegister_failsWhenFieldsEmpty() throws Exception {
        String[][] cases = {
                { "", "hugo@test.com", "password" },
                { "Hugo", "", "password" },
                { "Hugo", "hugo@test.com", "" }
        };

        for (String[] c : cases) {
            RegisterUserDTO dto = new RegisterUserDTO();
            dto.setPseudo(c[0]);
            dto.setEmail(c[1]);
            dto.setPassword(c[2]);

            mockMvc.perform(post("/api/user/register")
                    .contentType("application/json")
                    .content(TestUtils.toJson(objectMapper, dto)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ----------------- LOGIN -----------------
    @Test
    void testLogin_success() throws Exception {
        TestUtils.registerUser(mockMvc, objectMapper, "Hugo", "hugo@test.com", "password");

        LoginUserDTO loginDto = new LoginUserDTO();
        loginDto.setEmail("hugo@test.com");
        loginDto.setPassword("password");

        String response = mockMvc.perform(post("/api/user/login")
                .contentType("application/json")
                .content(TestUtils.toJson(objectMapper, loginDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ReturnUserTokenDTO returned = TestUtils.assertAndExtractData(
                response,
                "Connexion réussie",
                objectMapper,
                ReturnUserTokenDTO.class);
        TestUtils.assertUserToken(returned, "hugo@test.com", "Hugo", userRepository, jwtService, emailSecretKey);
    }

    @Test
    void testLogin_failsWithWrongCredentials() throws Exception {
        TestUtils.registerUser(mockMvc, objectMapper, "Hugo", "hugo@test.com", "password");

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
                    .contentType("application/json")
                    .content(TestUtils.toJson(objectMapper, dto)))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ----------------- GET CONNECTED USER -----------------
    @Test
    void testGetUserConnected_success() throws Exception {
        String token = TestUtils.registerAndGetToken(mockMvc, objectMapper);

        String response = mockMvc.perform(get("/api/user/getUserConnected")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDTO userDTO = TestUtils.assertAndExtractData(
                response,
                "Utilisateur récupéré avec succès",
                objectMapper,
                UserDTO.class);
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
