package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeance;

public interface PresetSeancePort {
    PresetSeance save(PresetSeance relExerciseMuscles);
    
    List<PresetSeance> findAllPresetOfUser(Long idUser,String name);
    PresetSeance getById(Long idPreset);
    PresetSeance update(PresetSeance presetSeance);
    void delete(PresetSeance presetSeance);//verif middleWare token
    

}
