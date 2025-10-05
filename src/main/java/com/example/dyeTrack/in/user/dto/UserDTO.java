package com.example.dyeTrack.in.user.dto;

import java.sql.Date;
import com.example.dyeTrack.core.entity.User;

public class UserDTO {

    private final Long id;
    private final String pseudo;
    private final Date dateRegister;
    private final Date dateNaissance;

    private final Integer taille;
    private final Boolean sexeMale;

    // Constructeur principal
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
