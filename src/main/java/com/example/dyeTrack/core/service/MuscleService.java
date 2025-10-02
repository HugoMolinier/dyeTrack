package com.example.dyeTrack.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.port.in.MuscleUseCase;
import com.example.dyeTrack.core.port.out.MusclePort;


@Service
public class MuscleService implements MuscleUseCase {

    private MusclePort musclePort;

    public MuscleService (MusclePort musclePort){
        this.musclePort = musclePort;
    }

    public Muscle getById(Long id){
        return musclePort.getById(id);
    }
    public List<Muscle> getByName(String name){
        return musclePort.getByName(name);
    }
    public List<Muscle> getAll(){
        return musclePort.getAll();
    }

    public List<Muscle> getByIDGroupeMuscle(List<Integer> muscle){
        return musclePort.getByIDGroupeMuscle(muscle);
    }

    
}
