package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeanceExercise.PresetSeanceExercise;

public interface PresetSeanceExercisePort {
    void saveAll(List<PresetSeanceExercise> presetSeanceExercises);

    void deleteByPresetId(Long idPreset);
}