package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.valueobject.MuscleInfo;

public interface ExerciseUseCase {
    Exercise getByIdExercise(Long idExercise, Boolean onlyPrincipalMuscle); 
    List<Exercise> getAll(String name, Boolean officialExercise, Long idUser, Boolean onlyPrincipalMuscle);
    void create(String nameFR, String description, String linkVideo, Long idUser,List<MuscleInfo> muscles);
    //List<Muscle> getByGroupeMuscle(List<Integer> muscle); /// ajouter argument onlyPrincipal?
    //List<Muscle> getByName(List<Integer> muscle,langue); /// ajouter argument onlyPrincipal? 
    // update
}
