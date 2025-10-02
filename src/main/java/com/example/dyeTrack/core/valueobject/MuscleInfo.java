package com.example.dyeTrack.core.valueobject;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class MuscleInfo {

    @NotNull
    private Long idMuscle;

    private boolean principal = true; // valeur par défaut

    public MuscleInfo() {}

    public MuscleInfo(Long idMuscle, boolean principal) {
        this.idMuscle = idMuscle;
        this.principal = principal;
    }

    // getters & setters
    public Long getIdMuscle() {
        return idMuscle;
    }

    public void setIdMuscle(Long idMuscle) {
        this.idMuscle = idMuscle;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }
}
