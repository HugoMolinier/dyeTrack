package com.example.dyeTrack.in.presetSeance.dto;

import java.util.List;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciceVO;

import jakarta.validation.constraints.NotBlank;

public class PresetSeanceCreateRequestDTO {
    @NotBlank(message = "Le nom ne peut pas être vide")
    private String name;

    private List<PresetSeanceExerciceVO> presetSeanceExerciceVOs;

    // --- Getters ---
    public String getName() {
        return name;
    }

    public List<PresetSeanceExerciceVO> getPresetSeanceExerciceVOs() {
        return presetSeanceExerciceVOs;
    }

    // --- Setters ---
    public void setName(String name) {
        this.name = name;
    }

    public void setPresetSeanceExerciceVOs(List<PresetSeanceExerciceVO> presetSeanceExerciceVOs) {
        this.presetSeanceExerciceVOs = presetSeanceExerciceVOs;
    }
}
