package com.example.dyeTrack.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeanceExercice.PresetSeanceExercice;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExercise;

    @NotBlank
    private String nameFR;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String linkVideo;

    @ManyToOne
    @JoinColumn(name = "idCreator", nullable = true) // si null c'est que c'est admin add
    private User user;

    @OneToMany(mappedBy = "exercice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RelExerciseMuscle> relExerciseMuscles = new ArrayList<>();

    @OneToMany(mappedBy = "exercice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PresetSeanceExercice> presetSeanceExercices = new ArrayList<>();

    @OneToMany(mappedBy = "exercice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfoExerciceUser> relRecensementExercices = new ArrayList<>();

    public List<InfoExerciceUser> getRelRecensementExercices() {
        return relRecensementExercices;
    }

    public void setRelRecensementExercices(List<InfoExerciceUser> relRecensementExercices) {
        this.relRecensementExercices = relRecensementExercices;
    }

    public List<RelExerciseMuscle> getRelExerciseMuscles() {
        return relExerciseMuscles;
    }

    public Exercise() {
    }

    public Exercise(String nameFR, User user) {
        this(nameFR, null, null, user);
    }

    public Exercise(String nameFR, String description, User user) {
        this(nameFR, description, null, user);
    }

    public Exercise(String nameFR, String description, String linkVideo, User user) {
        this.nameFR = nameFR;
        this.description = description;
        this.linkVideo = linkVideo;
        this.user = user;
    }

    public Long getIdExercise() {
        return idExercise;
    }

    public String getNameFR() {
        return nameFR;
    }

    public String getDescription() {
        return description;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String link) {
        this.linkVideo = link;
    }

    public User getUser() {
        return user;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setNameFR(String newNameFR) {
        this.nameFR = newNameFR;
    }

    @Override
    public String toString() {
        return "idExercise" + this.idExercise +
                " : nameFR " + this.nameFR +
                ", user " + this.user;
    }
}
