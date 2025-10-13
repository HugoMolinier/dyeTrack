package com.example.dyeTrack.core.entity.infoExerciseUser;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.User;

import jakarta.persistence.*;

@Entity
public class InfoExerciseUser {

    @EmbeddedId
    private InfoExerciseUserId id = new InfoExerciseUserId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idExercise")
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    private String note;
    private Boolean favorite;

    // --- Constructeurs ---
    public InfoExerciseUser() {
    }

    public InfoExerciseUser(User user, Exercise exercise, String note, Boolean favorite) {
        this.user = user;
        this.exercise = exercise;
        this.id = new InfoExerciseUserId(user.getId(), exercise.getIdExercise());
        this.note = note;
        this.favorite = favorite;
    }

    // --- Getters & Setters ---
    public InfoExerciseUserId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
