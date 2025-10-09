package com.example.dyeTrack.in.infoExerciceUser.dto.out;

public class ReturnInfoExerciceUserDTO {
    private Boolean favorite;
    private String note;
    private Long idExercice;

    public ReturnInfoExerciceUserDTO() {
    }

    public ReturnInfoExerciceUserDTO(Long idExercice, String note, Boolean favorite) {
        this.favorite = favorite;
        this.note = note;
        this.idExercice = idExercice;

    }

    public Long getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Long idExercice) {
        this.idExercice = idExercice;
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
