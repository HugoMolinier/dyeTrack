package com.example.dyeTrack.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.entity.NutritionTrack;
import com.example.dyeTrack.core.entity.PhysioTrack;
import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise.SetType;
import com.example.dyeTrack.core.valueobject.DayDataOfUserVO;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.core.valueobject.PlannedExerciseVO;
import com.example.dyeTrack.core.valueobject.SeanceTrackVO;
import com.example.dyeTrack.core.valueobject.SetOfPlannedExerciseVO;
import com.example.dyeTrack.in.dayDataOfUser.dto.returnDTO.DayDataOfUserReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseDetailReturnDTO;
import com.example.dyeTrack.out.user.UserRepository;
import com.example.dyeTrack.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
@Transactional
public class DayDataOfUserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private UserRepository userRepository;

        private String tokenUser1;

        @BeforeEach
        void setUp() throws Exception {
                userRepository.deleteAll();
                tokenUser1 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "user1");
        }

        @Test
        void testSaveAndGetDayData_success() throws Exception {
                // Création d'un DayDataOfUserVO avec SeanceTrack vide
                DayDataOfUserVO request = new DayDataOfUserVO(
                                LocalDate.now(),
                                new PhysioTrack(70.5f, 12000, 7.5f, "good", null),
                                new NutritionTrack(2500, 150, 70, 300, 30, 50, null),
                                new SeanceTrackVO(LocalTime.of(10, 0), null, List.of()));

                // Save
                String response = mockMvc.perform(post("/api/daydata/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, request)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                DayDataOfUserReturnDTO savedDayData = TestUtils.assertAndExtractData(response, "DataDay update Info",
                                objectMapper, DayDataOfUserReturnDTO.class);

                // Assertions PhysioTrack
                assertThat(savedDayData.getPhysioTrack()).isNotNull();
                assertThat(savedDayData.getPhysioTrack().getWeight()).isEqualTo(70.5f);
                assertThat(savedDayData.getPhysioTrack().getStep()).isEqualTo(12000);
                assertThat(savedDayData.getPhysioTrack().getHourOfSleep()).isEqualTo(7.5f);
                assertThat(savedDayData.getPhysioTrack().getMood()).isEqualTo("good");

                // Assertions NutritionTrack
                assertThat(savedDayData.getNutritionTrack()).isNotNull();
                assertThat(savedDayData.getNutritionTrack().getCalories()).isEqualTo(2500);
                assertThat(savedDayData.getNutritionTrack().getProteins()).isEqualTo(150);
                assertThat(savedDayData.getNutritionTrack().getLipids()).isEqualTo(70);
                assertThat(savedDayData.getNutritionTrack().getCarbohydrates()).isEqualTo(300);
                assertThat(savedDayData.getNutritionTrack().getFiber()).isEqualTo(30);
                assertThat(savedDayData.getNutritionTrack().getCafeins()).isEqualTo(50);

                // Assertions SeanceTrack
                assertThat(savedDayData.getSeanceTrack()).isNotNull();
                assertThat(savedDayData.getSeanceTrack().getStartHour()).isEqualTo(LocalTime.of(10, 0));
                assertThat(savedDayData.getSeanceTrack().getPresetSeanceId()).isNull();
                assertThat(savedDayData.getSeanceTrack().getPlannedExercises()).isEmpty();

                // Get by ID
                String responseById = mockMvc.perform(get("/api/daydata/getById/" + savedDayData.getIdDayData())
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                DayDataOfUserReturnDTO dayDataById = TestUtils.assertAndExtractData(responseById,
                                "Operation for getById work",
                                objectMapper, DayDataOfUserReturnDTO.class);

                assertThat(dayDataById.getIdDayData()).isEqualTo(savedDayData.getIdDayData());

                // Get by day
                String responseByDay = mockMvc.perform(get("/api/daydata/getDay")
                                .param("day", savedDayData.getDayData().toString())
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                DayDataOfUserReturnDTO dayDataByDay = TestUtils.assertAndExtractData(responseByDay, "dayData ",
                                objectMapper, DayDataOfUserReturnDTO.class);

                assertThat(dayDataByDay.getDayData()).isEqualTo(savedDayData.getDayData());
                assertThat(dayDataByDay.getPhysioTrack().getMood()).isEqualTo("good");
                assertThat(dayDataByDay.getNutritionTrack().getCalories()).isEqualTo(2500);
                assertThat(dayDataByDay.getSeanceTrack().getStartHour()).isEqualTo(LocalTime.of(10, 0));
                assertThat(dayDataByDay.getSeanceTrack().getPlannedExercises()).isEmpty();
        }

        @Test
        void testGetAllDayData_success() throws Exception {
                // Création de 2 journées
                DayDataOfUserVO day1 = new DayDataOfUserVO(LocalDate.of(2025, 10, 10),
                                new PhysioTrack(70, 10000, 7, "ok", null),
                                new NutritionTrack(2200, 140, 60, 250, 25, 30, null),
                                null);

                DayDataOfUserVO day2 = new DayDataOfUserVO(LocalDate.of(2025, 10, 12), null,
                                new NutritionTrack(2300, 145, 65, 260, 28, 40, null),
                                null);

                TestUtils.createDayData(mockMvc, objectMapper, tokenUser1, day1);
                TestUtils.createDayData(mockMvc, objectMapper, tokenUser1, day2);

                String response = mockMvc.perform(get("/api/daydata/getAll")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                List<DayDataOfUser> days = TestUtils.assertAndExtractDataList(response, "Operation for getALl work",
                                objectMapper, com.example.dyeTrack.core.entity.DayDataOfUser.class);

                assertThat(days).hasSize(2);
        }

        @Test
        void testSaveDayData_invalidToken_shouldFail() throws Exception {
                DayDataOfUserVO request = new DayDataOfUserVO(LocalDate.now(),
                                new PhysioTrack(70, 10000, 7, "ok", null),
                                new NutritionTrack(2200, 140, 60, 250, 25, 30, null),
                                null);

                mockMvc.perform(post("/api/daydata/save")
                                .header("Authorization", "Bearer invalid_token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, request)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testDayDataWithSeanceTrackAndSets() throws Exception {
                // 1️⃣ Création d'une journée avec SeanceTrack vide
                DayDataOfUserVO dayDataVO = new DayDataOfUserVO(
                                LocalDate.of(2025, 10, 10),
                                new PhysioTrack(70.5f, 12000, 7.5f, "good"),
                                new NutritionTrack(2500, 150, 70, 300, 30, 50),
                                new SeanceTrackVO(LocalTime.of(10, 0), null, List.of()) // pas d'exercices
                );

                // Save initial
                String response = mockMvc.perform(post("/api/daydata/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, dayDataVO)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                DayDataOfUserReturnDTO savedDay = TestUtils.assertAndExtractData(
                                response,
                                "DataDay update Info",
                                objectMapper,
                                DayDataOfUserReturnDTO.class);

                assertThat(savedDay).isNotNull();
                assertThat(savedDay.getSeanceTrack()).isNotNull();
                assertThat(savedDay.getSeanceTrack().getPlannedExercises()).isEmpty();

                ExerciseCreateDTO dto = TestUtils.buildExercise(
                                "Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));
                ExerciseDetailReturnDTO createdExe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                ExerciseCreateDTO dto2 = TestUtils.buildExercise(
                                "traction", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));
                ExerciseDetailReturnDTO createdExe2 = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto2);
                // 2️⃣ Ajouter des exercices avec sets

                SetOfPlannedExerciseVO set1 = new SetOfPlannedExerciseVO(1, 10, 0, 50.0f, SetType.EFFECTIVE);
                SetOfPlannedExerciseVO set2 = new SetOfPlannedExerciseVO(2, 12, 0, 55.0f, SetType.EFFECTIVE);

                PlannedExerciseVO exercise1 = new PlannedExerciseVO(1, createdExe.getIdExercise(), 1L, 1L,
                                List.of(set1, set2));
                PlannedExerciseVO exercise2 = new PlannedExerciseVO(2, createdExe2.getIdExercise(), 2L, 2L,
                                List.of(set1));

                SeanceTrackVO updatedSeanceVO = new SeanceTrackVO(
                                LocalTime.of(10, 0),
                                null,
                                List.of(exercise1, exercise2));

                assertThat(savedDay.getDayData()).isNotNull();
                DayDataOfUserVO updatedDayVO = new DayDataOfUserVO(
                                savedDay.getDayData(),
                                savedDay.getPhysioTrack(),
                                savedDay.getNutritionTrack(),
                                updatedSeanceVO);

                // Save updated
                response = mockMvc.perform(post("/api/daydata/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, updatedDayVO)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                DayDataOfUserReturnDTO updatedDay = TestUtils.assertAndExtractData(
                                response,
                                "DataDay update Info",
                                objectMapper,
                                DayDataOfUserReturnDTO.class);

                assertThat(updatedDay.getSeanceTrack().getPlannedExercises()).hasSize(2);
                assertThat(updatedDay.getSeanceTrack().getPlannedExercises().get(0).getExerciseOrder()).isEqualTo(1);
                assertThat(updatedDay.getSeanceTrack().getPlannedExercises().get(0).getSets())
                                .hasSize(2);

                // 3️⃣ Modifier l'ordre des exercices
                PlannedExerciseVO reorderedExercise1 = new PlannedExerciseVO(2, createdExe.getIdExercise(), 1L, 1L,
                                List.of(set1));
                PlannedExerciseVO reorderedExercise2 = new PlannedExerciseVO(1, createdExe2.getIdExercise(), 2L, 2L,
                                List.of(set2));

                SeanceTrackVO reorderedSeanceVO = new SeanceTrackVO(
                                LocalTime.of(10, 0),
                                null,
                                List.of(reorderedExercise2, reorderedExercise1));

                DayDataOfUserVO reorderedDayVO = new DayDataOfUserVO(
                                savedDay.getDayData(),
                                savedDay.getPhysioTrack(),
                                savedDay.getNutritionTrack(),
                                reorderedSeanceVO);

                response = mockMvc.perform(post("/api/daydata/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, reorderedDayVO)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                var reorderedDay = TestUtils.assertAndExtractData(
                                response,
                                "DataDay update Info",
                                objectMapper,
                                DayDataOfUser.class);

                assertThat(reorderedDay.getSeanceTrack().getPlannedExercises()).hasSize(2);
                assertThat(reorderedDay.getSeanceTrack().getPlannedExercises().get(0).getExerciseOrder()).isEqualTo(1);
                assertThat(reorderedDay.getSeanceTrack().getPlannedExercises().get(1).getExerciseOrder()).isEqualTo(2);
        }

        @Test
        void testSaveDayData_onlyDayCanProvoque400() throws Exception {
                DayDataOfUserVO request = new DayDataOfUserVO(
                                LocalDate.of(2025, 10, 15),
                                null,
                                null,
                                null);

                mockMvc.perform(post("/api/daydata/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, request)))
                                .andExpect(status().is(400));
        }

        @Test
        void testDeleteDayDataBySendingAllNullForSameDay() throws Exception {
                LocalDate testDate = LocalDate.of(2025, 10, 20);

                // 1️⃣ Création initiale avec données complètes
                DayDataOfUserVO fullDay = new DayDataOfUserVO(
                                testDate,
                                new PhysioTrack(70f, 10000, 7f, "ok"),
                                new NutritionTrack(2200, 140, 60, 250, 25, 30),
                                new SeanceTrackVO(LocalTime.of(10, 0), null, List.of()));

                String response = mockMvc.perform(post("/api/daydata/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, fullDay)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                DayDataOfUserReturnDTO savedDay = TestUtils.assertAndExtractData(
                                response,
                                "DataDay update Info",
                                objectMapper,
                                DayDataOfUserReturnDTO.class);

                assertThat(savedDay.getPhysioTrack()).isNotNull();
                assertThat(savedDay.getNutritionTrack()).isNotNull();

                // 2️⃣ Envoyer un DayDataOfUserVO avec tout à null pour la même date
                DayDataOfUserVO emptyDay = new DayDataOfUserVO(testDate, null, null, null);

                mockMvc.perform(post("/api/daydata/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, emptyDay)))
                                .andExpect(status().isOk());

                // 3️⃣ Vérifier que les données ont été supprimées / vidées
                mockMvc.perform(get("/api/daydata/getDay")
                                .param("day", testDate.toString())
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isNotFound());

        }

        @Test
        void testExercisePartiallyFilledSets() throws Exception {
                // 3️⃣ Exercices partiellement renseignés
                DayDataOfUserVO dayData = new DayDataOfUserVO(
                                LocalDate.of(2025, 10, 25),
                                new PhysioTrack(70, 10000, 7, "ok"),
                                new NutritionTrack(2200, 140, 60, 250, 25, 30),
                                null);

                // Save initial
                DayDataOfUserReturnDTO savedDay = TestUtils.createDayData(mockMvc, objectMapper, tokenUser1, dayData);

                // Créer un exercice
                ExerciseCreateDTO exerciseDTO = TestUtils.buildExercise(
                                "Squat", "Legs exercise",
                                List.of(new MuscleInfo(3L, true)));
                ExerciseDetailReturnDTO createdExercise = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1,
                                exerciseDTO);

                // Créer un PlannedExerciseVO sans sets
                PlannedExerciseVO plannedExercise = new PlannedExerciseVO(1, createdExercise.getIdExercise(), 1L, 1L,
                                List.of());

                SeanceTrackVO seanceVO = new SeanceTrackVO(LocalTime.of(9, 0), null, List.of(plannedExercise));

                DayDataOfUserVO updatedDay = new DayDataOfUserVO(
                                savedDay.getDayData(),
                                savedDay.getPhysioTrack(),
                                savedDay.getNutritionTrack(),
                                seanceVO);

                DayDataOfUserReturnDTO updatedDayData = TestUtils.createDayData(mockMvc, objectMapper, tokenUser1,
                                updatedDay);

                assertThat(updatedDayData.getSeanceTrack().getPlannedExercises()).hasSize(1);
                assertThat(updatedDayData.getSeanceTrack().getPlannedExercises().get(0)
                                .getSets()).isEmpty();
        }

        @Test
        void testMergeDayDataTracks() throws Exception {
                // 4️⃣ Fusion de données existantes
                LocalDate date = LocalDate.of(2025, 10, 26);

                // Créer avec PhysioTrack
                DayDataOfUserVO dayWithPhysio = new DayDataOfUserVO(
                                date,
                                new PhysioTrack(68, 9000, 6.5f, "tired"),
                                null,
                                null);
                TestUtils.createDayData(mockMvc, objectMapper, tokenUser1, dayWithPhysio);

                // Envoyer NutritionTrack uniquement
                DayDataOfUserVO dayWithNutrition = new DayDataOfUserVO(
                                date,
                                null,
                                new NutritionTrack(2000, 120, 50, 230, 20, 25),
                                null);

                DayDataOfUserReturnDTO mergedDay = TestUtils.createDayData(mockMvc, objectMapper, tokenUser1,
                                dayWithNutrition);

                assertThat(mergedDay.getPhysioTrack()).isNotNull();
                assertThat(mergedDay.getPhysioTrack().getWeight()).isEqualTo(68f);
                assertThat(mergedDay.getNutritionTrack()).isNotNull();
                assertThat(mergedDay.getNutritionTrack().getCalories()).isEqualTo(2000);
        }

        @Test
        void testMultipleUsersIsolation() throws Exception {
                // 6️⃣ Test multi-utilisateurs
                String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "user2");

                LocalDate dayUser1 = LocalDate.of(2025, 10, 27);
                LocalDate dayUser2 = LocalDate.of(2025, 10, 28);

                DayDataOfUserVO dayDataUser1 = new DayDataOfUserVO(dayUser1,
                                new PhysioTrack(70, 10000, 7, "ok"),
                                null, null);
                DayDataOfUserVO dayDataUser2 = new DayDataOfUserVO(dayUser2,
                                new PhysioTrack(75, 11000, 8, "good"),
                                null, null);

                TestUtils.createDayData(mockMvc, objectMapper, tokenUser1, dayDataUser1);
                TestUtils.createDayData(mockMvc, objectMapper, tokenUser2, dayDataUser2);

                // Vérifier que user1 ne récupère que ses données
                String responseUser1 = mockMvc.perform(get("/api/daydata/getAll")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                List<DayDataOfUser> daysUser1 = TestUtils.assertAndExtractDataList(responseUser1,
                                "Operation for getALl work",
                                objectMapper, DayDataOfUser.class);
                assertThat(daysUser1).hasSize(1);

                // Vérifier que user2 ne récupère que ses données
                String responseUser2 = mockMvc.perform(get("/api/daydata/getAll")
                                .header("Authorization", "Bearer " + tokenUser2))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                List<DayDataOfUser> daysUser2 = TestUtils.assertAndExtractDataList(responseUser2,
                                "Operation for getALl work",
                                objectMapper, DayDataOfUser.class);
                assertThat(daysUser2).hasSize(1);
        }

}
