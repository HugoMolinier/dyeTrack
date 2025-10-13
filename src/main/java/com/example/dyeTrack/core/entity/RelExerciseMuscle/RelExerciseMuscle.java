package com.example.dyeTrack.core.entity.RelExerciseMuscle;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Muscle;

import jakarta.persistence.*;

@Entity
public class RelExerciseMuscle {

    @EmbeddedId
    private RelExerciseMuscleId id = new RelExerciseMuscleId();

    @ManyToOne
    @MapsId("muscleId")
    @JoinColumn(name = "muscle_id")
    private Muscle muscle;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private boolean principal;

    public RelExerciseMuscle() {
    }

    public RelExerciseMuscle(Muscle muscle, Exercise exercise, boolean principal) {
        this.muscle = muscle;
        this.exercise = exercise;
        this.id = new RelExerciseMuscleId(muscle.getId(), exercise.getIdExercise());
        this.principal = principal;
    }

    // getters & setters
    public RelExerciseMuscleId getId() {
        return id;
    }

    public void setId(RelExerciseMuscleId id) {
        this.id = id;
    }

    public Muscle getMuscle() {
        return muscle;
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public String toString() {
        return "RelExerciseMuscleId" + this.id +
                " : muscle (" + this.muscle + ")" +
                ", exercise(" + this.exercise + ")" +
                ", principal " + this.principal;
    }
}
