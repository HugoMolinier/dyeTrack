package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.Equipement;

public interface EquipementPort {
    List<Equipement> getAll();
    void save(Equipement equipement);
    
}
