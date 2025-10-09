package com.example.dyeTrack.in.infoExerciceUser.dto;

public class UpdateInfoExerciceUserDTO {
    private Boolean favorite; // au lieu de favorie

    private String note;

    public UpdateInfoExerciceUserDTO() {
    }

    public UpdateInfoExerciceUserDTO(Boolean favorite, String note) {
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
