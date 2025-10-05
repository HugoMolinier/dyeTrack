package com.example.dyeTrack.out.presetSeanceExercice;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.PresetSeanceExercice.PresetSeanceExercice;
import com.example.dyeTrack.core.port.out.PresetSeanceExercicePort;

@Component
public class PresetSeanceExerciceAdapter implements PresetSeanceExercicePort {
    private final PresetSeanceExerciceRepository presetSeanceExerciceRepository;

    public PresetSeanceExerciceAdapter(PresetSeanceExerciceRepository presetSeanceExerciceRepository) {
        this.presetSeanceExerciceRepository = presetSeanceExerciceRepository;
    }

    public void saveAll(List<PresetSeanceExercice> relation) {
        presetSeanceExerciceRepository.saveAll(relation);
    }

    public void deleteByPresetId(Long idPreset) {
        presetSeanceExerciceRepository.deleteByPresetId(idPreset);

    }

}
