package com.example.dyeTrack.in.infoExerciseUser.dto.out;

public class ReturnInfoExerciseUserDTO {
    private Boolean favorite;
    private String note;
    private Long idExercise;

    public ReturnInfoExerciseUserDTO() {
    }

    public ReturnInfoExerciseUserDTO(Long idExercise, String note, Boolean favorite) {
        this.favorite = favorite;
        this.note = note;
        this.idExercise = idExercise;

    }

    public Long getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
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
