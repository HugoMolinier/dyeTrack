package com.example.dyeTrack.core.valueobject;


import jakarta.persistence.Embeddable;

@Embeddable
public class MuscleInfo extends IDNameValue {

    private boolean principal = true; // valeur par défaut


    public MuscleInfo() {}

    public MuscleInfo(Long idMuscle, boolean principal,String nameFR, String nameEN) {
        super(idMuscle,nameFR,nameEN);
        this.principal = principal;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }
}
