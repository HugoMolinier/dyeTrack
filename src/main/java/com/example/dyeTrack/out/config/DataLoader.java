package com.example.dyeTrack.out.config;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
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

@Component
public class DataLoader implements CommandLineRunner {

    private final LateraliteRepository lateraliteRepository;
    private final EquipementRepository equipementRepository;

    private final GroupeMusculaireRepository groupeMusculaireRepository;

    private final MuscleRepository muscleRepository;

    public DataLoader(MuscleRepository muscleRepository, GroupeMusculaireRepository groupeMusculaireRepository,
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

        doTheSaveLateralite("Bilatéral", "Bilateral");
        doTheSaveLateralite("Unilatéral", "Unilateral");
        doTheSaveLateralite("Alterné", "Alternated");

        doTheSaveEquipement("Haltère", "Dumbbell");
        doTheSaveEquipement("Barre", "Bar");
        doTheSaveEquipement("Élastique", "Elastic Band");
        doTheSaveEquipement("Poids du corps", "Bodyweight");
        doTheSaveEquipement("Poulie", "Cable Machine");
        doTheSaveEquipement("Smith machine", "Smith Machine");
    }

    private void saveGroupeEtMuscles() {
        doTheSaveGroupeEtMuscles("Bras", "Arms", List.of(
                List.of("Triceps", "Triceps"),
                List.of("Biceps", "Biceps"),
                List.of("Brachialis", "Brachialis"),
                List.of("Avant Bras", "Forearm")));

        doTheSaveGroupeEtMuscles("Épaules", "Shoulder", List.of(
                List.of("Épaule", "Shoulder"),
                List.of("Infra-épineux", "Infraspinatus")));

        doTheSaveGroupeEtMuscles("Pectoraux", "Chest", List.of(
                List.of("Pectoraux", "Pectorals")));

        doTheSaveGroupeEtMuscles("Dos", "Back", List.of(
                List.of("Trapèzes", "Trapezius"),
                List.of("Dorsaux", "Lats"),
                List.of("Lombaires", "Lower Back"),
                List.of("Grand Rond", "Teres Major")));

        doTheSaveGroupeEtMuscles("Jambes", "Leg", List.of(
                List.of("Quadriceps", "Quadriceps"),
                List.of("Ischio-jambiers", "Hamstrings"),
                List.of("Mollets", "Calves"),
                List.of("Fessiers", "Glutes")));

        doTheSaveGroupeEtMuscles("Autre", "Other", List.of(
                List.of("Abdominaux", "Abs"),
                List.of("Cou", "Neck")));

        doTheSaveGroupeEtMuscles("Cardio", "Cardio", List.of(
                List.of("Coeur", "Heart")));

        System.out.println("✅ Muscles insérés ou vérifiés en base !");

    }

    private void doTheSaveGroupeEtMuscles(String nomFr, String nomEn, List<List<String>> muscles) {
        GroupeMusculaire groupe = groupeMusculaireRepository.findOneByNomFRAndNomEN(nomFr, nomEn);
        if (groupe == null) {
            groupe = groupeMusculaireRepository.save(new GroupeMusculaire(nomFr, nomEn));
        }

        for (List<String> muscleNom : muscles) {
            if (muscleRepository.findByName(nomFr).isEmpty()) {
                muscleRepository.save(new Muscle(muscleNom.get(0), muscleNom.get(1), groupe));
            }
        }
    }

    private void doTheSaveLateralite(String nomFr, String nomEn) {
        Lateralite lat = lateraliteRepository.findOneByNomFRAndNomEN(nomFr, nomEn);
        if (lat == null) {
            lateraliteRepository.save(new Lateralite(nomFr, nomEn));
        }
    }

    private void doTheSaveEquipement(String nomFr, String nomEn) {
        Equipement lat = equipementRepository.findOneByNomFRAndNomEN(nomFr, nomEn);
        if (lat == null) {
            equipementRepository.save(new Equipement(nomFr, nomEn));
        }
    }

}