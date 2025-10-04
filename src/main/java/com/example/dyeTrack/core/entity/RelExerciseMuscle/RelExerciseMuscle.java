package com.example.dyeTrack.core.entity.RelExerciseMuscle;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Muscle;

import jakarta.persistence.*;

@Entity
public class RelExerciseMuscle {

    @EmbeddedId
    private RelExerciseMuscleId id = new RelExerciseMuscleId();

    @ManyToOne
    @MapsId("muscleId") // correspond au champ muscleId dans RelExerciseMuscleId
    @JoinColumn(name = "muscle_id")
    private Muscle muscle;

    @ManyToOne
    @MapsId("exerciceId") // correspond au champ exerciceId dans RelExerciseMuscleId
    @JoinColumn(name = "exercice_id")
    private Exercise exercice;

    private boolean principal; // true = muscle principal, false = secondaire

    public RelExerciseMuscle() {}

    public RelExerciseMuscle(Muscle muscle, Exercise exercice, boolean principal) {
        this.muscle = muscle;
        this.exercice = exercice;
        this.id = new RelExerciseMuscleId(muscle.getId(), exercice.getIdExercise());
        this.principal = principal;
    }


    // getters & setters
    public RelExerciseMuscleId getId() { return id; }
    public void setId(RelExerciseMuscleId id) { this.id = id; }

    public Muscle getMuscle() { return muscle; }
    public void setMuscle(Muscle muscle) { this.muscle = muscle; }

    public Exercise getExercice() { return exercice; }
    public void setExercice(Exercise exercice) { this.exercice = exercice; }

    public boolean isPrincipal() { return principal; }
    public void setPrincipal(boolean principal) { this.principal = principal; }

    public String toString() {
       return "RelExerciseMuscleId" + this.id +
	  " : muscle (" + this.muscle+ ")" +
	  ", exercice(" + this.exercice +")" +
        ", principal " + this.principal;
   }
}
