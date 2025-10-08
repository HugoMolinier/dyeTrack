package com.example.dyeTrack.in.presetSeance.dto;

import java.util.List;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciceVO;

import jakarta.validation.constraints.NotBlank;

public class PresetSeanceCreateRequestDTO {
    @NotBlank(message = "Le nom ne peut pas être vide")
    private String name;

    private List<PresetSeanceExerciceVO> presetSeanceExerciceVOs;

    public PresetSeanceCreateRequestDTO() {
    }

    public PresetSeanceCreateRequestDTO(String name, List<PresetSeanceExerciceVO> presetSeanceExerciceVOs) {
        this.name = name;
        this.presetSeanceExerciceVOs = presetSeanceExerciceVOs;
    }

    public PresetSeanceCreateRequestDTO(String name) {
        this.name = name;
    }

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
