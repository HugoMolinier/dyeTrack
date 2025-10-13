package com.example.dyeTrack.out.relExerciseMuscle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dyeTrack.core.entity.RelExerciseMuscle.*;

import jakarta.transaction.Transactional;

public interface RelExerciseMuscleRepository extends JpaRepository<RelExerciseMuscle, RelExerciseMuscleId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM RelExerciseMuscle rem WHERE rem.exercise.id = :exerciseId")
    void deleteByExerciseId(@Param("exerciseId") Long exerciseId);
}
