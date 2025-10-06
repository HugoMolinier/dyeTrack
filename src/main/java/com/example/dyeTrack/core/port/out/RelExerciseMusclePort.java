package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;

public interface RelExerciseMusclePort {
    List<RelExerciseMuscle> saveAll(List<RelExerciseMuscle> relExerciseMuscles);

    void deleteByExerciceId(Long idExercice);

}
