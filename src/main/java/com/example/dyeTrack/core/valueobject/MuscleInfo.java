package com.example.dyeTrack.core.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public class MuscleInfo {

    private Long idMuscle;
    private boolean principal = true;

    public MuscleInfo() {
    }

    public MuscleInfo(Long idMuscle, boolean principal) {
        this.idMuscle = idMuscle;
        this.principal = principal;
    }

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
