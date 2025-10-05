package com.example.dyeTrack.in.exercise.dto;

import java.util.List;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.valueobject.IDNameValue;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExerciceDetailReturnDTO extends ExerciceLightReturnDTO {

    private List<MuscleInfo> muscleInfos;
    private IDNameValue mainFocusGroup;

    public ExerciceDetailReturnDTO() {
        super();
    }

    public ExerciceDetailReturnDTO(Long idExercice, String nameFR, String description, String linkVideo, Long idUser,
            List<MuscleInfo> muscleInfos, IDNameValue mainFocusGroup) {
        super(idExercice, nameFR, description, linkVideo, idUser);
        this.muscleInfos = muscleInfos;
        this.mainFocusGroup = mainFocusGroup;
    }

    public ExerciceDetailReturnDTO(Exercise exercise, List<MuscleInfo> muscleInfos, IDNameValue mainFocusGroup) {
        super(exercise);
        this.muscleInfos = muscleInfos;
        this.mainFocusGroup = mainFocusGroup;
    }

    public List<MuscleInfo> getMuscleInfos() {
        return muscleInfos;
    }

    public void setMuscleInfos(List<MuscleInfo> muscleInfos) {
        this.muscleInfos = muscleInfos;
    }

    public IDNameValue getMainFocusGroup() {
        return mainFocusGroup;
    }

    public void setMainFocusGroup(IDNameValue newMainFocusGroup) {
        this.mainFocusGroup = newMainFocusGroup;
    }
}
