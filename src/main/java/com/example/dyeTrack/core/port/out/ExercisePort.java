package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.Exercise;

public interface ExercisePort {
    Exercise getByIdExercise(Long idExercise);
    List<Exercise> getAll(String name, Boolean officialExercise, Long idUser);
    Exercise create(Exercise exercise);
}
