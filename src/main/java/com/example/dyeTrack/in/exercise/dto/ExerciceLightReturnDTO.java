package com.example.dyeTrack.in.exercise.dto;

import com.example.dyeTrack.core.entity.Exercise;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExerciceLightReturnDTO extends ExerciceUltraLightReturnDTO {

    private String description;
    private String linkVideo;
    private Long idCreator;

    public ExerciceLightReturnDTO() {
    }

    public ExerciceLightReturnDTO(Long idExercice, String nameFR, String description, String linkVideo, Long idUser) {
        super(idExercice, nameFR);
        this.description = description;
        this.linkVideo = linkVideo;
        this.idCreator = idUser;
    }

    public ExerciceLightReturnDTO(Exercise exercise) {
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
}
