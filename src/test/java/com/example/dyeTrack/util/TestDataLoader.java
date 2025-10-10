package com.example.dyeTrack.util;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.Equipement;
import com.example.dyeTrack.core.entity.GroupeMusculaire;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.out.equipement.EquipementRepository;
import com.example.dyeTrack.out.groupeMusculaire.GroupeMusculaireRepository;
import com.example.dyeTrack.out.lateralite.LateraliteAdapter;
import com.example.dyeTrack.out.lateralite.LateraliteRepository;
import com.example.dyeTrack.out.muscle.MuscleRepository;

/**
 * Charge des données de référence pour les tests d'intégration
 */
@Component
@Profile("test") // s'exécute uniquement pour le profile 'test'
public class TestDataLoader implements CommandLineRunner {

    private final LateraliteRepository lateraliteRepository;
    private final EquipementRepository equipementRepository;

    private final GroupeMusculaireRepository groupeMusculaireRepository;

    private final MuscleRepository muscleRepository;

    public TestDataLoader(MuscleRepository muscleRepository, GroupeMusculaireRepository groupeMusculaireRepository,
            LateraliteRepository lateraliteRepository,
            LateraliteAdapter lateraliteAdapter, EquipementRepository equipementRepository) {
        this.muscleRepository = muscleRepository;
        this.groupeMusculaireRepository = groupeMusculaireRepository;
        this.lateraliteRepository = lateraliteRepository;
        this.equipementRepository = equipementRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        saveGroupeEtMuscles();
        addData();

    }

    private void addData() {

        doTheSaveLateralite("Bilatéral", "Bilateral", 1L);
        doTheSaveLateralite("Unilatéral", "Unilateral", 2L);
        doTheSaveLateralite("Alterné", "Alternated", 3L);

        // Equipements
        doTheSaveEquipement("Haltère", "Dumbbell", 1L);
        doTheSaveEquipement("Barre", "Bar", 2L);
        doTheSaveEquipement("Élastique", "Elastic Band", 3L);
        doTheSaveEquipement("Poids du corps", "Bodyweight", 4L);
        doTheSaveEquipement("Poulie", "Cable Machine", 5L);
        doTheSaveEquipement("Smith machine", "Smith Machine", 6L);
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

    private void doTheSaveGroupeEtMuscles(Long groupeId, String nomFr, String nomEn, List<List<Object>> muscles) {
        GroupeMusculaire groupe = groupeMusculaireRepository.findOneByNomFRAndNomEN(nomFr, nomEn);
        if (groupe == null) {
            groupe = groupeMusculaireRepository.save(new GroupeMusculaire(groupeId, nomFr, nomEn));
        }

        for (List<Object> muscleNom : muscles) {
            Long id = (Long) muscleNom.get(0);
            String nomMuscleFR = (String) muscleNom.get(1);
            String nomMuscleEN = (String) muscleNom.get(2);

            // vérifie si le muscle existe déjà
            if (muscleRepository.findByName(nomMuscleFR).isEmpty()) {
                muscleRepository.save(new Muscle(id, nomMuscleFR, nomMuscleEN, groupe));
            }
        }
    }

    private void doTheSaveLateralite(String nomFr, String nomEn, Long id) {
        Lateralite lat = lateraliteRepository.findOneByNomFRAndNomEN(nomFr, nomEn);
        if (lat == null) {
            lateraliteRepository.save(new Lateralite(id, nomFr, nomEn));
        }
    }

    private void doTheSaveEquipement(String nomFr, String nomEn, Long id) {
        Equipement lat = equipementRepository.findOneByNomFRAndNomEN(nomFr, nomEn);
        if (lat == null) {
            equipementRepository.save(new Equipement(id, nomFr, nomEn));
        }
    }
}
