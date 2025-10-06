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
import com.example.dyeTrack.out.lateralite.LateraliteRepository;
import com.example.dyeTrack.out.muscle.MuscleRepository;

/**
 * Charge des données de référence pour les tests d'intégration
 */
@Component
@Profile("test") // s'exécute uniquement pour le profile 'test'
public class TestDataLoader implements CommandLineRunner {

    private final MuscleRepository muscleRepository;
    private final GroupeMusculaireRepository groupeMusculaireRepository;
    private final LateraliteRepository lateraliteRepository;
    private final EquipementRepository equipementRepository;

    public TestDataLoader(MuscleRepository muscleRepository,
            GroupeMusculaireRepository groupeMusculaireRepository,
            LateraliteRepository lateraliteRepository,
            EquipementRepository equipementRepository) {
        this.muscleRepository = muscleRepository;
        this.groupeMusculaireRepository = groupeMusculaireRepository;
        this.lateraliteRepository = lateraliteRepository;
        this.equipementRepository = equipementRepository;
    }

    @Override
    public void run(String... args) {
        saveGroupeEtMuscles();
        saveLateralites();
        saveEquipements();
    }

    private void saveGroupeEtMuscles() {
        GroupeMusculaire bras = groupeMusculaireRepository.findOneByNomFRAndNomEN("Bras", "Arms");
        if (bras == null)
            bras = groupeMusculaireRepository.save(new GroupeMusculaire("Bras", "Arms"));

        if (muscleRepository.findByName("Biceps").isEmpty()) {
            muscleRepository.save(new Muscle("Biceps", "Biceps", bras));
        }
        if (muscleRepository.findByName("Triceps").isEmpty()) {
            muscleRepository.save(new Muscle("Triceps", "Triceps", bras));
        }

        GroupeMusculaire jambes = groupeMusculaireRepository.findOneByNomFRAndNomEN("Jambes", "Leg");
        if (jambes == null)
            jambes = groupeMusculaireRepository.save(new GroupeMusculaire("Jambes", "Leg"));

        if (muscleRepository.findByName("Quadriceps").isEmpty()) {
            muscleRepository.save(new Muscle("Quadriceps", "Quadriceps", jambes));
        }
        if (muscleRepository.findByName("Mollets").isEmpty()) {
            muscleRepository.save(new Muscle("Mollets", "Calves", jambes));
        }
    }

    private void saveLateralites() {
        if (lateraliteRepository.findOneByNomFRAndNomEN("Bilatéral", "Bilateral") == null) {
            lateraliteRepository.save(new Lateralite("Bilatéral", "Bilateral"));
        }
        if (lateraliteRepository.findOneByNomFRAndNomEN("Unilatéral", "Unilateral") == null) {
            lateraliteRepository.save(new Lateralite("Unilatéral", "Unilateral"));
        }
    }

    private void saveEquipements() {
        if (equipementRepository.findOneByNomFRAndNomEN("Haltère", "Dumbbell") == null) {
            equipementRepository.save(new Equipement("Haltère", "Dumbbell"));
        }
        if (equipementRepository.findOneByNomFRAndNomEN("Barre", "Bar") == null) {
            equipementRepository.save(new Equipement("Barre", "Bar"));
        }
    }
}
