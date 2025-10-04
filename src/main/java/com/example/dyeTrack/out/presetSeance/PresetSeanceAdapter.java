package com.example.dyeTrack.out.presetSeance;

import java.util.List;

import org.springframework.stereotype.Component;


import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.port.out.PresetSeancePort;

@Component
public class PresetSeanceAdapter implements PresetSeancePort{
    private final PresetSeanceRepository presetSeanceRepository;

    public PresetSeanceAdapter(PresetSeanceRepository presetSeanceRepository){
        this.presetSeanceRepository = presetSeanceRepository;
    }

    public PresetSeance getById(Long idPresetSeance){
        return presetSeanceRepository.findById(idPresetSeance).orElse(null);
    }

    public List<PresetSeance> findAllPresetOfUser(Long idUser, String name){
        return presetSeanceRepository.findAllPresetOfUser(idUser,name);
    }


    public PresetSeance save(PresetSeance presetSeance){
        return presetSeanceRepository.save(presetSeance);
    }

    public PresetSeance update(PresetSeance presetSeance) {
        return presetSeanceRepository.save(presetSeance);
    }
    public void delete(PresetSeance presetSeance){
        presetSeanceRepository.delete(presetSeance);
    }



    
}
