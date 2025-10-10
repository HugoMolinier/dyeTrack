package com.example.dyeTrack.in.user.dto;

import java.sql.Date;
import com.example.dyeTrack.core.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserDTO {

    @Schema(description = "Identifiant de l'utilisateur", example = "1")
    private Long id;

    @Schema(description = "Pseudo de l'utilisateur", example = "MyPseudo")
    private String pseudo;

    @Schema(description = "Date d'inscription", example = "2025-10-10T12:00:00.000Z")
    private Date dateRegister;

    @Schema(description = "Date de naissance", example = "1995-05-15T00:00:00.000Z")
    private Date dateNaissance;

    @Schema(description = "Taille en cm", example = "180")
    private Integer taille;

    @Schema(description = "Sexe masculin ou féminin", example = "true")
    private Boolean sexeMale;

    public UserDTO() {
    }

    public UserDTO(Long id, String pseudo, Date dateRegister, Date dateNaissance, Integer taille, Boolean sexeMale) {
        this.id = id;
        this.pseudo = pseudo;
        this.dateRegister = dateRegister;
        this.dateNaissance = dateNaissance;
        this.taille = taille;
        this.sexeMale = sexeMale;
    }

    public UserDTO(User user) {
        this(user.getId(), user.getPseudo(), user.getDateRegister(), user.getDateNaissance(), user.getTaille(),
                user.getSexeMale());
    }

    public Long getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
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
}
