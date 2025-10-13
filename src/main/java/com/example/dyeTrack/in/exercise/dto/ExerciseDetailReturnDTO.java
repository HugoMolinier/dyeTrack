package com.example.dyeTrack.in.exercise.dto;

import java.util.List;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExerciseDetailReturnDTO extends ExerciseUltraLightReturnDTO {

    private List<MuscleInfo> muscleInfos;
    private Long mainFocusGroup;

    private String description;
    private String linkVideo;
    private Long idCreator;

    public ExerciseDetailReturnDTO() {
        super();
    }

    public ExerciseDetailReturnDTO(Long idExercise, String nameFR, String description, String linkVideo, Long idUser,
            List<MuscleInfo> muscleInfos, Long mainFocusGroup) {
        super(idExercise, nameFR);
        this.description = description;
        this.linkVideo = linkVideo;
        this.idCreator = idUser;
        this.muscleInfos = muscleInfos;
        this.mainFocusGroup = mainFocusGroup;
    }

    public ExerciseDetailReturnDTO(Exercise exercise, List<MuscleInfo> muscleInfos, Long mainFocusGroup) {
        super(exercise);
        this.description = exercise.getDescription();
        this.linkVideo = exercise.getLinkVideo();
        this.idCreator = exercise.getUser() != null ? exercise.getUser().getId() : null;
        this.muscleInfos = muscleInfos;
        this.mainFocusGroup = mainFocusGroup;
    }

    public ExerciseDetailReturnDTO(Exercise exercise) {
        super(exercise);
        this.description = exercise.getDescription();
        this.linkVideo = exercise.getLinkVideo();
        this.idCreator = exercise.getUser() != null ? exercise.getUser().getId() : null;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public Long getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(Long idUser) {
        this.idCreator = idUser;
    }

    public List<MuscleInfo> getMuscleInfos() {
        return muscleInfos;
    }

    public void setMuscleInfos(List<MuscleInfo> muscleInfos) {
        this.muscleInfos = muscleInfos;
    }

    public Long getMainFocusGroup() {
        return mainFocusGroup;
    }

    public void setMainFocusGroup(Long newMainFocusGroup) {
        this.mainFocusGroup = newMainFocusGroup;
    }
}
