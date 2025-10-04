package com.example.dyeTrack.in.exercise.dto;

import java.util.List;

import com.example.dyeTrack.core.valueobject.MuscleInsertExercice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class ExerciseCreateDTO {

    @NotBlank(message = "nameFR est obligatoire")
    private String nameFR;
    private String description;
    private String linkVideo;

    @NotNull(message = "idUser est obligatoire")
    private Long idUser;

    @NotBlank(message = "relExerciseMuscle est obligatoire")
    private List<MuscleInsertExercice> relExerciseMuscle;

    public ExerciseCreateDTO() {
    }

    public ExerciseCreateDTO(String nameFR, String description, String linkVideo, Long idUser,List<MuscleInsertExercice> relExerciseMuscle) {
        this.nameFR = nameFR;
        this.description = description;
        this.linkVideo = linkVideo;
        this.idUser = idUser;
        this.relExerciseMuscle = relExerciseMuscle;
    }


    public String getNameFR() {
        return nameFR;
    }

    public void setNameFR(String nameFR) {
        this.nameFR = nameFR;
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

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public List<MuscleInsertExercice> getRelExerciseMuscles(){
        return relExerciseMuscle;
    }
    public void setRelExerciseMuscles(List<MuscleInsertExercice> relExerciseMuscle){
        this.relExerciseMuscle = relExerciseMuscle;
    }
}
