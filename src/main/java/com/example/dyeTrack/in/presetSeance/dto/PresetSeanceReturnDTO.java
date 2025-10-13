package com.example.dyeTrack.in.presetSeance.dto;

import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeance;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PresetSeanceReturnDTO {

    private List<PresetSeanceExerciseVODTO> presetSeanceExerciseVODTO;
    private Long idPresetSeance;
    private String name;

    public PresetSeanceReturnDTO() {
    }

    public PresetSeanceReturnDTO(Long idPresetSeance, String name,
            List<PresetSeanceExerciseVODTO> presetSeanceExerciseVOs) {
        this.idPresetSeance = idPresetSeance;
        this.name = name;
        this.presetSeanceExerciseVODTO = presetSeanceExerciseVOs;
    }

    public PresetSeanceReturnDTO(PresetSeance presetSeance, List<PresetSeanceExerciseVODTO> presetSeanceExerciseVOs) {
        this.idPresetSeance = presetSeance.getIdPresetSeance();
        this.name = presetSeance.getName();
        this.presetSeanceExerciseVODTO = presetSeanceExerciseVOs;
    }

    public Long getIdPreset() {
        return idPresetSeance;
    }

    public void setIdPreset(Long idPresetSeance) {
        this.idPresetSeance = idPresetSeance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PresetSeanceExerciseVODTO> getPresetSeanceExerciseVODTO() {
        return presetSeanceExerciseVODTO;
    }

    public void setPresetSeanceExerciseVODTO(List<PresetSeanceExerciseVODTO> presetSeanceExerciseVODTO) {
        this.presetSeanceExerciseVODTO = presetSeanceExerciseVODTO;
    }

}
