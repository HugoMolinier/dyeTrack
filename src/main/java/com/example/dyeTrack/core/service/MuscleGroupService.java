package com.example.dyeTrack.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.MuscleGroup;
import com.example.dyeTrack.core.port.in.MuscleGroupUseCase;
import com.example.dyeTrack.core.port.out.MuscleGroupPort;

@Service
public class MuscleGroupService implements MuscleGroupUseCase {

    private final MuscleGroupPort groupePort;

    public MuscleGroupService(MuscleGroupPort groupePort) {
        this.groupePort = groupePort;
    }

    public List<MuscleGroup> getAll() {
        return groupePort.getAll();
    }

}
