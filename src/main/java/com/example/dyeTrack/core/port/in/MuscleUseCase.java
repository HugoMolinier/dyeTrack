package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.Muscle;


public interface MuscleUseCase {
    Muscle getById(Long id);
    List<Muscle> getByName(String name);
    List<Muscle> getAll();
    List<Muscle> getByIDGroupeMuscle(List<Integer> muscle);




    
}
