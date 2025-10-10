package com.example.dyeTrack.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Muscle {
    @Id
    @NotNull
    private Long id;

    @NotBlank
    private String nameFR;

    @NotBlank
    private String nameEN;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "groupe_musculaire_id", nullable = false)
    private GroupeMusculaire groupeMusculaire;

    public Muscle(Long id, String nameFR, String nameEN, GroupeMusculaire groupeMusculaire) {
        this.nameFR = nameFR;
        this.id = id;
        this.nameEN = nameEN;
        this.groupeMusculaire = groupeMusculaire;
    }

    protected Muscle() {
    } // Pour Hibernate

    public Long getId() {
        return id;
    }

    public String getNameFR() {
        return nameFR;
    }

    public String getNameEN() {
        return nameEN;
    }

    public GroupeMusculaire getGroupeMusculaire() {
        return groupeMusculaire;
    }

    @Override
    public String toString() {
        return "nameFR" + this.nameFR +
                " : id " + this.id +
                ", groupeMusculair( " + this.groupeMusculaire + ")";
    }
}
