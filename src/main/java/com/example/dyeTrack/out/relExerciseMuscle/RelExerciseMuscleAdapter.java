package com.example.dyeTrack.out.relExerciseMuscle;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.port.out.RelExerciseMusclePort;

@Component
public class RelExerciseMuscleAdapter implements RelExerciseMusclePort{
    private final RelExerciseMuscleRepository relExerciseMuscleRepository;

    public RelExerciseMuscleAdapter(RelExerciseMuscleRepository relExerciseMuscleRepository){
        this.relExerciseMuscleRepository = relExerciseMuscleRepository;
    }

    public void saveAll(List<RelExerciseMuscle> relation){
        relExerciseMuscleRepository.saveAll(relation);
    }


    public void deleteByExerciceId(Long idExercice){
        relExerciseMuscleRepository.deleteByExerciseId(idExercice);

    }


    
}
