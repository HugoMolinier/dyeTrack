package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.valueobject.MuscleInfo;

public interface ExerciseUseCase {
        Exercise getByIdExercise(Long idExercise, Boolean onlyPrincipalMuscle);

        List<Exercise> getAll(String name, Boolean officialExercise, Long idUser, Boolean onlyPrincipalMuscle,
                        Boolean showMainFocusMuscularGroup, List<Integer> idsGroupesMusculaire, List<Integer> idsMuscle,
                        List<Long> idsExercises);

        Exercise create(String nameFR, String description, String linkVideo, Long idUser,
                        List<MuscleInfo> muscles);

        Exercise update(Long idExercise, Long idUserQuiModifie, String nameFR, String description, String linkVideo,
                        List<MuscleInfo> relExerciseMuscles);

        void delete(Long idExercise, Long idUser);
}
