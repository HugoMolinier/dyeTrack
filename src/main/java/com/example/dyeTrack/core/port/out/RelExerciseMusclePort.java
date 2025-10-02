package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;

public interface RelExerciseMusclePort {
    void saveAll(List<RelExerciseMuscle> relExerciseMuscles);
    List<RelExerciseMuscle> findAllWithExerciseAndMuscle();
    
    
}
