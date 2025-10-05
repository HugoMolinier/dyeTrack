package com.example.dyeTrack.in.exercise.dto;

import com.example.dyeTrack.core.entity.Exercise;

public class ExerciceLightReturnDTO implements ExerciceReturnDTO {

    private Long idExercice;
    private String nameFR;
    private String description;
    private String linkVideo;
    private Long idCreator;

    public ExerciceLightReturnDTO() {
    }

    public ExerciceLightReturnDTO(Long idExercice, String nameFR, String description, String linkVideo, Long idUser) {
        this.idExercice = idExercice;
        this.nameFR = nameFR;
        this.description = description;
        this.linkVideo = linkVideo;
        this.idCreator = idUser;
    }

    public ExerciceLightReturnDTO(Exercise exercise) {
        this.idExercice = exercise.getIdExercise();
        this.nameFR = exercise.getNameFR();
        this.description = exercise.getDescription();
        this.linkVideo = exercise.getLinkVideo();
        this.idCreator = exercise.getUser() != null ? exercise.getUser().getId() : null;
    }

    public Long getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Long idExercice) {
        this.idExercice = idExercice;
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

    public Long getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(Long idUser) {
        this.idCreator = idUser;
    }
}
