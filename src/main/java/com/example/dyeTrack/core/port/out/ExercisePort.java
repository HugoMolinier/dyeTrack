package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.Exercise;

public interface ExercisePort {
        Exercise getByIdExercise(Long idExercise);

        List<Exercise> getAll(String name, Boolean officialExercise, Long idUser, Boolean onlyPrincipalMuscle,
                        List<Integer> idsMuscle, List<Long> idsExercises);

        Exercise create(Exercise exercise);

        List<Exercise> getAllWithShowGroupe(String name, Boolean officialExercise, Long idUser,
                        Boolean onlyPrincipalMuscle,
                        List<Integer> idsGroupesMusculaire, List<Integer> idsMuscle, List<Long> idsExercises);

        Exercise update(Exercise exercise);

        void delete(Exercise exercise);
}
