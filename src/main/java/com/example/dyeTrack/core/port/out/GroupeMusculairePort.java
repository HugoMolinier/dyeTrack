package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.MuscleGroup;

public interface GroupeMusculairePort {
    List<MuscleGroup> getAll();

    void save(MuscleGroup nameGroupeMusculaire);

}
