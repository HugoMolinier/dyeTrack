package com.example.dyeTrack.in.presetSeance.dto;

import com.example.dyeTrack.core.entity.PresetSeance;




public class PresetLightReturnDTO implements PresetReturnDTO {

    private Long idPresetSeance;
    private String name;

    public PresetLightReturnDTO() {
    }

    public PresetLightReturnDTO(Long idPresetSeance, String name) {
        this.idPresetSeance = idPresetSeance;
        this.name = name;

    }

    public PresetLightReturnDTO(PresetSeance presetSeance) {
        this.idPresetSeance =presetSeance.getIdPresetSeance();
        this.name = presetSeance.getName();
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



}
