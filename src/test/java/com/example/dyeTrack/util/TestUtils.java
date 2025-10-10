package com.example.dyeTrack.util;

import com.example.dyeTrack.core.util.HashUtil;
import com.example.dyeTrack.core.valueobject.MuscleInsertExercice;
import com.example.dyeTrack.in.exercise.dto.ExerciceDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetDetailReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetSeanceCreateRequestDTO;
import com.example.dyeTrack.in.user.dto.RegisterUserDTO;
import com.example.dyeTrack.in.user.dto.ReturnUserTokenDTO;
import com.example.dyeTrack.in.utils.ResponseBuilder;
import com.example.dyeTrack.out.security.JWTService;
import com.example.dyeTrack.out.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
    public static ReturnUserTokenDTO registerUser(MockMvc mockMvc, ObjectMapper objectMapper,
            String pseudo, String email, String password) throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setPseudo(pseudo);
        dto.setEmail(email);
        dto.setPassword(password);

        String response = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return assertAndExtractData(response, "Utilisateur créé avec succès", objectMapper, ReturnUserTokenDTO.class);
    }

    public static PresetDetailReturnDTO createPreset(MockMvc mockMvc, ObjectMapper objectMapper,
            String token, PresetSeanceCreateRequestDTO presetDTO) throws Exception {

        String response = mockMvc.perform(post("/api/preset-seances/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJson(objectMapper, presetDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return TestUtils.assertAndExtractData(response, "Preset créé avec succès", objectMapper,
                PresetDetailReturnDTO.class);
    }

    public static <T> T assertAndExtractData(String response, String messageToAssert, ObjectMapper objectMapper,
            Class<T> clazz) throws Exception {
        // Parse JSON
        JsonNode root = objectMapper.readTree(response);

        // Vérifie le message si fourni
        if (messageToAssert != null) {
            String actualMessage = root.get("message").asText();
            assertThat(actualMessage).isEqualTo(messageToAssert);
        }
        JsonNode dataNode = root.get("data");
        assertThat(dataNode).isNotNull();
        return objectMapper.treeToValue(dataNode, clazz);
    }

    public static <T> List<T> assertAndExtractDataList(String response, String expectedMessage,
            ObjectMapper objectMapper, Class<T> elementType) throws Exception {
        ResponseBuilder.ResponseDTO<List<T>> resp = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructParametricType(ResponseBuilder.ResponseDTO.class,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, elementType)));

        assertThat(resp.getMessage()).isEqualTo(expectedMessage);

        return resp.getData();
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
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return assertAndExtractData(response, "Exercice créé avec succès", objectMapper,
                ExerciceDetailReturnDTO.class);

    }

    public static ExerciseCreateDTO buildExercise(String name, String desc, List<MuscleInsertExercice> muscles) {
        ExerciseCreateDTO dto = new ExerciseCreateDTO();
        dto.setNameFR(name);
        dto.setDescription(desc);
        dto.setRelExerciseMuscles(muscles);
        dto.setLinkVideo("http://youtube.com/xxx");
        return dto;
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
