package com.example.dyeTrack.out.presetSeanceExercice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dyeTrack.core.entity.PresetSeanceExercice.PresetSeanceExercice;
import com.example.dyeTrack.core.entity.PresetSeanceExercice.PresetSeanceExerciceId;

import jakarta.transaction.Transactional;

public interface PresetSeanceExerciceRepository extends JpaRepository<PresetSeanceExercice, PresetSeanceExerciceId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM PresetSeanceExercice pse WHERE pse.presetSeance.idPresetSeance = :presetId")
    void deleteByPresetId(@Param("presetId") Long presetId);

}
