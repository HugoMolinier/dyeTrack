package com.example.dyeTrack.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.exception.ExerciseCreationException;
import com.example.dyeTrack.core.port.in.ExerciseUseCase;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.MusclePort;
import com.example.dyeTrack.core.port.out.RelExerciseMusclePort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.valueobject.MuscleInfo;

import jakarta.transaction.Transactional;

@Service
public class ExerciseService implements ExerciseUseCase {
    private final ExercisePort exercisePort;
    private final MusclePort musclePort;
    private final UserPort userPort;
    private final RelExerciseMusclePort relExerciseMusclePort;

    public ExerciseService (ExercisePort exercisePort,UserPort userPort,RelExerciseMusclePort relExerciseMusclePort,MusclePort musclePort){
        this.exercisePort = exercisePort;
        this.userPort = userPort;
        this.relExerciseMusclePort = relExerciseMusclePort;
        this.musclePort = musclePort;
    }


    public Exercise getByIdExercise(Long idExercise, Boolean onlyPrincipalMuscle){
        Exercise e = exercisePort.getByIdExercise( idExercise);
         e.getRelExerciseMuscles().removeIf(rem -> !rem.isPrincipal());
         return e;
    }

    public List<Exercise> getAll(String name, Boolean officialExercise, Long idUser, Boolean onlyPrincipalMuscle){
        List<Exercise> exercises = exercisePort.getAll( name,  officialExercise,  idUser);
        if(onlyPrincipalMuscle){
            for(Exercise e : exercises){
                e.getRelExerciseMuscles().removeIf(rem -> !rem.isPrincipal());
            }
        }
        return exercises;
    }

    

    @Transactional
    public void create(String nameFR, String description, String linkVideo, Long idUser,List<MuscleInfo> relExerciseMuscles) {
        if (nameFR == null) throw new ExerciseCreationException("nameFR empty");
        if (idUser ==null) throw new ExerciseCreationException("idUser empty");
        if (relExerciseMuscles == null || relExerciseMuscles.isEmpty()) throw new ExerciseCreationException("La liste des muscles ne peut pas être vide");
        User user = userPort.get(idUser);
        if(user == null) throw new ExerciseCreationException("user Not found with id "+ idUser);
        Exercise exercise = exercisePort.create(new Exercise(nameFR,description,linkVideo,user));
        List<RelExerciseMuscle> relExerciseMusclesArray =  new ArrayList<RelExerciseMuscle>() ;
        int principalCount = 0;
        for(MuscleInfo muscleInfo : relExerciseMuscles){
            Muscle muscle = musclePort.getById(muscleInfo.getIdMuscle());
            if(muscle == null) throw new ExerciseCreationException("muscle Not found with id "+ muscleInfo.getIdMuscle());
            relExerciseMusclesArray.add(new RelExerciseMuscle(muscle,exercise,muscleInfo.isPrincipal()));
            if (muscleInfo.isPrincipal()) principalCount++;
        }
        if(principalCount != 1) throw new ExerciseCreationException("Il doit y avoir 1 muscle principal");
        relExerciseMusclePort.saveAll(relExerciseMusclesArray);
    }

    
}
