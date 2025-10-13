package com.example.dyeTrack.in.infoExerciseUser.dto;

public class UpdateInfoExerciseUserDTO {
    private Boolean favorite; // au lieu de favorite

    private String note;

    public UpdateInfoExerciseUserDTO() {
    }

    public UpdateInfoExerciseUserDTO(Boolean favorite, String note) {
        this.favorite = favorite;
        this.note = note;

    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
