package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.valueobject.MuscleInsertExercice;

public interface ExerciseUseCase {
    Exercise getByIdExercise(Long idExercise, Boolean onlyPrincipalMuscle); 
    List<Exercise> getAll(String name, Boolean officialExercise, Long idUser, Boolean onlyPrincipalMuscle,Boolean showMainFocusMuscularGroup,List<Integer> idsGroupesMusculaire,List<Integer> idsMuscle,List<Long> idsExercices);
    void create(String nameFR, String description, String linkVideo, Long idUser,List<MuscleInsertExercice> muscles);
    Exercise update(Long idExercice,Long idUserQuiModifie ,String nameFR, String description, String linkVideo,List<MuscleInsertExercice> relExerciseMuscles);
    void delete(Long idExercice, Long idUser);//verif middleWare token
}
