package com.example.dyeTrack.out.presetSeanceExercise;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.PresetSeanceExercise.PresetSeanceExercise;
import com.example.dyeTrack.core.port.out.PresetSeanceExercisePort;

@Component
public class PresetSeanceExerciseAdapter implements PresetSeanceExercisePort {
    private final PresetSeanceExerciseRepository presetSeanceExerciseRepository;

    public PresetSeanceExerciseAdapter(PresetSeanceExerciseRepository presetSeanceExerciseRepository) {
        this.presetSeanceExerciseRepository = presetSeanceExerciseRepository;
    }

    public void saveAll(List<PresetSeanceExercise> relation) {
        presetSeanceExerciseRepository.saveAll(relation);
    }

    public void deleteByPresetId(Long idPreset) {
        presetSeanceExerciseRepository.deleteByPresetId(idPreset);

    }

}
