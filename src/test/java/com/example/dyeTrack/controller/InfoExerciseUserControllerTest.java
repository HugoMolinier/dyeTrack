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

import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.in.exercise.dto.ExerciseDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.in.infoExerciseUser.dto.UpdateInfoExerciseUserDTO;
import com.example.dyeTrack.in.infoExerciseUser.dto.out.ReturnInfoExerciseUserDTO;
import com.example.dyeTrack.out.infoExerciseUser.InfoExerciseUserRepository;
import com.example.dyeTrack.out.user.UserRepository;
import com.example.dyeTrack.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
public class InfoExerciseUserControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private InfoExerciseUserRepository infoExerciseUserRepository;

        private String tokenUser1;

        @BeforeEach
        void setUp() throws Exception {
                userRepository.deleteAll();
                tokenUser1 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "userA");
        }

        @Test
        void testUpdateInfo_createNewInfo_success() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise(
                                "Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));
                ExerciseDetailReturnDTO createdExe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                // Création ou mise à jour de l’info
                UpdateInfoExerciseUserDTO updateDto = new UpdateInfoExerciseUserDTO(true, "Très bon exercise");

                String response = mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercise())
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, updateDto)))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                ReturnInfoExerciseUserDTO result = TestUtils.assertAndExtractData(response, null, objectMapper,
                                ReturnInfoExerciseUserDTO.class);

                assertThat(result).isNotNull();
                assertThat(result.getIdExercise()).isEqualTo(createdExe.getIdExercise());
                assertThat(result.getIdExercise()).isEqualTo(createdExe.getIdExercise());
                assertThat(result.getNote()).isEqualTo("Très bon exercise");
                assertThat(result.getFavorite()).isTrue();
        }

        @Test
        void testUpdateInfo_removeWhenEmptyAndNotFavorite() throws Exception {
                // Création d’un exercise
                ExerciseCreateDTO dto = TestUtils.buildExercise(
                                "Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true)));
                ExerciseDetailReturnDTO createdExe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                // Étape 1 : on crée une info
                UpdateInfoExerciseUserDTO createDto = new UpdateInfoExerciseUserDTO(true, "Top !");
                mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercise())
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, createDto)))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                assertThat(infoExerciseUserRepository.findAll()).hasSize(1);

                // Étape 2 : on supprime la note et le favori → suppression automatique
                UpdateInfoExerciseUserDTO removeDto = new UpdateInfoExerciseUserDTO(false, "");
                mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercise())
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, removeDto)))
                                .andExpect(status().isOk());

                // Vérifie que l’entrée a été supprimée
                assertThat(infoExerciseUserRepository.findAll()).isEmpty();
        }

        @Test
        void testGetAllInfoOfUser_success() throws Exception {
                // Création d’un exercise
                ExerciseCreateDTO dto = TestUtils.buildExercise(
                                "Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true)));
                ExerciseDetailReturnDTO createdExe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                UpdateInfoExerciseUserDTO dto1 = new UpdateInfoExerciseUserDTO(true, "Bon exercise");

                mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercise())
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, dto1)))
                                .andExpect(status().isOk());

                UpdateInfoExerciseUserDTO dto2 = new UpdateInfoExerciseUserDTO(false, null); // juste changer favorite

                mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercise())
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

                List<ReturnInfoExerciseUserDTO> list = TestUtils.assertAndExtractDataList(response,
                                "Liste des exercises utilisateur récupérée avec succès", objectMapper,
                                ReturnInfoExerciseUserDTO.class);
                assertThat(list).isNotEmpty();
                assertThat(list.get(0).getIdExercise()).isEqualTo(createdExe.getIdExercise());
        }

        @Test
        void testUpdateInfo_invalidToken_shouldFail() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "desc",
                                List.of(new MuscleInfo(1L, true)));
                ExerciseDetailReturnDTO createdExe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                UpdateInfoExerciseUserDTO body = new UpdateInfoExerciseUserDTO(true, "Top !");
                mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercise())
                                .header("Authorization", "Bearer wrong_token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, body)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testGetAll_otherUserHasNoInfo() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "desc",
                                List.of(new MuscleInfo(1L, true)));
                ExerciseDetailReturnDTO createdExe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                UpdateInfoExerciseUserDTO body = new UpdateInfoExerciseUserDTO(true, "Top !");
                mockMvc.perform(post("/api/info-exercise-user/" + createdExe.getIdExercise())
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

                List<ReturnInfoExerciseUserDTO> infos = TestUtils.assertAndExtractDataList(response,
                                "Liste des exercises utilisateur récupérée avec succès", objectMapper,
                                ReturnInfoExerciseUserDTO.class);

                assertThat(infos).isEmpty();
        }
}
