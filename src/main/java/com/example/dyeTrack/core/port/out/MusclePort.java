package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.Muscle;

public interface MusclePort {
    Muscle getById(Long id);

    List<Muscle> getByName(String name);

    List<Muscle> getAll();

    List<Muscle> getByIDGroupeMuscle(List<Integer> muscle);

    void save(Muscle muscle);

}
