package com.example.dyeTrack.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dyeTrack.core.valueobject.MuscleInsertExercice;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciceVO;
import com.example.dyeTrack.in.exercise.dto.ExerciceDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetDetailReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetSeanceCreateRequestDTO;
import com.example.dyeTrack.out.presetSeance.PresetSeanceRepository;
import com.example.dyeTrack.out.user.UserRepository;
import com.example.dyeTrack.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest(properties = "spring.profiles.active=test")
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
                ExerciceDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciceVO(createdexe.getIdExercice(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetDetailReturnDTO presetSeanceex = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                assertThat(presetSeanceex).isNotNull();
                assertThat(presetSeanceex.getName()).isEqualTo(presetSeance.getName());
                assertThat(presetSeanceex.getPresetSeanceExerciceVODTO()).hasSize(1);
                assertThat(presetSeanceex.getPresetSeanceExerciceVODTO().get(0).getExercice().getIdExercice())
                                .isEqualTo(createdexe.getIdExercice());

                assertThat(presetSeanceex.getPresetSeanceExerciceVODTO().get(0).getExercice().getNameFR())
                                .isEqualTo(createdexe.getNameFR());

                assertThat(presetSeanceex.getPresetSeanceExerciceVODTO().get(0).getLateralite().getNomFR())
                                .isEqualTo("Bilatéral");
                assertThat(presetSeanceex.getPresetSeanceExerciceVODTO().get(0).getEquipement().getNomFR())
                                .isEqualTo("Haltère");
        }

        @Test
        void testUpdatePreset_success() throws Exception {
                ExerciceDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciceVO(createdexe.getIdExercice(), "5 reps", 10, 15, 1L,
                                                1L)));
                PresetDetailReturnDTO presetSeanceex = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
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

                PresetDetailReturnDTO presetSeanceex1 = TestUtils.assertAndExtractData(responseupdate2,
                                "Preset mis à jour avec succès", objectMapper,
                                PresetDetailReturnDTO.class);

                assertThat(presetSeanceex1).isNotNull();
                assertThat(presetSeanceex1.getName()).isEqualTo(presetSeanceAfterModif.getName());
                assertThat(presetSeanceex1.getPresetSeanceExerciceVODTO()).hasSize(1);
                assertThat(presetSeanceex1.getPresetSeanceExerciceVODTO().get(0).getExercice().getIdExercice())
                                .isEqualTo(createdexe.getIdExercice());

                assertThat(presetSeanceex1.getName())
                                .isEqualTo(presetSeanceAfterModif.getName());

                assertThat(presetSeanceex1.getPresetSeanceExerciceVODTO().get(0).getLateralite().getNomFR())
                                .isEqualTo("Bilatéral");
                assertThat(presetSeanceex1.getPresetSeanceExerciceVODTO().get(0).getEquipement().getNomFR())
                                .isEqualTo("Haltère");

                // ====

                ExerciceDetailReturnDTO createdexe2 = createSampleExercise("traction");

                PresetSeanceCreateRequestDTO presetSeanceAfterModif2 = new PresetSeanceCreateRequestDTO("Push",
                                List.of(new PresetSeanceExerciceVO(createdexe2.getIdExercice(), "6 hauteur", 9, 14, 1L,
                                                1L),
                                                new PresetSeanceExerciceVO(createdexe.getIdExercice(), "3 hauteur", 7,
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

                PresetDetailReturnDTO presetSeanceex2 = TestUtils.assertAndExtractData(responseupdate3,
                                "Preset mis à jour avec succès", objectMapper,
                                PresetDetailReturnDTO.class);

                assertThat(presetSeanceex2).isNotNull();
                assertThat(presetSeanceex2.getName()).isEqualTo(presetSeanceAfterModif2.getName());
                assertThat(presetSeanceex2.getPresetSeanceExerciceVODTO()).hasSize(2);
                assertThat(presetSeanceex2.getPresetSeanceExerciceVODTO().get(0).getExercice().getIdExercice())
                                .isEqualTo(createdexe2.getIdExercice());

                assertThat(presetSeanceex2.getPresetSeanceExerciceVODTO().get(1).getExercice().getIdExercice())
                                .isEqualTo(createdexe.getIdExercice());

                assertThat(presetSeanceex2.getPresetSeanceExerciceVODTO().get(0).getOrdreExercise())
                                .isEqualTo(1);
                assertThat(presetSeanceex2.getPresetSeanceExerciceVODTO().get(1).getOrdreExercise())
                                .isEqualTo(2);

                assertThat(presetSeanceex2.getPresetSeanceExerciceVODTO().get(0).getParameter())
                                .isEqualTo("6 hauteur");
                assertThat(presetSeanceex2.getPresetSeanceExerciceVODTO().get(0).getRangeRepInf())
                                .isEqualTo(9);
                assertThat(presetSeanceex2.getPresetSeanceExerciceVODTO().get(0).getRangeRepSup())
                                .isEqualTo(14);

                assertThat(presetSeanceex2.getPresetSeanceExerciceVODTO().get(0).getLateralite().getNomFR())
                                .isEqualTo("Bilatéral");
                assertThat(presetSeanceex2.getPresetSeanceExerciceVODTO().get(0).getEquipement().getNomFR())
                                .isEqualTo("Haltère");

        }

        @Test
        void testGet_success() throws Exception {
                ExerciceDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciceVO(createdexe.getIdExercice(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetDetailReturnDTO createPreset = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                String focusResp = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/preset-seances/getById/" + createPreset.getIdPreset())
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                PresetDetailReturnDTO getPreset = TestUtils.assertAndExtractData(focusResp,
                                "Preset récupéré avec succès", objectMapper,
                                PresetDetailReturnDTO.class);
                assertThat(focusResp).isNotNull();
                assertThat(getPreset.getName()).isEqualTo(createPreset.getName());
                assertThat(getPreset.getPresetSeanceExerciceVODTO()).hasSize(1);
                assertThat(getPreset.getPresetSeanceExerciceVODTO().get(0).getExercice().getIdExercice())
                                .isEqualTo(createdexe.getIdExercice());

                assertThat(getPreset.getName())
                                .isEqualTo(createPreset.getName());

                assertThat(getPreset.getPresetSeanceExerciceVODTO().get(0).getLateralite().getNomFR())
                                .isEqualTo("Bilatéral");
                assertThat(getPreset.getPresetSeanceExerciceVODTO().get(0).getEquipement().getNomFR())
                                .isEqualTo("Haltère");

        }

        @Test
        void testCreatePreset_invalidToken_shouldFail() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));

                ExerciceDetailReturnDTO createdexe = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Pull",
                                List.of(new PresetSeanceExerciceVO(createdexe.getIdExercice(), "5 hauteur", 7, 12, 1L,
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
                ExerciceDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciceVO(createdexe.getIdExercice(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetDetailReturnDTO createdPreset = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
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
                ExerciceDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciceVO(createdexe.getIdExercice(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetDetailReturnDTO createdPreset = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);
                // Requête avec mauvais token
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/preset-seances/getById/" + createdPreset.getIdPreset())
                                .header("Authorization", "Bearer wrong_token"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testGetPreset_otherUserToken_shouldFail() throws Exception {
                ExerciceDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciceVO(createdexe.getIdExercice(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetDetailReturnDTO createdPreset = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
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
                ExerciceDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceCreateRequestDTO presetSeance = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciceVO(createdexe.getIdExercice(), "5 reps", 10, 15, 1L,
                                                1L)));

                PresetDetailReturnDTO createdPreset = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
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

                PresetDetailReturnDTO createdPreset2 = TestUtils.createPreset(mockMvc, objectMapper, tokenUser1,
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
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));
                ExerciceDetailReturnDTO createdExe = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);

                PresetSeanceCreateRequestDTO preset1 = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciceVO(createdExe.getIdExercice(), "5 reps", 10, 15, 1L,
                                                1L)));
                PresetSeanceCreateRequestDTO preset2 = new PresetSeanceCreateRequestDTO("Pull Day",
                                List.of(new PresetSeanceExerciceVO(createdExe.getIdExercice(), "5 reps", 10, 15, 1L,
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

                List<PresetDetailReturnDTO> presets = TestUtils.assertAndExtractDataList(response,
                                "Liste des presets récupérée avec succès", objectMapper,
                                PresetDetailReturnDTO.class);

                assertThat(presets).hasSize(2);
                assertThat(presets.stream().map(PresetDetailReturnDTO::getName))
                                .containsExactlyInAnyOrder("Push Day", "Pull Day");

                String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "userB");

                String responseuser2 = mockMvc.perform(get("/api/preset-seances/getAll")
                                .header("Authorization", "Bearer " + tokenUser2)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                List<PresetDetailReturnDTO> presets2 = TestUtils.assertAndExtractDataList(responseuser2,
                                "Liste des presets récupérée avec succès", objectMapper,
                                PresetDetailReturnDTO.class);
                assertThat(presets2).hasSize(0);
        }

        @Test
        void testGetAllOfUser_withNameFilter() throws Exception {
                // Création de presets
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercice pectoraux",
                                List.of(new MuscleInsertExercice(1L, true), new MuscleInsertExercice(2L, false)));
                ExerciceDetailReturnDTO createdExe = TestUtils.createExercice(mockMvc, objectMapper, tokenUser1, dto);

                PresetSeanceCreateRequestDTO preset1 = new PresetSeanceCreateRequestDTO("Push Day",
                                List.of(new PresetSeanceExerciceVO(createdExe.getIdExercice(), "5 reps", 10, 15, 1L,
                                                1L)));
                PresetSeanceCreateRequestDTO preset2 = new PresetSeanceCreateRequestDTO("Pull Day",
                                List.of(new PresetSeanceExerciceVO(createdExe.getIdExercice(), "5 reps", 10, 15, 1L,
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

                List<PresetDetailReturnDTO> presets = TestUtils.assertAndExtractDataList(response,
                                "Liste des presets récupérée avec succès", objectMapper,
                                PresetDetailReturnDTO.class);

                assertThat(presets).hasSize(1);
                assertThat(presets.get(0).getName()).isEqualTo("Push Day");
        }

        private ExerciceDetailReturnDTO createSampleExercise(String name) throws Exception {
                return TestUtils.createExercice(mockMvc, objectMapper, tokenUser1,
                                TestUtils.buildExercise(name, "Exercice pectoraux",
                                                List.of(new MuscleInsertExercice(1L, true),
                                                                new MuscleInsertExercice(2L, false))));
        }

}
