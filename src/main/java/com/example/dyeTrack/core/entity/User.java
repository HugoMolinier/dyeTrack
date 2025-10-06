package com.example.dyeTrack.core.entity;

import java.sql.Date;
import java.util.List;

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

    private Date dateNaissance;

    private Integer taille;
    private Boolean sexeMale;

    @OneToMany(mappedBy = "user")
    private List<PresetSeance> presets;

    // --- Constructeurs ---
    public User() {
    }

    public User(String pseudo, String email, String password) {
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
    }

    public User(String pseudo, String email, String password, Date dateRegister, Date dateNaissance, Integer taille,
            Boolean sexeMale) {
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.dateRegister = dateRegister;
        this.dateNaissance = dateNaissance;
        this.taille = taille;
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

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public Integer getTaille() {
        return taille;
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

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setTaille(Integer taille) {
        this.taille = taille;
    }

    public void setSexeMale(Boolean sexeMale) {
        this.sexeMale = sexeMale;
    }

    public void setPresets(List<PresetSeance> presets) {
        this.presets = presets;
    }
}
