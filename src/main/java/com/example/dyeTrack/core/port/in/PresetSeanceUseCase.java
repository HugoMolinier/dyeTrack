package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.entity.PresetSeanceExercice.PresetSeanceExercice;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciceVO;

public interface PresetSeanceUseCase {
    PresetSeance save(String name, Long idUserWhoAdd,List<PresetSeanceExerciceVO> relExerciseMuscles);
    List<PresetSeance> getAllPresetOfUser(Long idUser,String name);
    PresetSeance getById(Long idPreset,Long idUser);
    PresetSeance update(Long idPreset,Long idUserQuiModifie, String newName, List<PresetSeanceExercice> relExerciseMuscles);
    void delete(Long idpresetSeance,Long idUserQuiDelete);//verif middleWare token
}


