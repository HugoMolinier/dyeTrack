package com.example.dyeTrack.core.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String pseudo;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private Date dateRegister;

    private Date birthdate;

    private Integer height;
    private Boolean sexeMale;

    @OneToMany(mappedBy = "user")
    private List<PresetSeance> presets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfoExerciseUser> relRecensementExercises = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DayDataOfUser> dataOfUsers = new ArrayList<>();

    // Getter et Setter
    public List<InfoExerciseUser> getRelRecensementExercises() {
        return relRecensementExercises;
    }

    public void setRelRecensementExercises(List<InfoExerciseUser> relRecensementExercises) {
        this.relRecensementExercises = relRecensementExercises;
    }

    public List<DayDataOfUser> getDayDataOfUser() {
        return dataOfUsers;
    }

    public void setDayDataOfUser(List<DayDataOfUser> dataOfUsers) {
        this.dataOfUsers = dataOfUsers;
    }

    // --- Constructeurs ---
    public User() {
    }

    public User(String pseudo, String email, String password) {
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
    }

    public User(String pseudo, String email, String password, Date dateRegister, Date birthdate, Integer height,
            Boolean sexeMale) {
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.dateRegister = dateRegister;
        this.birthdate = birthdate;
        this.height = height;
        this.sexeMale = sexeMale;
    }

    // --- Getters ---
    public Long getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public Integer getHeight() {
        return height;
    }

    public Boolean getSexeMale() {
        return sexeMale;
    }

    public List<PresetSeance> getPresets() {
        return presets;
    }

    // --- Setters ---
    public void setId(Long id) {
        this.id = id;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setSexeMale(Boolean sexeMale) {
        this.sexeMale = sexeMale;
    }

    public void setPresets(List<PresetSeance> presets) {
        this.presets = presets;
    }
}
