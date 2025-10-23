package com.example.dyeTrack.out.config;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.MuscleGroup;
import com.example.dyeTrack.core.service.ExerciseService;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.out.equipment.EquipmentRepository;
import com.example.dyeTrack.out.muscleGroup.MuscleGroupRepository;
import com.example.dyeTrack.out.lateralite.LateraliteAdapter;
import com.example.dyeTrack.out.lateralite.LateraliteRepository;
import com.example.dyeTrack.out.muscle.MuscleRepository;

@Component
public class DataLoader implements CommandLineRunner {

        private final ExerciseService exerciseService;

        private final LateraliteRepository lateraliteRepository;
        private final EquipmentRepository equipmentRepository;

        private final MuscleGroupRepository muscleGroupRepository;

        private final MuscleRepository muscleRepository;

        public DataLoader(MuscleRepository muscleRepository, MuscleGroupRepository muscleGroupRepository,
                        LateraliteRepository lateraliteRepository,
                        LateraliteAdapter lateraliteAdapter, EquipmentRepository equipmentRepository,
                        ExerciseService exerciseService) {
                this.muscleRepository = muscleRepository;
                this.exerciseService = exerciseService;
                this.muscleGroupRepository = muscleGroupRepository;
                this.lateraliteRepository = lateraliteRepository;
                this.equipmentRepository = equipmentRepository;
        }

        @Override
        public void run(String... args) throws Exception {
                return;
                // saveGroupeEtMuscles();
                // addData();
                // createExercise();

        }

        private void addData() {

                doTheSaveLateralite("Bilatéral", "Bilateral", 1L);
                doTheSaveLateralite("Unilatéral", "Unilateral", 2L);
                doTheSaveLateralite("Alterné", "Alternated", 3L);

                // Equipments
                doTheSaveEquipment("Haltère", "Dumbbell", 1L);
                doTheSaveEquipment("Barre", "Bar", 2L);
                doTheSaveEquipment("Élastique", "Elastic Band", 3L);
                doTheSaveEquipment("Poids du corps", "Bodyweight", 4L);
                doTheSaveEquipment("Poulie", "Cable Machine", 5L);
                doTheSaveEquipment("Smith machine", "Smith Machine", 6L);

        }

        private void saveGroupeEtMuscles() {
                doTheSaveGroupeEtMuscles(1L, "Bras", "Arms", List.of(
                                List.of(1L, "Triceps", "Triceps"),
                                List.of(2L, "Biceps", "Biceps"),
                                List.of(3L, "Brachialis", "Brachialis"),
                                List.of(4L, "Avant Bras", "Forearm")));

                doTheSaveGroupeEtMuscles(2L, "Épaules", "Shoulder", List.of(
                                List.of(5L, "Épaule", "Shoulder"),
                                List.of(6L, "Infra-épineux", "Infraspinatus")));

                doTheSaveGroupeEtMuscles(3L, "Pectoraux", "Chest", List.of(
                                List.of(7L, "Pectoraux", "Pectorals")));

                doTheSaveGroupeEtMuscles(4L, "Dos", "Back", List.of(
                                List.of(8L, "Trapèzes", "Trapezius"),
                                List.of(9L, "Dorsaux", "Lats"),
                                List.of(10L, "Lombaires", "Lower Back"),
                                List.of(11L, "Grand Rond", "Teres Major")));

                doTheSaveGroupeEtMuscles(5L, "Jambes", "Leg", List.of(
                                List.of(12L, "Quadriceps", "Quadriceps"),
                                List.of(13L, "Ischio-jambiers", "Hamstrings"),
                                List.of(14L, "Mollets", "Calves"),
                                List.of(15L, "Fessiers", "Glutes")));

                doTheSaveGroupeEtMuscles(6L, "Autre", "Other", List.of(
                                List.of(16L, "Abdominaux", "Abs"),
                                List.of(17L, "Cou", "Neck")));

                doTheSaveGroupeEtMuscles(7L, "Cardio", "Cardio", List.of(
                                List.of(18L, "Coeur", "Heart")));

                System.out.println("✅ Muscles insérés ou vérifiés en base !");

        }

        private void createExercise() {

                // Exercices pour Pectoraux
                exerciseService.createOfficial(
                                "Développé Couché",
                                "Exercice de base pour les pectoraux",
                                null,
                                List.of(
                                                new MuscleInfo(7L, true), // Pectoraux -> principal
                                                new MuscleInfo(1L, false), // Triceps
                                                new MuscleInfo(5L, false) // Épaule
                                ));

                exerciseService.createOfficial(
                                "Écarté Couché",
                                "Travaille principalement les pectoraux",
                                null,
                                List.of(
                                                new MuscleInfo(7L, true), // Pectoraux -> principal
                                                new MuscleInfo(6L, false) // Infra-épineux
                                ));

                // Exercices pour Dos
                exerciseService.createOfficial(
                                "Tractions",
                                "Exercice de dos complet",
                                null,
                                List.of(
                                                new MuscleInfo(9L, true), // Dorsaux -> principal
                                                new MuscleInfo(8L, false), // Trapèzes
                                                new MuscleInfo(1L, false) // Triceps
                                ));

                exerciseService.createOfficial(
                                "Rowing Barre",
                                "Rowing pour le dos et biceps",
                                null,
                                List.of(
                                                new MuscleInfo(9L, true), // Dorsaux -> principal
                                                new MuscleInfo(2L, false), // Biceps
                                                new MuscleInfo(10L, false) // Lombaires
                                ));

                // Exercices pour Jambes
                exerciseService.createOfficial(
                                "Squat",
                                "Exercice fondamental pour les jambes",
                                null,
                                List.of(
                                                new MuscleInfo(12L, true), // Quadriceps -> principal
                                                new MuscleInfo(15L, false), // Fessiers
                                                new MuscleInfo(14L, false) // Mollets
                                ));

                exerciseService.createOfficial(
                                "Soulevé de Terre",
                                "Travaille le dos et les jambes",
                                null,
                                List.of(
                                                new MuscleInfo(10L, true), // Lombaires -> principal
                                                new MuscleInfo(12L, false), // Quadriceps
                                                new MuscleInfo(13L, false), // Ischio-jambiers
                                                new MuscleInfo(15L, false) // Fessiers
                                ));

                // Exercices pour Bras
                exerciseService.createOfficial(
                                "Curl Biceps",
                                "Exercice pour le biceps",
                                null,
                                List.of(
                                                new MuscleInfo(2L, true), // Biceps -> principal
                                                new MuscleInfo(1L, false) // Triceps
                                ));

                exerciseService.createOfficial(
                                "Extension Triceps",
                                "Exercice pour le triceps",
                                null,
                                List.of(
                                                new MuscleInfo(1L, true), // Triceps -> principal
                                                new MuscleInfo(2L, false) // Biceps
                                ));

                System.out.println("✅ Exercices insérés en base !");
        }

        private void doTheSaveGroupeEtMuscles(Long groupeId, String nameFr, String nameEn, List<List<Object>> muscles) {
                MuscleGroup groupe = muscleGroupRepository.findOneByNameFRAndNameEN(nameFr, nameEn);
                if (groupe == null) {
                        groupe = muscleGroupRepository.save(new MuscleGroup(groupeId, nameFr, nameEn));
                }

                for (List<Object> muscleName : muscles) {
                        Long id = (Long) muscleName.get(0);
                        String nameMuscleFR = (String) muscleName.get(1);
                        String nameMuscleEN = (String) muscleName.get(2);

                        // vérifie si le muscle existe déjà
                        if (muscleRepository.findByName(nameMuscleFR).isEmpty()) {
                                muscleRepository.save(new Muscle(id, nameMuscleFR, nameMuscleEN, groupe));
                        }
                }
        }

        private void doTheSaveLateralite(String nameFr, String nameEn, Long id) {
                Lateralite lat = lateraliteRepository.findOneByNameFRAndNameEN(nameFr, nameEn);
                if (lat == null) {
                        lateraliteRepository.save(new Lateralite(id, nameFr, nameEn));
                }
        }

        private void doTheSaveEquipment(String nameFr, String nameEn, Long id) {
                Equipment lat = equipmentRepository.findOneByNameFRAndNameEN(nameFr, nameEn);
                if (lat == null) {
                        equipmentRepository.save(new Equipment(id, nameFr, nameEn));
                }
        }

}