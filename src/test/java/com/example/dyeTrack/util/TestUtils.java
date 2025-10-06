package com.example.dyeTrack.util;

import com.example.dyeTrack.core.util.HashUtil;
import com.example.dyeTrack.in.exercise.dto.ExerciceDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciceLightReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.in.user.dto.RegisterUserDTO;
import com.example.dyeTrack.in.user.dto.ReturnUserTokenDTO;
import com.example.dyeTrack.out.security.JWTService;
import com.example.dyeTrack.out.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;

public class TestUtils {

    private TestUtils() {
    }

    public static String toJson(ObjectMapper objectMapper, Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * Enregistre un utilisateur via MockMvc et retourne le token + user DTO
     * 
     * @param mockMvc
     * @param objectMapper
     * @param pseudo
     * @param email
     * @param password
     * @return ReturnUserTokenDTO {User + Token}
     * @throws Exception
     */
    public static ReturnUserTokenDTO registerUser(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String pseudo,
            String email,
            String password) throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setPseudo(pseudo);
        dto.setEmail(email);
        dto.setPassword(password);

        String response = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, ReturnUserTokenDTO.class);
    }

    public static ExerciceDetailReturnDTO createExercice(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String token,
            ExerciseCreateDTO dto) throws Exception {

        String response = mockMvc.perform(post("/api/Exercise/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ExerciceDetailReturnDTO created = objectMapper.readValue(response, ExerciceDetailReturnDTO.class);
        if (created.getIdExercice() == null) {
            throw new RuntimeException("L'ID de l'exercice créé est null !");
        }
        return created;
    }

    /**
     * 
     * @param mockMvc
     * @param objectMapper
     * @return Token
     * @throws Exception
     */
    public static String registerAndGetToken(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String inte) throws Exception {
        return registerUser(mockMvc, objectMapper, "Hugo", "hugo@test" + inte + ".com", "password").getToken();
    }

    public static String registerAndGetToken(
            MockMvc mockMvc,
            ObjectMapper objectMapper) throws Exception {
        return registerAndGetToken(mockMvc, objectMapper, "");
    }

    /**
     * 
     * @param returned
     * @param email
     * @param pseudo
     * @param userRepository
     * @param jwtService
     * @param emailSecretKey
     */
    public static void assertUserToken(
            ReturnUserTokenDTO returned,
            String email,
            String pseudo,
            UserRepository userRepository,
            JWTService jwtService,
            String emailSecretKey) {
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
}
