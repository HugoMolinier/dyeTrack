package com.example.dyeTrack.in.presetSeance.dto;

import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciceVO;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PresetDetailReturnDTO extends PresetLightReturnDTO {

    private List<PresetSeanceExerciceVO> presetSeanceExerciceVOs;

    public PresetDetailReturnDTO() {
        super();
    }

    public PresetDetailReturnDTO(Long idPreset, String name, List<PresetSeanceExerciceVO> presetSeanceExerciceVOs) {
        super(idPreset, name);
        this.presetSeanceExerciceVOs = presetSeanceExerciceVOs;
    }

    public PresetDetailReturnDTO(PresetSeance presetSeance, List<PresetSeanceExerciceVO> presetSeanceExerciceVOs) {
        super(presetSeance);
        this.presetSeanceExerciceVOs = presetSeanceExerciceVOs;
    }

    public List<PresetSeanceExerciceVO> getPresetSeanceExerciceVO() {
        return presetSeanceExerciceVOs;
    }

    public void setMuscleInfos(List<PresetSeanceExerciceVO> presetSeanceExerciceVOs) {
        this.presetSeanceExerciceVOs = presetSeanceExerciceVOs;
    }

}
