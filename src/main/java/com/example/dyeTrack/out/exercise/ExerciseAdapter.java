package com.example.dyeTrack.out.exercise;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.port.out.ExercisePort;

@Component
public class ExerciseAdapter implements ExercisePort {
    private ExerciseRepository exerciseRepository;

    public ExerciseAdapter(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise getByIdExercise(Long idExercise) {
        return exerciseRepository.findByIdExercise(idExercise);
    }

    public List<Exercise> getAll(String name, Boolean officialExercise, Long idUser, Boolean onlyPrincipalMuscle,
            List<Integer> idsMuscle, List<Long> idsExercises) {
        return exerciseRepository.findAllFiltered(name, officialExercise, idUser, onlyPrincipalMuscle, idsMuscle,
                idsExercises);
    }

    public List<Exercise> getAllWithShowGroupe(String name, Boolean officialExercise, Long idUser,
            Boolean onlyPrincipalMuscle, List<Integer> idsGroupesMusculaire, List<Integer> idsMuscle,
            List<Long> idsExercises) {
        return exerciseRepository.findAllFilteredWithGroup(name, officialExercise, idUser, onlyPrincipalMuscle,
                idsGroupesMusculaire, idsMuscle, idsExercises);
    }

    public Exercise create(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    public Exercise update(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    public void delete(Exercise exercise) {
        exerciseRepository.delete(exercise);
    }

}
