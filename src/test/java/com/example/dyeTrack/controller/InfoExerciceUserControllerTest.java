package com.example.dyeTrack.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dyeTrack.core.valueobject.MuscleInsertExercice;
import com.example.dyeTrack.in.exercise.dto.ExerciceDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.in.infoExerciceUser.dto.UpdateInfoExerciceUserDTO;
import com.example.dyeTrack.in.infoExerciceUser.dto.out.ReturnInfoExerciceUserDTO;
import com.example.dyeTrack.out.infoExerciceUser.InfoExerciceUserRepository;
import com.example.dyeTrack.out.user.UserRepository;
import com.example.dyeTrack.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
public class InfoExerciceUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InfoExerciceUserRepository infoExerciceUserRepository;

    private String tokenUser1;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        tokenUser1 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "userA");
    }

    @Test
    void testUpdateInfo_createNewInfo_success() throws Exception {
        ExerciseCreateDTO dto = TestUtils.buildExercise(
                "Pompes", "Exercice pectoraux",
                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));
        ExerciceDetailReturnDTO createdExe = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);

        // Création ou mise à jour de l’info
        UpdateInfoExerciceUserDTO updateDto = new UpdateInfoExerciceUserDTO(true, "Très bon exercice");

        String response = mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercice())
                .header("Authorization", "Bearer " + tokenUser1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJson(objectMapper, updateDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ReturnInfoExerciceUserDTO result = objectMapper.readValue(response, ReturnInfoExerciceUserDTO.class);

        assertThat(result).isNotNull();
        assertThat(result.getIdExercice()).isEqualTo(createdExe.getIdExercice());
        assertThat(result.getIdExercice()).isEqualTo(createdExe.getIdExercice());
        assertThat(result.getNote()).isEqualTo("Très bon exercice");
        assertThat(result.getFavorite()).isTrue();
    }

    @Test
    void testUpdateInfo_removeWhenEmptyAndNotFavorite() throws Exception {
        // Création d’un exercice
        ExerciseCreateDTO dto = TestUtils.buildExercise(
                "Pompes", "Exercice pectoraux",
                List.of(new MuscleInsertExercice(1L, true)));
        ExerciceDetailReturnDTO createdExe = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);

        // Étape 1 : on crée une info
        UpdateInfoExerciceUserDTO createDto = new UpdateInfoExerciceUserDTO(true, "Top !");
        mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercice())
                .header("Authorization", "Bearer " + tokenUser1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJson(objectMapper, createDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(infoExerciceUserRepository.findAll()).hasSize(1);

        // Étape 2 : on supprime la note et le favori → suppression automatique
        UpdateInfoExerciceUserDTO removeDto = new UpdateInfoExerciceUserDTO(false, "");
        mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercice())
                .header("Authorization", "Bearer " + tokenUser1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJson(objectMapper, removeDto)))
                .andExpect(status().isOk());

        // Vérifie que l’entrée a été supprimée
        assertThat(infoExerciceUserRepository.findAll()).isEmpty();
    }

    @Test
    void testGetAllInfoOfUser_success() throws Exception {
        // Création d’un exercice
        ExerciseCreateDTO dto = TestUtils.buildExercise(
                "Pompes", "Exercice pectoraux",
                List.of(new MuscleInsertExercice(1L, true)));
        ExerciceDetailReturnDTO createdExe = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);

        UpdateInfoExerciceUserDTO dto1 = new UpdateInfoExerciceUserDTO(true, "Bon exercice");

        mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercice())
                .header("Authorization", "Bearer " + tokenUser1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJson(objectMapper, dto1)))
                .andExpect(status().isOk());

        UpdateInfoExerciceUserDTO dto2 = new UpdateInfoExerciceUserDTO(false, null); // juste changer favorie

        mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercice())
                .header("Authorization", "Bearer " + tokenUser1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJson(objectMapper, dto2)))
                .andExpect(status().isOk());

        // Récupération de toutes les infos utilisateur
        String response = mockMvc.perform(get("/api/info-exercise-user/getAll")
                .header("Authorization", "Bearer " + tokenUser1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ReturnInfoExerciceUserDTO> list = objectMapper.readValue(
                response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ReturnInfoExerciceUserDTO.class));

        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getIdExercice()).isEqualTo(createdExe.getIdExercice());
    }

    @Test
    void testUpdateInfo_invalidToken_shouldFail() throws Exception {
        ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "desc",
                List.of(new MuscleInsertExercice(1L, true)));
        ExerciceDetailReturnDTO createdExe = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);

        UpdateInfoExerciceUserDTO body = new UpdateInfoExerciceUserDTO(true, "Top !");
        mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercice())
                .header("Authorization", "Bearer wrong_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJson(objectMapper, body)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAll_otherUserHasNoInfo() throws Exception {
        ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "desc",
                List.of(new MuscleInsertExercice(1L, true)));
        ExerciceDetailReturnDTO createdExe = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);

        UpdateInfoExerciceUserDTO body = new UpdateInfoExerciceUserDTO(true, "Top !");
        mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercice())
                .header("Authorization", "Bearer " + tokenUser1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJson(objectMapper, body)))
                .andExpect(status().isOk());

        // Nouvel utilisateur sans info
        String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "userB");

        String response = mockMvc.perform(get("/api/info-exercise-user/getAll")
                .header("Authorization", "Bearer " + tokenUser2))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ReturnInfoExerciceUserDTO> infos = objectMapper.readValue(
                response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ReturnInfoExerciceUserDTO.class));

        assertThat(infos).isEmpty();
    }
}
