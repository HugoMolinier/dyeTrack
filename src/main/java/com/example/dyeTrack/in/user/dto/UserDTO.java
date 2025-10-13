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
    private Date birthdate;

    @Schema(description = "Height en cm", example = "180")
    private Integer height;

    @Schema(description = "Sexe masculin ou féminin", example = "true")
    private Boolean sexeMale;

    public UserDTO() {
    }

    public UserDTO(Long id, String pseudo, Date dateRegister, Date birthdate, Integer height, Boolean sexeMale) {
        this.id = id;
        this.pseudo = pseudo;
        this.dateRegister = dateRegister;
        this.birthdate = birthdate;
        this.height = height;
        this.sexeMale = sexeMale;
    }

    public UserDTO(User user) {
        this(user.getId(), user.getPseudo(), user.getDateRegister(), user.getBirthdate(), user.getHeight(),
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

    public Date getBirthdate() {
        return birthdate;
    }

    public Integer getHeight() {
        return height;
    }

    public Boolean getSexeMale() {
        return sexeMale;
    }
}
