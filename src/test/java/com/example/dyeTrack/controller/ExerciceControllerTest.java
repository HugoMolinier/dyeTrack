package com.example.dyeTrack.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import com.example.dyeTrack.core.valueobject.MuscleInsertExercice;
import com.example.dyeTrack.in.exercise.dto.ExerciceDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciceLightReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.out.exercise.ExerciseRepository;
import com.example.dyeTrack.out.user.UserRepository;
import com.example.dyeTrack.util.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
public class ExerciceControllerTest {

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
        void testCreateExercice_success() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));

                ExerciceDetailReturnDTO created = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);

                assertThat(created).isNotNull();
                assertThat(created.getNameFR()).isEqualTo(dto.getNameFR());
                assertThat(created.getDescription()).isEqualTo(dto.getDescription());
                assertThat(created.getMuscleInfos()).hasSize(2);
                assertThat(created.getMuscleInfos()).anyMatch(m -> m.isPrincipal());
                assertThat(created.getMuscleInfos()).anyMatch(m -> !m.isPrincipal());

                assertThat(created.getMainFocusGroup()).isNotNull();
                assertThat(created.getMainFocusGroup().getId()).isEqualTo(1L);
        }

        @Test
        void testCreateExercice_invalidMuscle() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(115L, true), new MuscleInsertExercice(2L, false)));

                mockMvc.perform(post("/api/Exercise/create")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testCreateExercice_noPrincipal() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(1L, false), new MuscleInsertExercice(2L, false)));

                mockMvc.perform(post("/api/Exercise/create")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testCreateExercice_badToken() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));

                mockMvc.perform(post("/api/exercise/create")
                                .header("Authorization", "Bearer mauvais.token.ici")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, dto)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testCreateExercice_missingFields() throws Exception {
                ExerciseCreateDTO dto = new ExerciseCreateDTO();
                dto.setDescription("Exercice pectoraux");

                mockMvc.perform(post("/api/Exercise/create")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testCreateMultipleExercices_success() throws Exception {
                ExerciseCreateDTO ex1 = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));
                ExerciseCreateDTO ex2 = TestUtils.buildExercise("Tractions", "Exercice dos",
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));

                String response = mockMvc.perform(post("/api/Exercise/createMultiple")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(List.of(ex1, ex2))))
                                .andReturn().getResponse().getContentAsString();

                List<ExerciceLightReturnDTO> createds = objectMapper.readValue(response,
                                new TypeReference<List<ExerciceLightReturnDTO>>() {
                                });

                assertThat(createds).hasSize(2);
                assertThat(createds.get(0).getNameFR()).isEqualTo(ex1.getNameFR());
                assertThat(createds.get(1).getNameFR()).isEqualTo(ex2.getNameFR());
        }

        @Test
        void testUpdateExercise_withValidAndInvalidToken() throws Exception {
                ExerciseCreateDTO ex1 = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false),
                                                new MuscleInsertExercice(3L, false)));

                ExerciceLightReturnDTO created = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, ex1);

                ExerciseCreateDTO updateDTO = new ExerciseCreateDTO();
                updateDTO.setNameFR("Pompes inclinées");
                updateDTO.setDescription("Exercice pectoraux haut");
                updateDTO.setLinkVideo("http://youtube.com/new-link");

                String response = mockMvc
                                .perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                                .put("/api/Exercise/update/" + created.getIdExercice())
                                                .header("Authorization", "Bearer " + tokenUser1)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.toJson(objectMapper, updateDTO)))
                                .andReturn().getResponse().getContentAsString();

                ExerciceDetailReturnDTO updatedExercise = objectMapper.readValue(response,
                                ExerciceDetailReturnDTO.class);

                assertThat(updatedExercise.getNameFR()).isEqualTo("Pompes inclinées");
                assertThat(updatedExercise.getDescription()).isEqualTo("Exercice pectoraux haut");
                assertThat(updatedExercise.getMuscleInfos()).hasSize(3);

                updateDTO.setRelExerciseMuscles(
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));
                String responseafterModif = mockMvc
                                .perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                                .put("/api/Exercise/update/" + created.getIdExercice())
                                                .header("Authorization", "Bearer " + tokenUser1)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.toJson(objectMapper, updateDTO)))
                                .andReturn().getResponse().getContentAsString();

                ExerciceDetailReturnDTO updatedExercise2 = objectMapper.readValue(responseafterModif,
                                ExerciceDetailReturnDTO.class);

                assertThat(updatedExercise2.getNameFR()).isEqualTo("Pompes inclinées");
                assertThat(updatedExercise2.getDescription()).isEqualTo("Exercice pectoraux haut");
                assertThat(updatedExercise2.getMuscleInfos()).hasSize(2);
                assertThat(updatedExercise2.getMuscleInfos().stream().filter(MuscleInfo::isPrincipal)).hasSize(1);

                // change muscleInfo
                updateDTO.setRelExerciseMuscles(
                                List.of(new MuscleInsertExercice(1L, false), new MuscleInsertExercice(2L, false)));
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/Exercise/update/" + created.getIdExercice())
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, updateDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Il doit y avoir 1 muscle principal"));

                ExerciseCreateDTO updateByOtherUser = new ExerciseCreateDTO();
                updateByOtherUser.setNameFR("Pompes déclinées");
                updateByOtherUser.setDescription("Tentative modification non autorisée");

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/Exercise/update/" + created.getIdExercice())
                                .header("Authorization", "Bearer " + tokenUser2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, updateByOtherUser)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testGetById_variants() throws Exception {
                ExerciseCreateDTO ex1 = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));

                ExerciceDetailReturnDTO created = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, ex1);

                Long id = created.getIdExercice();

                // 1️ showMuscles = false -> ExerciceLightReturnDTO
                String lightResp = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/Exercise/getById/" + id)
                                .param("showMuscles", "false")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                ExerciceLightReturnDTO lightDTO = objectMapper.readValue(lightResp, ExerciceLightReturnDTO.class);
                assertThat(lightDTO.getNameFR()).isEqualTo(ex1.getNameFR());

                // 2️ showMuscles = true -> ExerciceDetailReturnDTO avec muscles
                String detailResp = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/Exercise/getById/" + id)
                                .param("showMuscles", "true")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                ExerciceDetailReturnDTO detailDTO = objectMapper.readValue(detailResp, ExerciceDetailReturnDTO.class);

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

                ExerciceDetailReturnDTO focusDTO = objectMapper.readValue(focusResp, ExerciceDetailReturnDTO.class);
                assertThat(focusDTO.getMainFocusGroup()).isNotNull();

                String principalResp = mockMvc
                                .perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                                .get("/api/Exercise/getById/" + id)
                                                .param("showMuscles", "true")
                                                .param("onlyPrincipalMuscle", "true")
                                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                ExerciceDetailReturnDTO principalDTO = objectMapper.readValue(principalResp,
                                ExerciceDetailReturnDTO.class);
                assertThat(principalDTO.getMuscleInfos()).allMatch(MuscleInfo::isPrincipal);
        }

        @Test
        void testDeleteExercise_successAndFailures() throws Exception {
                // 1️ Création d’un exercice par user1
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));

                ExerciceLightReturnDTO created = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);
                Long idExercice = created.getIdExercice();

                // Vérifie que l'exercice existe bien en base
                assertThat(exerciseRepository.findById(idExercice)).isPresent();

                // 2️ Suppression par le bon utilisateur
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/Exercise/delete/" + idExercice)
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Exercise deleted successfully"));

                // Vérifie que l'exercice n’existe plus
                assertThat(exerciseRepository.findById(idExercice)).isEmpty();

                // 3️ Tentative de suppression d’un exercice inexistant
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/Exercise/delete/999999")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("exercise Not found with id 999999"));

                // 4️ Recréation d’un exercice par user1
                ExerciceLightReturnDTO created2 = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);

                // Tentative de suppression par un autre utilisateur
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/Exercise/delete/" + created2.getIdExercice())
                                .header("Authorization", "Bearer " + tokenUser2))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error")
                                                .value("Cet utilisateur ne peut pas delete cet exercice"));

                // Vérifie qu’il existe toujours après la tentative échouée
                assertThat(exerciseRepository.findById(created2.getIdExercice())).isPresent();
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/Exercise/delete/" + created2.getIdExercice()))
                                .andExpect(status().isBadRequest());
        }

}
