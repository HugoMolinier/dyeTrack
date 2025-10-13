package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.MuscleGroup;

public interface GroupeMusculaireUseCase {
    List<MuscleGroup> getAll();
}