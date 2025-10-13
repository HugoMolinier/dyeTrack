package com.example.dyeTrack.in.presetSeance.dto;

import java.util.List;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciseVO;

import jakarta.validation.constraints.NotBlank;

public class PresetSeanceCreateRequestDTO {
    @NotBlank(message = "Le name ne peut pas être vide")
    private String name;

    private List<PresetSeanceExerciseVO> presetSeanceExerciseVOs;

    public PresetSeanceCreateRequestDTO() {
    }

    public PresetSeanceCreateRequestDTO(String name, List<PresetSeanceExerciseVO> presetSeanceExerciseVOs) {
        this.name = name;
        this.presetSeanceExerciseVOs = presetSeanceExerciseVOs;
    }

    public PresetSeanceCreateRequestDTO(String name) {
        this.name = name;
    }

    // --- Getters ---

    public String getName() {
        return name;
    }

    public List<PresetSeanceExerciseVO> getPresetSeanceExerciseVOs() {
        return presetSeanceExerciseVOs;
    }

    // --- Setters ---
    public void setName(String name) {
        this.name = name;
    }

    public void setPresetSeanceExerciseVOs(List<PresetSeanceExerciseVO> presetSeanceExerciseVOs) {
        this.presetSeanceExerciseVOs = presetSeanceExerciseVOs;
    }
}
