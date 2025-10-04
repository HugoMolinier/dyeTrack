package com.example.dyeTrack.in.presetSeance.dto;

import java.util.List;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciceVO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PresetSeanceCreateRequestDTO {
    @NotBlank(message = "Le nom ne peut pas être vide")
    private String name;

    @NotNull(message = "L'ID utilisateur ne peut pas être null")
    private Long idUser;
    private List<PresetSeanceExerciceVO> presetSeanceExerciceVOs;

    // --- Getters ---
    public String getName() {
        return name;
    }

    public Long getIdUser() {
        return idUser;
    }

    public List<PresetSeanceExerciceVO> getPresetSeanceExerciceVOs() {
        return presetSeanceExerciceVOs;
    }

    // --- Setters ---
    public void setName(String name) {
        this.name = name;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setPresetSeanceExerciceVOs(List<PresetSeanceExerciceVO> presetSeanceExerciceVOs) {
        this.presetSeanceExerciceVOs = presetSeanceExerciceVOs;
    }
}
