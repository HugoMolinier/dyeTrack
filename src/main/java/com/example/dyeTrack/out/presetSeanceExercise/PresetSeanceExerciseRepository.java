package com.example.dyeTrack.out.presetSeanceExercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dyeTrack.core.entity.PresetSeanceExercise.PresetSeanceExercise;
import com.example.dyeTrack.core.entity.PresetSeanceExercise.PresetSeanceExerciseId;

import jakarta.transaction.Transactional;

public interface PresetSeanceExerciseRepository extends JpaRepository<PresetSeanceExercise, PresetSeanceExerciseId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM PresetSeanceExercise pse WHERE pse.presetSeance.idPresetSeance = :presetId")
    void deleteByPresetId(@Param("presetId") Long presetId);

}
