package com.example.dyeTrack.in.presetSeance.dto;

import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeance;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PresetDetailReturnDTO extends PresetLightReturnDTO {

    private List<PresetSeanceExerciceVODTO> presetSeanceExerciceVODTO;

    public PresetDetailReturnDTO() {
        super();
    }

    public PresetDetailReturnDTO(Long idPreset, String name, List<PresetSeanceExerciceVODTO> presetSeanceExerciceVOs) {
        super(idPreset, name);
        this.presetSeanceExerciceVODTO = presetSeanceExerciceVOs;
    }

    public PresetDetailReturnDTO(PresetSeance presetSeance, List<PresetSeanceExerciceVODTO> presetSeanceExerciceVOs) {
        super(presetSeance);
        this.presetSeanceExerciceVODTO = presetSeanceExerciceVOs;
    }

    public List<PresetSeanceExerciceVODTO> getPresetSeanceExerciceVODTO() {
        return presetSeanceExerciceVODTO;
    }

    public void setPresetSeanceExerciceVODTO(List<PresetSeanceExerciceVODTO> presetSeanceExerciceVODTO) {
        this.presetSeanceExerciceVODTO = presetSeanceExerciceVODTO;
    }

}
