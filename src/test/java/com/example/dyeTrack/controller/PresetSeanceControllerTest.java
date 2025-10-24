package com.example.dyeTrack.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciseVO;
import com.example.dyeTrack.in.exercise.dto.ExerciseDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetSeanceReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetSeanceCreateRequestDTO;
import com.example.dyeTrack.out.presetSeance.PresetSeanceRepository;
import com.example.dyeTrack.out.user.UserRepository;
import com.example.dyeTrack.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
@Transactional
public class PresetSeanceControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PresetSeanceRepository presetSeanceRepository;

        private String tokenUser1;

        @BeforeEach
        void setUp() throws Exception {
                userRepository.deleteAll();
                tokenUser1 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "da5d");
        }

        @Test
        void testCreatePreset_success() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetSeanceReturnDTO presetSeanceex = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                assertThat(presetSeanceex).isNotNull();
                assertThat(presetSeanceex.getName()).isEqualTo(presetSeance.getName());
                assertThat(presetSeanceex.getPresetSeanceExerciseVODTO()).hasSize(1);
                assertThat(presetSeanceex.getPresetSeanceExerciseVODTO().get(0).getExercise().getIdExercise())
                                .isEqualTo(createdexe.getIdExercise());

                assertThat(presetSeanceex.getPresetSeanceExerciseVODTO().get(0).getExercise().getNameFR())
                                .isEqualTo(createdexe.getNameFR());

                assertThat(presetSeanceex.getPresetSeanceExerciseVODTO().get(0).getIdLateralite())
                                .isEqualTo(1L);
                assertThat(presetSeanceex.getPresetSeanceExerciseVODTO().get(0).getIdEquipment())
                                .isEqualTo(1L);
        }

        @Test
        void testUpdatePreset_success() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));
                PresetSeanceReturnDTO presetSeanceex = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);
                PresetSeanceCreateRequestDTO presetSeanceAfterModif = new PresetSeanceCreateRequestDTO("Pull");

                String responseupdate2 = mockMvc
                                .perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                                .put("/api/preset-seances/update/" + presetSeanceex.getIdPreset())
                                                .header("Authorization", "Bearer " + tokenUser1)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.toJson(objectMapper, presetSeanceAfterModif)))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                PresetSeanceReturnDTO presetSeanceex1 = TestUtils.assertAndExtractData(responseupdate2,
                                "Preset mis à jour avec succès", objectMapper,
                                PresetSeanceReturnDTO.class);

                assertThat(presetSeanceex1).isNotNull();
                assertThat(presetSeanceex1.getName()).isEqualTo(presetSeanceAfterModif.getName());
                assertThat(presetSeanceex1.getPresetSeanceExerciseVODTO()).hasSize(1);
                assertThat(presetSeanceex1.getPresetSeanceExerciseVODTO().get(0).getExercise().getIdExercise())
                                .isEqualTo(createdexe.getIdExercise());

                assertThat(presetSeanceex1.getName())
                                .isEqualTo(presetSeanceAfterModif.getName());

                assertThat(presetSeanceex1.getPresetSeanceExerciseVODTO().get(0).getIdLateralite())
                                .isEqualTo(1L);
                assertThat(presetSeanceex1.getPresetSeanceExerciseVODTO().get(0).getIdEquipment())
                                .isEqualTo(1L);

                // ====

                ExerciseDetailReturnDTO createdexe2 = createSampleExercise("traction");

                PresetSeanceCreateRequestDTO presetSeanceAfterModif2 = new PresetSeanceCreateRequestDTO("Push",
                                List.of(new PresetSeanceExerciseVO(createdexe2.getIdExercise(), "6 hauteur", 9, 14, 1L,
                                                1L),
                                                new PresetSeanceExerciseVO(createdexe.getIdExercise(), "3 hauteur", 7,
                                                                12, 2L,
                                                                2L)));

                String responseupdate3 = mockMvc
                                .perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                                .put("/api/preset-seances/update/" + presetSeanceex.getIdPreset())
                                                .header("Authorization", "Bearer " + tokenUser1)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.toJson(objectMapper, presetSeanceAfterModif2)))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                PresetSeanceReturnDTO presetSeanceex2 = TestUtils.assertAndExtractData(responseupdate3,
                                "Preset mis à jour avec succès", objectMapper,
                                PresetSeanceReturnDTO.class);

                assertThat(presetSeanceex2).isNotNull();
                assertThat(presetSeanceex2.getName()).isEqualTo(presetSeanceAfterModif2.getName());
                assertThat(presetSeanceex2.getPresetSeanceExerciseVODTO()).hasSize(2);
                assertThat(presetSeanceex2.getPresetSeanceExerciseVODTO().get(0).getExercise().getIdExercise())
                                .isEqualTo(createdexe2.getIdExercise());

                assertThat(presetSeanceex2.getPresetSeanceExerciseVODTO().get(1).getExercise().getIdExercise())
                                .isEqualTo(createdexe.getIdExercise());

                assertThat(presetSeanceex2.getPresetSeanceExerciseVODTO().get(0).getOrdreExercise())
                                .isEqualTo(1);
                assertThat(presetSeanceex2.getPresetSeanceExerciseVODTO().get(1).getOrdreExercise())
                                .isEqualTo(2);

                assertThat(presetSeanceex2.getPresetSeanceExerciseVODTO().get(0).getParameter())
                                .isEqualTo("6 hauteur");
                assertThat(presetSeanceex2.getPresetSeanceExerciseVODTO().get(0).getRangeRepInf())
                                .isEqualTo(9);
                assertThat(presetSeanceex2.getPresetSeanceExerciseVODTO().get(0).getRangeRepSup())
                                .isEqualTo(14);

                assertThat(presetSeanceex2.getPresetSeanceExerciseVODTO().get(0).getIdLateralite())
                                .isEqualTo(1L);
                assertThat(presetSeanceex2.getPresetSeanceExerciseVODTO().get(0).getIdEquipment())
                                .isEqualTo(1L);

        }

        @Test
        void testGet_success() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetSeanceReturnDTO createPreset = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                String focusResp = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/preset-seances/getById/" + createPreset.getIdPreset())
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                PresetSeanceReturnDTO getPreset = TestUtils.assertAndExtractData(focusResp,
                                "Preset récupéré avec succès", objectMapper,
                                PresetSeanceReturnDTO.class);
                assertThat(focusResp).isNotNull();
                assertThat(getPreset.getName()).isEqualTo(createPreset.getName());
                assertThat(getPreset.getPresetSeanceExerciseVODTO()).hasSize(1);
                assertThat(getPreset.getPresetSeanceExerciseVODTO().get(0).getExercise().getIdExercise())
                                .isEqualTo(createdexe.getIdExercise());

                assertThat(getPreset.getName())
                                .isEqualTo(createPreset.getName());

                assertThat(getPreset.getPresetSeanceExerciseVODTO().get(0).getIdLateralite())
                                .isEqualTo(1L);
                assertThat(getPreset.getPresetSeanceExerciseVODTO().get(0).getIdEquipment())
                                .isEqualTo(1L);

        }

        @Test
        void testCreatePreset_invalidToken_shouldFail() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));

                ExerciseDetailReturnDTO createdexe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Pull",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), "5 hauteur", 7, 12, 1L,
                                                1L)));

                // Token invalide
                mockMvc.perform(post("/api/preset-seances/create")
                                .header("Authorization", "Bearer invalid_token_123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, presetSeance)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testCreatePreset_otherUserToken_shouldFail() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetSeanceReturnDTO createdPreset = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                // Création d’un autre utilisateur
                String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "otherUser");

                // Tentative de modification par un autre utilisateur
                PresetSeanceCreateRequestDTO modifPreset = new PresetSeanceCreateRequestDTO("Pull Modifié");

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/preset-seances/update/" + createdPreset.getIdPreset())
                                .header("Authorization", "Bearer " + tokenUser2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, modifPreset)))
                                .andExpect(status().isForbidden());
        }

        @Test
        void testGetPreset_invalidToken_shouldFail() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetSeanceReturnDTO createdPreset = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);
                // Requête avec mauvais token
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/preset-seances/getById/" + createdPreset.getIdPreset())
                                .header("Authorization", "Bearer wrong_token"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testGetPreset_otherUserToken_shouldFail() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetSeanceReturnDTO createdPreset = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                // Autre utilisateur
                String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "newUser");

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/preset-seances/getById/" + createdPreset.getIdPreset())
                                .header("Authorization", "Bearer " + tokenUser2))
                                .andExpect(status().isForbidden());

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/preset-seances/getById/" + "898998594")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testDeletePreset_successAndFailures() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetSeanceReturnDTO createdPreset = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);
                Long idPreset = createdPreset.getIdPreset();

                assertThat(idPreset).isNotNull();

                // Vérifie que le preset existe bien
                assertThat(presetSeanceRepository.findById(idPreset)).isPresent();

                // 2️ Suppression réussie par le bon utilisateur
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/preset-seances/delete/" + idPreset)
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk());

                // Vérifie que le preset n’existe plus
                assertThat(presetSeanceRepository.findById(idPreset)).isEmpty();

                // 3️ Tentative de suppression d’un preset inexistant
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/preset-seances/delete/999999")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isNotFound());

                // 4️ Recréation d’un preset par user1

                PresetSeanceReturnDTO createdPreset2 = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);
                String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "userB");

                // Tentative de suppression par un autre utilisateur
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/preset-seances/delete/" + createdPreset2.getIdPreset())
                                .header("Authorization", "Bearer " + tokenUser2))
                                .andExpect(status().isForbidden());

                // Vérifie que le preset existe toujours
                assertThat(presetSeanceRepository.findById(createdPreset2.getIdPreset())).isPresent();

                // 5️ Tentative sans token
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/preset-seances/delete/" + createdPreset2.getIdPreset()))
                                .andExpect(status().isForbidden());
        }

        @Test
        void testGetAllOfUser_success() throws Exception {
                // 1️⃣ Création de 2 presets pour user1
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));
                ExerciseDetailReturnDTO createdExe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                PresetSeanceCreateRequestDTO preset1 = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdExe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));
                PresetSeanceCreateRequestDTO preset2 = new PresetSeanceCreateRequestDTO("Pull Day",
                                List.of(new PresetSeanceExerciseVO(createdExe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));

                // Création des presets via POST
                mockMvc.perform(post("/api/preset-seances/create")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, preset1)))
                                .andExpect(status().isCreated());

                mockMvc.perform(post("/api/preset-seances/create")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, preset2)))
                                .andExpect(status().isCreated());

                // 2️⃣ Récupération de tous les presets pour user1
                String response = mockMvc.perform(get("/api/preset-seances/getAll")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                List<PresetSeanceReturnDTO> presets = TestUtils.assertAndExtractDataList(response,
                                "Liste des presets récupérée avec succès", objectMapper,
                                PresetSeanceReturnDTO.class);

                assertThat(presets).hasSize(2);
                assertThat(presets.stream().map(PresetSeanceReturnDTO::getName))
                                .containsExactlyInAnyOrder("Push Day", "Pull Day");

                String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "userB");

                String responseuser2 = mockMvc.perform(get("/api/preset-seances/getAll")
                                .header("Authorization", "Bearer " + tokenUser2)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                List<PresetSeanceReturnDTO> presets2 = TestUtils.assertAndExtractDataList(responseuser2,
                                "Liste des presets récupérée avec succès", objectMapper,
                                PresetSeanceReturnDTO.class);
                assertThat(presets2).hasSize(0);
        }

        @Test
        void testGetAllOfUser_withNameFilter() throws Exception {
                // Création de presets
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));
                ExerciseDetailReturnDTO createdExe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                PresetSeanceCreateRequestDTO preset1 = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdExe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));
                PresetSeanceCreateRequestDTO preset2 = new PresetSeanceCreateRequestDTO("Pull Day",
                                List.of(new PresetSeanceExerciseVO(createdExe.getIdExercise(), "5 reps", 10, 15, 1L,
                                                1L)));

                mockMvc.perform(post("/api/preset-seances/create")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, preset1)))
                                .andExpect(status().isCreated());

                mockMvc.perform(post("/api/preset-seances/create")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, preset2)))
                                .andExpect(status().isCreated());

                // Filtrage sur "Push"
                String response = mockMvc.perform(get("/api/preset-seances/getAll")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .param("name", "Push")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                List<PresetSeanceReturnDTO> presets = TestUtils.assertAndExtractDataList(response,
                                "Liste des presets récupérée avec succès", objectMapper,
                                PresetSeanceReturnDTO.class);

                assertThat(presets).hasSize(1);
                assertThat(presets.get(0).getName()).isEqualTo("Push Day");
        }

        private ExerciseDetailReturnDTO createSampleExercise(String name) throws Exception {
                return TestUtils.createExercise(mockMvc, objectMapper, tokenUser1,
                                TestUtils.buildExercise(name, "Exercise pectoraux",
                                                List.of(new MuscleInfo(1L, true),
                                                                new MuscleInfo(2L, false))));
        }

}
