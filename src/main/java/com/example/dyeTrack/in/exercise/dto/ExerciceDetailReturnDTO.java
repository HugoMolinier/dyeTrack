package com.example.dyeTrack.in.exercise.dto;

import java.util.List;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.valueobject.MuscleInfo;


public class ExerciceDetailReturnDTO extends ExerciceLightReturnDTO {


    private List<MuscleInfo> muscleInfos;

    public ExerciceDetailReturnDTO() {
        super();
    }

    public ExerciceDetailReturnDTO(Long idExercice, String nameFR, String description, String linkVideo, Long idUser,List<MuscleInfo> muscleInfos) {
        super(idExercice, nameFR, description, linkVideo, idUser);
        this.muscleInfos=muscleInfos;
    }

    public ExerciceDetailReturnDTO(Exercise exercise,List<MuscleInfo> muscleInfos) {
        super(exercise);
        this.muscleInfos=muscleInfos;
    }



    public List<MuscleInfo> getMuscleInfos(){
        return muscleInfos;
    }

    public void setMuscleInfos(List<MuscleInfo> muscleInfos){
        this.muscleInfos = muscleInfos;
    }
}
