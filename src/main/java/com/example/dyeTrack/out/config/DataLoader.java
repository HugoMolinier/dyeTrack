package com.example.dyeTrack.out.config;

import java.util.List;
import com.example.dyeTrack.out.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.GroupeMusculaire;
import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.out.exercise.ExerciseRepository;
import com.example.dyeTrack.out.groupeMusculaire.GroupeMusculaireRepository;
import com.example.dyeTrack.out.muscle.MuscleRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final GroupeMusculaireRepository groupeMusculaireRepository;

    private final MuscleRepository muscleRepository;
    private final ExerciseRepository exerciseRepository;

    public DataLoader(MuscleRepository muscleRepository, GroupeMusculaireRepository groupeMusculaireRepository,ExerciseRepository exerciseRepository, UserRepository userRepository) {
        this.muscleRepository = muscleRepository;
        this.groupeMusculaireRepository = groupeMusculaireRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        
    }

    @Override
    public void run(String... args) throws Exception {

        saveGroupeEtMuscles();
        addVerifExercice();

    }



    private void addVerifExercice(){

        userRepository.save(new User("hugo For test"));
    }

    private void saveGroupeEtMuscles(){
          doTheSaveGroupeEtMuscles("Bras", "Arms", List.of(
            List.of("Triceps", "Triceps"),
            List.of("Biceps", "Biceps"),
            List.of("Brachialis", "Brachialis"),
            List.of("Avant Bras", "Forearm")
        ));

        doTheSaveGroupeEtMuscles("Épaules", "Shoulder", List.of(
            List.of("Épaule", "Shoulder"),
            List.of("Infra-épineux", "Infraspinatus")
        ));

        doTheSaveGroupeEtMuscles("Pectoraux", "Chest", List.of(
            List.of("Pectoraux", "Pectorals")
        ));

        doTheSaveGroupeEtMuscles("Dos", "Back", List.of(
            List.of("Trapèzes", "Trapezius"),
            List.of("Dorsaux", "Lats"),
            List.of("Lombaires", "Lower Back"),
            List.of("Grand Rond", "Teres Major")
        ));

        doTheSaveGroupeEtMuscles("Jambes", "Leg", List.of(
            List.of("Quadriceps", "Quadriceps"),
            List.of("Ischio-jambiers", "Hamstrings"),
            List.of("Mollets", "Calves"),
            List.of("Fessiers", "Glutes")
        ));

        doTheSaveGroupeEtMuscles("Autre", "Other", List.of(
            List.of("Abdominaux", "Abs"),
            List.of("Cou", "Neck")
        ));

        doTheSaveGroupeEtMuscles("Cardio", "Cardio", List.of(
            List.of("Coeur", "Heart")
        ));

        System.out.println("✅ Muscles insérés ou vérifiés en base !");

    }

    private void doTheSaveGroupeEtMuscles(String nomFr, String nomEn, List<List<String>> muscles) {
        GroupeMusculaire groupe = groupeMusculaireRepository.findOneByNomFRAndNomEN(nomFr, nomEn);
        if (groupe==null){groupe= groupeMusculaireRepository.save(new GroupeMusculaire(nomFr, nomEn));}

        for (List<String> muscleNom : muscles) {
            if (muscleRepository.findByName(nomFr).isEmpty()) {
                muscleRepository.save(new Muscle(muscleNom.get(0),muscleNom.get(1), groupe));
            }
        }
    }


    
}