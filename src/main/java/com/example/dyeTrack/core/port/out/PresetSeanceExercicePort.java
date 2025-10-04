package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeanceExercice.PresetSeanceExercice;

public interface PresetSeanceExercicePort {
    void saveAll(List<PresetSeanceExercice> presetSeanceExercices);
    void deleteByPresetId(Long idPreset);
}