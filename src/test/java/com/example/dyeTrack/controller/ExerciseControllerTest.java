package com.example.dyeTrack.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.in.exercise.dto.ExerciseDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.out.exercise.ExerciseRepository;
import com.example.dyeTrack.out.user.UserRepository;
import com.example.dyeTrack.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
public class ExerciseControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ExerciseRepository exerciseRepository;

        private String tokenUser1;
        private String tokenUser2;

        @BeforeEach
        void setUp() throws Exception {
                userRepository.deleteAll();
                tokenUser1 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "da5d");
                tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "dfzkjflzka5d");

        }

        @Test
        void testCreateExercise_success() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));

                ExerciseDetailReturnDTO created = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                assertThat(created).isNotNull();
                assertThat(created.getNameFR()).isEqualTo(dto.getNameFR());
                assertThat(created.getDescription()).isEqualTo(dto.getDescription());
                assertThat(created.getMuscleInfos()).hasSize(2);
                assertThat(created.getMuscleInfos()).anyMatch(m -> m.isPrincipal());
                assertThat(created.getMuscleInfos()).anyMatch(m -> !m.isPrincipal());

                assertThat(created.getMainFocusGroup()).isNotNull();
                assertThat(created.getMainFocusGroup()).isEqualTo(1L);
        }

        @Test
        void testCreateExercise_invalidMuscle() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(115L, true), new MuscleInfo(2L, false)));

                mockMvc.perform(post("/api/Exercise/create")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, dto)))
                                .andExpect(status().is(404));
        }

        @Test
        void testCreateExercise_noPrincipal() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, false), new MuscleInfo(2L, false)));

                mockMvc.perform(post("/api/Exercise/create")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testCreateExercise_badToken() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));

                mockMvc.perform(post("/api/exercise/create")
                                .header("Authorization", "Bearer mauvais.token.ici")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, dto)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testCreateExercise_missingFields() throws Exception {
                ExerciseCreateDTO dto = new ExerciseCreateDTO();
                dto.setDescription("Exercise pectoraux");

                mockMvc.perform(post("/api/Exercise/create")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testCreateMultipleExercises_success() throws Exception {
                ExerciseCreateDTO ex1 = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));
                ExerciseCreateDTO ex2 = TestUtils.buildExercise("Tractions", "Exercise dos",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));

                String response = mockMvc.perform(post("/api/Exercise/createMultiple")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(List.of(ex1, ex2))))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                List<ExerciseDetailReturnDTO> createds = TestUtils.assertAndExtractDataList(response,
                                "Exercises créés avec succès", objectMapper,
                                ExerciseDetailReturnDTO.class);

                assertThat(createds).hasSize(2);
                assertThat(createds.get(0).getNameFR()).isEqualTo(ex1.getNameFR());
                assertThat(createds.get(1).getNameFR()).isEqualTo(ex2.getNameFR());
        }

        @Test
        void testUpdateExercise_withValidAndInvalidToken() throws Exception {
                ExerciseCreateDTO ex1 = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false),
                                                new MuscleInfo(3L, false)));

                ExerciseDetailReturnDTO created = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, ex1);

                ExerciseCreateDTO updateDTO = new ExerciseCreateDTO();
                updateDTO.setNameFR("Pompes inclinées");
                updateDTO.setDescription("Exercise pectoraux haut");
                updateDTO.setLinkVideo("http://youtube.com/new-link");

                String response = mockMvc
                                .perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                                .put("/api/Exercise/update/" + created.getIdExercise())
                                                .header("Authorization", "Bearer " + tokenUser1)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.toJson(objectMapper, updateDTO)))
                                .andReturn().getResponse().getContentAsString();

                ExerciseDetailReturnDTO updatedExercise = TestUtils.assertAndExtractData(response,
                                "Exercise mis à jour avec succès", objectMapper,
                                ExerciseDetailReturnDTO.class);

                assertThat(updatedExercise.getNameFR()).isEqualTo("Pompes inclinées");
                assertThat(updatedExercise.getDescription()).isEqualTo("Exercise pectoraux haut");
                assertThat(updatedExercise.getMuscleInfos()).hasSize(3);

                updateDTO.setRelExerciseMuscles(
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));
                String responseafterModif = mockMvc
                                .perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                                .put("/api/Exercise/update/" + created.getIdExercise())
                                                .header("Authorization", "Bearer " + tokenUser1)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.toJson(objectMapper, updateDTO)))
                                .andReturn().getResponse().getContentAsString();

                ExerciseDetailReturnDTO updatedExercise2 = TestUtils.assertAndExtractData(responseafterModif,
                                "Exercise mis à jour avec succès", objectMapper,
                                ExerciseDetailReturnDTO.class);

                assertThat(updatedExercise2.getNameFR()).isEqualTo("Pompes inclinées");
                assertThat(updatedExercise2.getDescription()).isEqualTo("Exercise pectoraux haut");
                assertThat(updatedExercise2.getMuscleInfos()).hasSize(2);
                assertThat(updatedExercise2.getMuscleInfos().stream().filter(MuscleInfo::isPrincipal)).hasSize(1);

                // change muscleInfo
                updateDTO.setRelExerciseMuscles(
                                List.of(new MuscleInfo(1L, false), new MuscleInfo(2L, false)));
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/Exercise/update/" + created.getIdExercise())
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, updateDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Il doit y avoir 1 muscle principal"));

                ExerciseCreateDTO updateByOtherUser = new ExerciseCreateDTO();
                updateByOtherUser.setNameFR("Pompes déclinées");
                updateByOtherUser.setDescription("Tentative modification non autorisée");

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/Exercise/update/" + created.getIdExercise())
                                .header("Authorization", "Bearer " + tokenUser2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, updateByOtherUser)))
                                .andExpect(status().isForbidden());
        }

        @Test
        void testGetById_variants() throws Exception {
                ExerciseCreateDTO ex1 = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));

                ExerciseDetailReturnDTO created = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, ex1);

                Long id = created.getIdExercise();

                // 1️ showMuscles = false -> ExerciseDetailReturnDTO
                String lightResp = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/Exercise/getById/" + id)
                                .param("showMuscles", "false")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                ExerciseDetailReturnDTO lightDTO = TestUtils.assertAndExtractData(lightResp,
                                "Exercise récupérée avec succès", objectMapper,
                                ExerciseDetailReturnDTO.class);
                assertThat(lightDTO.getNameFR()).isEqualTo(ex1.getNameFR());

                // 2️ showMuscles = true -> ExerciseDetailReturnDTO avec muscles
                String detailResp = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/Exercise/getById/" + id)
                                .param("showMuscles", "true")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                ExerciseDetailReturnDTO detailDTO = TestUtils.assertAndExtractData(detailResp,
                                "Exercise récupérée avec succès", objectMapper,
                                ExerciseDetailReturnDTO.class);

                assertThat(detailDTO.getMuscleInfos()).isNotEmpty();
                assertThat(detailDTO.getMuscleInfos()).hasSize(1);

                // 3️ showMainFocusMuscularGroup = true -> mainFocusGroup non null
                String focusResp = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/Exercise/getById/" + id)
                                .param("showMuscles", "true")
                                .param("showMainFocusMuscularGroup", "true")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                ExerciseDetailReturnDTO focusDTO = TestUtils.assertAndExtractData(focusResp,
                                "Exercise récupérée avec succès", objectMapper,
                                ExerciseDetailReturnDTO.class);
                assertThat(focusDTO.getMainFocusGroup()).isNotNull();

                String principalResp = mockMvc
                                .perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                                .get("/api/Exercise/getById/" + id)
                                                .param("showMuscles", "true")
                                                .param("onlyPrincipalMuscle", "true")
                                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                ExerciseDetailReturnDTO principalDTO = TestUtils.assertAndExtractData(principalResp,
                                "Exercise récupérée avec succès", objectMapper,
                                ExerciseDetailReturnDTO.class);
                assertThat(principalDTO.getMuscleInfos()).allMatch(MuscleInfo::isPrincipal);

                // Wrong Id

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/Exercise/getById/" + "999999")
                                .param("showMuscles", "true")
                                .param("onlyPrincipalMuscle", "true")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isNotFound());

        }

        @Test
        void testDeleteExercise_successAndFailures() throws Exception {
                // 1️ Création d’un exercise par user1
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));

                ExerciseDetailReturnDTO created = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);
                Long idExercise = created.getIdExercise();

                // Vérifie que l'exercise existe bien en base
                assertThat(exerciseRepository.findById(idExercise)).isPresent();

                // 2️ Suppression par le bon utilisateur
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/Exercise/delete/" + idExercise)
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk());

                // Vérifie que l'exercise n’existe plus
                assertThat(exerciseRepository.findById(idExercise)).isEmpty();

                // 3️ Tentative de suppression d’un exercise inexistant
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/Exercise/delete/999999")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Exercise not found with id 999999"));

                // 4️ Recréation d’un exercise par user1
                ExerciseDetailReturnDTO created2 = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                // Tentative de suppression par un autre utilisateur
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/Exercise/delete/" + created2.getIdExercise())
                                .header("Authorization", "Bearer " + tokenUser2))
                                .andExpect(status().isForbidden())
                                .andExpect(jsonPath("$.message")
                                                .value("Cet utilisateur ne peut pas delete cet exercise"));

                // Vérifie qu’il existe toujours après la tentative échouée
                assertThat(exerciseRepository.findById(created2.getIdExercise())).isPresent();

                // No token
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/Exercise/delete/" + created2.getIdExercise()))
                                .andExpect(status().isForbidden());
        }

}
