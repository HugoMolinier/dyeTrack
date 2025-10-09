package com.example.dyeTrack.core.entity.infoExerciceUser;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.User;

import jakarta.persistence.*;

@Entity
public class InfoExerciceUser {

    @EmbeddedId
    private InfoExerciceUserId id = new InfoExerciceUserId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idExercise")
    @JoinColumn(name = "exercice_id", nullable = false)
    private Exercise exercice;

    private String note;
    private Boolean favorie;

    // --- Constructeurs ---
    public InfoExerciceUser() {
    }

    public InfoExerciceUser(User user, Exercise exercice, String note, Boolean favorie) {
        this.user = user;
        this.exercice = exercice;
        this.id = new InfoExerciceUserId(user.getId(), exercice.getIdExercise());
        this.note = note;
        this.favorie = favorie;
    }

    // --- Getters & Setters ---
    public InfoExerciceUserId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Exercise getExercice() {
        return exercice;
    }

    public void setExercice(Exercise exercice) {
        this.exercice = exercice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isFavorie() {
        return favorie;
    }

    public void setFavorie(Boolean favorie) {
        this.favorie = favorie;
    }
}
