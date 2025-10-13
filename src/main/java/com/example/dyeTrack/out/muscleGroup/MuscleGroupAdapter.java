package com.example.dyeTrack.out.muscleGroup;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.MuscleGroup;
import com.example.dyeTrack.core.port.out.MuscleGroupPort;

@Component
public class MuscleGroupAdapter implements MuscleGroupPort {
    private MuscleGroupRepository repository;

    public MuscleGroupAdapter(MuscleGroupRepository repository) {
        this.repository = repository;
    }

    public List<MuscleGroup> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(MuscleGroup muscleGroup) {
        repository.save(muscleGroup);
    }

}
