package com.example.dyeTrack.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.exception.EntityNotFoundException;
import com.example.dyeTrack.core.port.in.MuscleUseCase;
import com.example.dyeTrack.core.port.out.MusclePort;

@Service
public class MuscleService implements MuscleUseCase {

    private final MusclePort musclePort;

    public MuscleService(MusclePort musclePort) {
        this.musclePort = musclePort;
    }

    public Muscle getById(Long id) {
        Muscle muscle = musclePort.getById(id);
        if (muscle == null) {
            throw new EntityNotFoundException("Muscle not found with id " + id);
        }
        return muscle;
    }

    public List<Muscle> getByName(String name) {
        return musclePort.getByName(name);
    }

    public List<Muscle> getAll() {
        return musclePort.getAll();
    }

    public List<Muscle> getByIDGroupeMuscle(List<Integer> muscle) {
        return musclePort.getByIDGroupeMuscle(muscle);
    }

}
